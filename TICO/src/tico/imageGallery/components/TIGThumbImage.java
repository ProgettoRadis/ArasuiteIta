/*
 * File: TIGThumbImage.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Apr 15, 2008
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

package tico.imageGallery.components;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.io.File;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import tico.components.resources.TFileUtils;
import tico.configuration.TLanguage;

/*This class shows an image from its path*/

public class TIGThumbImage extends JLabel {
		
	private ImageIcon image;
	
	public TIGThumbImage(){
		this.setPreferredSize(new Dimension(125,125));
		this.setBorder(new LineBorder(Color.GREEN,2));
	}
	
	protected void updateBorder(Color color){
		this.setBorder(new LineBorder(color,2));
	}
	
	protected JLabel createImageLabel(String image_path){ 
		
		if (image_path != null){
			String path = "images" + File.separator + image_path.substring(0,1).toUpperCase() 
				+ File.separator + image_path; 
			File image = new File(path);
			setImageFile(image);
		}
		else{
			this.image = null;
			setIcon(null);
		}					
		return this;
	}
	
	private void setImageFile(File imageFile) {
		// Temporal image
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
				setText(TLanguage.getString("TIGThumbImageDialog.TEXT"));
				this.setIcon(null);
			}
			else
				setText("");
			this.setIcon(image);
		}		
	}
	
	/**
	 * Returns the selected <code>icon</code>.
	 * 
	 * @return The selected <code>icon</code>
	 */
	public ImageIcon returnIcon() {
		return image;
	}
	
	
	/**
	 * Returns the selected <code>String</code>.
	 * 
	 * @return The selected <code>String</code>
	 */
	public String returnText() {
		return getText();
	}
}