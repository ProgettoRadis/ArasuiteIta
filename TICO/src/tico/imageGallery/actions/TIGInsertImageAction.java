/*
 * File: TIGInsertImageAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Apr 2, 2008
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

package tico.imageGallery.actions;

import java.awt.event.ActionEvent;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.sun.image.codec.jpeg.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;



import tico.components.resources.TFileUtils;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

/**
 * Action which exists from the editor application.
 * 
 * @author Patricia M. Jaray
 * @version 2.2 Apr 2, 2008
 */
public class TIGInsertImageAction extends TIGAbstractAction {
	
	private final static int PREVIEW_WIDTH = 125;
	private final static int PREVIEW_HEIGHT = 125;
	
	private ImageIcon image;
	
	protected TIGDataBase dataBase;
	
	protected Vector theConcepts;
	
	protected String path;
	
	protected String directoryPath;
	
	protected String imageName;
	
	protected String imagePath;
	
	protected String imagePathThumb;
	
	protected String name;
	
	protected String myImagesBehaviour;
	
	/**
	 * Constructor for TEditorExitAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TIGInsertImageAction(TEditor editor, Vector concepts, String image, TIGDataBase dataBase, String behaviour) {
		super(editor);
		theConcepts = (Vector) concepts.clone();
		path = image;
		myImagesBehaviour = behaviour;
		this.dataBase = dataBase;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (path.compareTo("") != 0){
		
			imageName = (path.substring(path.lastIndexOf(File.separator) + 1, path.length()));
			String name = imageName.substring(0,imageName.lastIndexOf('.'));
			String extension = imageName.substring(imageName.lastIndexOf('.')+1, imageName.length());
			
			File imageFile = new File(path);
			directoryPath = "images" + File.separator + imageName.substring(0,1).toUpperCase();
			File directory = new File(directoryPath);
			directory.mkdirs(); 
		
			imagePath = "." + File.separator + "images" + File.separator + imageName.substring(0,1).toUpperCase() + File.separator + imageName;
			
			File newFile = new File(imagePath);
			
			if (myImagesBehaviour.equals(TLanguage.getString("TIGManageGalleryDialog.REPLACE_IMAGE"))){
				//Replace
				Vector<Vector<String>> aux = TIGDataBase.imageSearchByName(name);
				if (aux.size()!=0){
					int idImage = TIGDataBase.imageKeySearchName(name);
				    TIGDataBase.deleteAsociatedOfImage(idImage);
				}
	       	 }
			 if (myImagesBehaviour.equals(TLanguage.getString("TIGManageGalleryDialog.ADD_IMAGE"))){
				 int i = 1;
				 while (newFile.exists()){
					 imagePath = "." + File.separator + "images" + File.separator + imageName.substring(0,1).toUpperCase() + File.separator + imageName.substring(0,imageName.lastIndexOf('.')) + "_" + i + imageName.substring(imageName.lastIndexOf('.'),imageName.length());
					 name = name + "_" + i;
					 newFile = new File(imagePath);
					 i++;
				 }					 
			 }
			 
			 imagePathThumb = (imagePath.substring(0,imagePath.lastIndexOf("."))).concat("_th.jpg");
			 imageName = name +  "." + extension;
			 //Copy the image
			 try {
					// Create channel on the source
					FileChannel srcChannel = new FileInputStream(path).getChannel();
		    
					// Create channel on the destination
					FileChannel dstChannel = new FileOutputStream(imagePath).getChannel();
		    
					// Copy file contents from source to destination
					dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
		    
					// Close the channels
		        		srcChannel.close();
		        		dstChannel.close();
				} catch (IOException exc) {
		    			System.out.println(exc.getMessage());
		    			System.out.println(exc.toString());    		
				}
				
			 //Insert image into database
			 TIGDataBase.insertDB(theConcepts, imageName, imageName.substring(0,imageName.lastIndexOf('.')));
			 
			 image = null;

			if (imageFile != null) {
				// Test if need to be loaded with JAI libraries (different
				// format than JPG, PNG and GIF)
				if (TFileUtils.isJAIRequired(imageFile)) {
					// Load it with JAI class
					RenderedOp src = JAI.create("fileload", imageFile
							.getAbsolutePath());
					BufferedImage bufferedImage = src.getAsBufferedImage();
					image = new ImageIcon(bufferedImage);				
				} else {
					// Create it as usual
					image = new ImageIcon(imageFile.getAbsolutePath());
				}

				if (image.getImageLoadStatus() == MediaTracker.ERRORED){
					int choosenOption = JOptionPane.NO_OPTION;
					// Ask the user if he is sure to delete the keyword
					//(Component parentComponent, Object message, String title, int optionType, int messageType) 
					choosenOption = JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGInsertImageAction.MESSAGE"),
							TLanguage.getString("TIGInsertImageAction.NAME"),
							JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
				}
				else{	
					createThumbnail();
				}
			}
			
			//Check if there is an image with the same name. If it exists, add something at the end
			/*newFile = new File(imagePath);
			int i = 1;
			
			while (newFile.exists()){
				imagePath = "." + File.separator + "images" + File.separator + imageName.substring(0,1).toUpperCase() + File.separator + imageName.substring(0,imageName.lastIndexOf('.')) + "_" + i + imageName.substring(imageName.lastIndexOf('.'),imageName.length());
				name = name + "_" + i;
				newFile = new File(imagePath);
				i++;
			}

			imagePathThumb = (imagePath.substring(0,imagePath.lastIndexOf("."))).concat("_th.jpg");
			
			imageName = name + "." + extension;
			
			TIGDataBase.insertDB(theConcepts, imageName, imageName.substring(0,imageName.lastIndexOf('.')));*/
		
