/*
 * File: TProjectHandler.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 22, 2006
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS
 * 
 * License:
 * 		This program is free software: you can redistribute it and/or 
 * 		modify it under the terms of the GNU General Public License 
 * 		as published by the Free Software Foundation, either version 3
 * 		of the License, or (at your option) any later version.
 * 
 * 		This program is distributed in the hope that it will be useful,
 * 		but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 		GNU General Public License for more details.
 * 
 * 		You should have received a copy of the GNU General Public License
 *     	along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package tico.editor;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import tico.board.TBoard;
import tico.board.TProject;
import tico.board.encoding.InvalidFormatException;
import tico.board.encoding.TAttributeEncoder;
import tico.interpreter.TInterpreterProject;
import database.DB;

/**
 * Static class that manages the current editing project files.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */

public class TProjectHandler {
	private final static int ZIP_BUFFER_SIZE = 2048;

	private static String tempDirectoryPath;

	private static File tempDirectory;

	static {
		tempDirectory = new File(TFileHandler.getCurrentDirectory(), "temp");
		tempDirectoryPath = tempDirectory.getAbsolutePath();
		tempDirectory.mkdirs();
	}

	/**
	 * Returns the current temporal directory.
	 * 
	 * @return The current temporal directory
	 */
	public static File getTempDirectory() {
		return tempDirectory;
	}
	
	/**
	 * Returns the current temporal directory path.
	 * 
	 * @return The current temporal directory path
	 */
	public static String getTempDirectoryPath() {
		return tempDirectoryPath;
	}

	/**
	 * Deletes the current temporal directory and all its contents.
	 */
	public static void deleteTempDirectory() {
		TFileHandler.deleteDirectory(tempDirectory);
		tempDirectory.delete();
	}

	/**
	 * Deletes all the current temporal directory contents.
	 */
	public static void cleanTempDirectory() {
		TFileHandler.deleteDirectory(tempDirectory);
	}

	/**
	 * Copies a list of files from the current directory to the
	 * temporal directory.
	 * 
	 * @param partialPaths The list of files
	 * @throws IOException If there is any problem copying the files
	 */
	public static void copyCurrentToTemp(String[] partialPaths)
			throws IOException {
		
		for (int i = 0; i < partialPaths.length; i++) {
			File dstFile = new File(tempDirectory, partialPaths[i]);
			if (!dstFile.getParentFile().exists()){
				dstFile.getParentFile().mkdirs();
			}
			//To support compatibility with old project we save projects using two ways.
			try{
				File orFile = new File(DB.getInstance().getImagesPath() + File.separator + partialPaths[i].substring(partialPaths[i].lastIndexOf(File.separator)));
				TFileHandler.copyFile(orFile, dstFile);
			}catch (IOException e) {
				TFileHandler.copyFile(new File(TFileHandler.getCurrentDirectory(),
						partialPaths[i]), dstFile);
			}catch (SQLException e) {
				throw new IOException();
			}
		}
	}

	/**
	 * Stores the current termporal directory in the specified
	 * <code>zipFile</code>.
	 * 
	 * @param zipFile The specified <code>zipFile</code>
	 * @throws IOException If there is any problem with the <code>zipFile</code>
	 */
	public static void saveZip(File zipFile) throws IOException {
		if (zipFile.exists())
			zipFile.delete();
		zip(tempDirectory, zipFile);
	}

	/**
	 * Loads the current temporal directory with the contents of the specified
	 * <code>zipFile</code>.
	 * 
	 * @param zipFile The specified <code>zipFile</code>
	 * @throws IOException If there is any problem with the <code>zipFile</code>
	 */
	public static void loadZip(File zipFile) throws IOException {
		unzip(zipFile, tempDirectory);
	}

	/**
	 * Saves the specified <code>project</code> to the specified
	 * <code>zipFile</code>.
	 * 
	 * @param project The <code>project</code> to save
	 * @param zipFile The target <code>zipFile</code>
	 * @throws ParserConfigurationException If there are syntactic error
	 * in the XML document
	 * @throws IOException If there are file problems with the <code>zipFile</code>
	 * @throws TransformerException If there are problems transforming the
	 * XML document to text
	 */
	public static void saveProject(TProject project, File zipFile)
			throws ParserConfigurationException, IOException,
			TransformerException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
		Document doc = domBuilder.newDocument();
		
