/*
 * File: TFileHandler.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import tico.components.resources.TFileUtils;

/**
 * Static class that manages the current editing project internal files.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFileHandler {
	private final static String CURRENT_BASE_DIRECTORY_PATH = "current";

	private final static File CURRENT_BASE_DIRECTORY = new File(
			CURRENT_BASE_DIRECTORY_PATH);
	
	private final static File DEFAULT_IMPORT_DIR = new File("templates");

	private static String currentDirectoryPath;

	private static File currentDirectory;

	static {
		if (!CURRENT_BASE_DIRECTORY.exists())
			CURRENT_BASE_DIRECTORY.mkdirs();

		int i = 0;
		currentDirectory = new File(CURRENT_BASE_DIRECTORY_PATH
				+ File.separator + i++);
		while (currentDirectory.exists())
			currentDirectory = new File(CURRENT_BASE_DIRECTORY_PATH
					+ File.separator + i++);
		currentDirectoryPath = currentDirectory.getAbsolutePath();
		currentDirectory.mkdirs();
	}

	/**
	 * Imports the specified <code>file</code> to the internal application
	 * structure.
	 * 
	 * @param fileAbsolutePath The specified <code>file</code> fileAbsolutePath
	 * @return The new internal file
	 * @throws IOException If there is any problem importing the file
	 */
	public static File importFile(String fileAbsolutePath) throws IOException {
		File file = new File(fileAbsolutePath);

		return TFileHandler.importFile(file);
	}

	/**
	 * Imports the specified <code>file</code> to the internal application
	 * structure.
	 * 
	 * @param file The specified <code>file</code>
	 * @return The new internal file
	 * @throws IOException If there is any problem importing the file
	 */
	
	public static File importFile(File file) throws IOException {
		
		String directoryPath = currentDirectoryPath;
		
		if (TFileUtils.isImageFile(file))
		{
			directoryPath += File.separator + "image";}
		
		if (TFileUtils.isSoundFile(file)){
			directoryPath += File.separator + "sound";
			}
		
		if (TFileUtils.isVideoFile(file)){
			directoryPath += File.separator + "video";
			}

		File directory = new File(directoryPath);

		if (!directory.exists())
			directory.mkdirs();

		return TFileHandler.importFile(file, directory);
	}

	/**
	 * Removes the specified <code>file</code> from the internal application
	 * structure.
	 * 
	 * @param fileAbsolutePath The specified <code>file</code> fileAbsolutePath
	 */
	public static void remove(String fileAbsolutePath) {
		File file = new File(fileAbsolutePath);

		TFileHandler.remove(file);
	}

	// Import an external file to an internal file
	private static File importFile(File srcFile, File dstDir)
			throws IOException {
		String baseFilename;
		File newFile;
		
		String name = getFilename(srcFile.getName());
		String extension = getExtension(srcFile.getName());
		String nameRenamed = replace(name);
		if (!nameRenamed.equals(name)){
			newFile = new File(dstDir,nameRenamed+"."+extension);
			baseFilename = nameRenamed+"."+extension;
		}else{
			newFile = new File(dstDir, srcFile.getName());
			baseFilename = srcFile.getName();
		}
		if ((!dstDir.exists()) || (!dstDir.canWrite()))
			throw new IOException("Invalid destination directory");
		if ((!srcFile.exists()) || (srcFile.isDirectory()))
			throw new IOException("Invalid input file");
		if (!srcFile.canRead())
			throw new IOException("Input file can't be read");

		int fileCount = 1;
		while (newFile.exists())
			newFile = new File(dstDir, TFileUtils.getFilename(baseFilename)
					+ "_" + (fileCount++) + "."
					+ TFileUtils.getExtension(baseFilename));

		newFile.createNewFile();
		copyFile(srcFile, newFile);

		return newFile;
	}

	// Remove a file
	private static void remove(File file) {
		file.delete();
	}
	
	/**
	 * Returns the editor internal <code>currentDirectory</code>.
	 * 
	 * @return The editor internal <code>currentDirectory</code>
	 */
	public static File getCurrentDirectory() {
		return currentDirectory;
	}
	
	/**
	 * Returns the editor internal <code>currentDirectoryPath</code>.
	 * 
	 * @return The editor internal <code>currentDirectoryPath</code>
	 */
	public static String getCurrentDirectoryPath() {
		return currentDirectoryPath;
	}

	/**
	 * Converts the specified internal <code>file</code> absolute path to a
	 * current directory partial path.
	 * 
	 * @param file The specified internal <code>file</code>
	 * @return The <code>file</code> partial path
	 */
	public static String convertToPartial(File file) {
		return convertToPartial(file.getAbsolutePath());
	}
	
	/**
	 * Converts the specified internal <code>file</code> absolute path to a
	 * current directory partial path.
	 * 
	 * @param path The specified internal <code>file</code> path
	 * @return The <code>file</code> partial path
	 */
	public static String convertToPartial(String path) {
		return removeDirectoryPath(currentDirectoryPath, path);
	}

	/**
	 * Removes the specified <code>directory</code> from the specified
	 * <code>path</code>.
	 * 
	 * @param directory The specified <code>directory</code>
	 * @param path The specified <code>path</code>
	 * @return Specified 
	 */
	public static String removeDirectoryPath(String directory, String path) {
		if (path.substring(0, directory.length()).equals(directory)) {
			return path.substring(directory.length() + 1);
		}

		return null;
	}

	/**
	 * Converts the specified internal <code>file</code> partial path to
	 * an absolute path adding the current directory path.
	 * 
	 * @param path The specified internal <code>file</code> partial path
	 * @return The <code>file</code> absolute path
	 */
	public static String convertToAbsolute(String path) {
		return currentDirectoryPath + File.separator + path;
	}

	/**
	 * Deletes the current directory and all its contents.
	 */
	public static void deleteCurrentDirectory() {
		deleteDirectory(currentDirectory);
		currentDirectory.delete();
	}

	/**
	 * Deletes all the current directory contents.
	 */
	public static void cleanCurrentDirectory() {
		deleteDirectory(currentDirectory);
	}

	/**
	 * Deletes the specified <code>directory</code> and all its contents.
	 * 
	 * @param directory The specified <code>directory</code>
	 */
	public static void deleteDirectory(File directory) {
		File[] files = directory.listFiles();
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory())
					deleteDirectory(files[i]);
				files[i].delete();
			}
	}

	/**
	 * Copies the content of an <code>in</code> file to an
	 * <code>out</code> file.
	 * 
	 * @param in The <code>in</code> file
	 * @param out The <code>out</code> file
	 * @throws IOException If there is a problem with any of both files
	 */
	public static void copyFile(File in, File out) throws IOException {
		FileChannel sourceChannel = new FileInputStream(in).getChannel();
		FileChannel destinationChannel = new FileOutputStream(out).getChannel();

		destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

		sourceChannel.close();
		destinationChannel.close();
	}

	private static String replace(String word){
		String result = word.replace(' ', '_').replace(',', '-').replace('�', 'a').replace('�', 'e').replace('�', 'i').replace('�', 'o').replace('�', 'u').
		replace('�', 'A').replace('�', 'E').replace('�', 'I').replace('�', 'O').replace('�', 'U').replace("�", "ny").replace("�", "NY").toLowerCase();
		return result;
	}
	
	public static String getFilename(String filePath) {
		String fileName = null;
		
		int ini = -1;
		int end = filePath.lastIndexOf('.');

		if ((ini < end) && (ini >= -1 && ini < filePath.length() - 1)
				&& (end >= 0 && end < filePath.length()))
			fileName = filePath.substring(ini + 1, end);
		
		return fileName;
	}
	
	public static String getExtension(String filePath) {
		String extension = null;
		int i = filePath.lastIndexOf('.');

		if (i > 0 && i < filePath.length() - 1)
			extension = filePath.substring(i + 1).toLowerCase();

		return extension;
	}
	
	
	/**
	 * Returns the default import/export dir.
	 * 
	 * @return The default import/export dir
	 */
	public static File getDefaultImportDirectory() {
		return DEFAULT_IMPORT_DIR;
	}
}