			/*try {
				// Create channel on the source
				FileChannel srcChannel = new FileInputStream(path).getChannel();
	    
				// Create channel on the destination
				FileChannel dstChannel = new FileOutputStream(imagePath).getChannel();
	    
				// Copy file contents from source to destination
				dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
	    
				// Close the channels
	        		srcChannel.close();
	        		dstChannel.close();
			} catch (IOException exc) {
	    			System.out.println(exc.getMessage());
	    			System.out.println(exc.toString());    		
			}*/
	    
			//image = null;

		/*	if (imageFile != null) {
				// Test if need to be loaded with JAI libraries (different
				// format than JPG, PNG and GIF)
				if (TFileUtils.isJAIRequired(imageFile)) {
					// Load it with JAI class
					RenderedOp src = JAI.create("fileload", imageFile
							.getAbsolutePath());
					BufferedImage bufferedImage = src.getAsBufferedImage();
					image = new ImageIcon(bufferedImage);				
				} else {
					// Create it as usual
					image = new ImageIcon(imageFile.getAbsolutePath());
				}

				if (image.getImageLoadStatus() == MediaTracker.ERRORED){
					int choosenOption = JOptionPane.NO_OPTION;
					// Ask the user if he is sure to delete the keyword
					//(Component parentComponent, Object message, String title, int optionType, int messageType) 
					choosenOption = JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGInsertImageAction.MESSAGE"),
							TLanguage.getString("TIGInsertImageAction.NAME"),
							JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
				}
				else{	
							
					createThumbnail();
				
				}//else
			}//if*/
		}//if
	    	    
	}//End actionPerformed
	
	private void createThumbnail(){
		try{
			int thumbWidth = PREVIEW_WIDTH;
			int thumbHeight = PREVIEW_HEIGHT;
			double thumbRatio = (double)thumbWidth / (double)thumbHeight;
			int imageWidth = image.getIconWidth();
			int imageHeight = image.getIconHeight();
			double imageRatio = (double)imageWidth / (double)imageHeight;
			if (thumbRatio < imageRatio) {
				thumbHeight = (int)(thumbWidth / imageRatio);
			} else {
				thumbWidth = (int)(thumbHeight * imageRatio);
			}
			BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = thumbImage.createGraphics();
	    			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    			graphics2D.drawImage(image.getImage(), 0, 0, thumbWidth, thumbHeight, null);
	    			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(imagePathThumb));
	    			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
	    			int quality = 100;
	    			quality = Math.max(0, Math.min(quality, 100));
	    			param.setQuality(quality / 100.0f, false);
	    			encoder.setJPEGEncodeParam(param);
	    			encoder.encode(thumbImage);
	    			out.close(); 
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println(ex.toString());   
		}	    
	}
	
	
}