		doc.appendChild(project.XMLEncode(doc));
		
		TProjectHandler.cleanTempDirectory();
		TProjectHandler.copyCurrentToTemp(TAttributeEncoder.getFilePaths(doc));
		File file = new File(TProjectHandler.getTempDirectory(), "project.xml");
		BufferedWriter bufferWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		
		// Use a Transformer for output
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(bufferWriter);
		transformer.transform(source, result);
		bufferWriter.close();
		
		TProjectHandler.saveZip(zipFile);
		TProjectHandler.cleanTempDirectory();
	}

	/**
	 * Saves the specified <code>board</code> to the specified
	 * <code>zipFile</code>.
	 * 
	 * @param board The <code>board</code> to save
	 * @param zipFile The target <code>zipFile</code>
	 * @throws ParserConfigurationException If there are syntactic error
	 * in the XML document
	 * @throws IOException If there are file problems with the <code>zipFile</code>
	 * @throws TransformerException If there are problems transforming the
	 * XML document to text
	 */
	public static void saveBoard(TBoard board, File zipFile)
			throws ParserConfigurationException, IOException,
			TransformerException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
		Document doc = domBuilder.newDocument();

		doc.appendChild(board.XMLEncode(doc));

		TProjectHandler.cleanTempDirectory();
		TProjectHandler.copyCurrentToTemp(TAttributeEncoder.getFilePaths(doc));
		File file = new File(TProjectHandler.getTempDirectory(), "board.xml");
		BufferedWriter bufferWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

		// Use a Transformer for output
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(bufferWriter);
		transformer.transform(source, result);
		bufferWriter.close();

		TProjectHandler.saveZip(zipFile);
	}

	/**
	 * Creates an <code>editor project</code> using the information contained in a
	 * <code>zipFile</code>.
	 * 
	 * @param zipFile The source <code>zipFile</code>
	 * @return The created <code>project</code>
	 * @throws IOException If there are file problems with the <code>zipFile</code>
	 * @throws ParserConfigurationException If there are syntactic error
	 * in the XML document
	 * @throws InvalidFormatException If the XML document has an invalid format
	 * @throws SAXException If there are problems transforming the
	 * text to a XML document
	 */
	public static TProject loadProject(File zipFile) throws IOException,
		ParserConfigurationException, InvalidFormatException, SAXException {
		// Create XML Parsing objects
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		// Load project file contents
		TProjectHandler.cleanTempDirectory();
		TProjectHandler.loadZip(zipFile);
		// Get project name
		String projectName = zipFile.getName();
		projectName = projectName.substring(0, projectName.lastIndexOf('.'));
		
		// Load project
		File file = new File(TProjectHandler.getTempDirectory(), "project.xml");
		Document doc = docBuilder.parse(file);
		TProject project = TProject.XMLDecode(doc.getDocumentElement(), projectName);
		
		TProjectHandler.cleanTempDirectory();
		
		return project;
	}
	
	/**
	 * Creates an <code>interpreter project</code> using the information contained in a
	 * <code>zipFile</code>.
	 * 
	 * @param zipFile The source <code>zipFile</code>
	 * @return The created <code>project</code>
	 * @throws IOException If there are file problems with the <code>zipFile</code>
	 * @throws ParserConfigurationException If there are syntactic error
	 * in the XML document
	 * @throws InvalidFormatException If the XML document has an invalid format
	 * @throws SAXException If there are problems transforming the
	 * text to a XML document
	 */
	public static TInterpreterProject loadProjectInterpreter(File zipFile) throws IOException,
	ParserConfigurationException, InvalidFormatException, SAXException {
		
		TProjectHandler.cleanTempDirectory();
		TProjectHandler.loadZip(zipFile);
		
		// Get project name
		String projectName = zipFile.getName();
		projectName = projectName.substring(0, projectName.lastIndexOf('.'));

		// Load project
		File file = new File(TProjectHandler.getTempDirectory(), "project.xml");
		TInterpreterProject project= TInterpreterProject.XMLDecode(file, projectName);
		
		TProjectHandler.cleanTempDirectory();
		return project;
	}

	/**
	 * Creates a <code>board</code> using the information contained in a
	 * <code>zipFile</code>.
	 * 
	 * @param zipFile The source <code>zipFile</code>
	 * @return The created <code>board</code>
	 * @throws IOException If there are file problems with the <code>zipFile</code>
	 * @throws ParserConfigurationException If there are syntactic error
	 * in the XML document
	 * @throws InvalidFormatException If the XML document has an invalid format
	 * @throws SAXException If there are problems transforming the
	 * text to a XML document
	 */
	public static TBoard loadBoard(File zipFile)
			throws ParserConfigurationException, IOException, SAXException,
			InvalidFormatException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		TProjectHandler.cleanTempDirectory();
		TProjectHandler.loadZip(zipFile);

		File file = new File(TProjectHandler.getTempDirectory(), "board.xml");
		Document doc = docBuilder.parse(file);
		TBoard board = TBoard.XMLDecode(doc.getDocumentElement());
			
		TProjectHandler.cleanTempDirectory();
		
		return board;
	}

	// Zips a directory to a file
	private static void zip(File srcDir, File dstFile) throws IOException {
		// Create a zip output stream to zip the data to
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dstFile));
		// Zip all the contents of the directory into the zip output stream
		zip(srcDir, srcDir, zos);
		// Close the zip output stream
		zos.close();
	}
	
	// Zips a directory to a ZipOutputStream
	private static void zip(File srcDir, File originSrcDir,
			ZipOutputStream dstStream) throws IOException {
		// Create buffer
		byte[] buffer = new byte[ZIP_BUFFER_SIZE];
		int bytes = 0;
		// Get a listing of the directory content
		String[] dirList = srcDir.list();
		// Zip each file of the directory listing
		for (int i = 0; i < dirList.length; i++) {
			File file = new File(srcDir, dirList[i]);
			// If the file is a directory, call this
			// function again to add its content recursively
			if (file.isDirectory()) {
				zip(file, originSrcDir, dstStream);
				continue;
			}
			// If the file is not a directory
			// Create a FileInputStream on top of the file
			FileInputStream fis = new FileInputStream(file);
			// Create a new zip entry
			ZipEntry entry = new ZipEntry(TFileHandler.removeDirectoryPath(
					originSrcDir.getAbsolutePath(), file.getAbsolutePath()));
			// Place the zip entry in the ZipOutputStream object
			dstStream.putNextEntry(entry);
			// Write the content of the file to the ZipOutputStream
			while ((bytes = fis.read(buffer)) != -1)
				dstStream.write(buffer, 0, bytes);
			// Close the FileInputStream
			fis.close();
		}
	}

	// Unzips a file to a directory
	private static void unzip(File srcFile, File dstDir) throws IOException {
		// Create buffer
		byte[] buffer = new byte[ZIP_BUFFER_SIZE];
		int bytes;
		// Get zip file entries
		ZipFile zipFile = new ZipFile(srcFile);
		Enumeration entries = zipFile.entries();
		// For each entry
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry)entries.nextElement();
			// If the entry is a directory, create it
			if (entry.isDirectory()) {
				File newDirectory = new File(entry.getName());
				if (!newDirectory.exists())
					newDirectory.mkdirs();
				continue;
			}
			// If the entry is a file, get it
			File newFile = new File(dstDir, entry.getName().replace('\\', '/'));
			// Check if its directory exists and create it if necessary
			File newFileDir = newFile.getParentFile();
			if (!newFileDir.exists())
				newFileDir.mkdirs();
			// Create the input and output streams
			InputStream in = zipFile.getInputStream(entry);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					newFile));
			// Copy one into the other
			while ((bytes = in.read(buffer)) >= 0)
				out.write(buffer, 0, bytes);
			// Close input and output streams
			in.close();
			out.close();
		}
		// Close the zip file
		zipFile.close();
	}
}
