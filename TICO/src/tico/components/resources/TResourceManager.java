/*
 * File: ImageResource.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Sep 6, 2006
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

package tico.components.resources;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Static class that contains attributes and methods to manage application
 * internal image files.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TResourceManager {
	/**
	 * Returns the <code>imageIcon</code> generated with the specified file.
	 * The file name is specified by its <code>name</code> and the directory
	 * <i>images/</i> relative to this class.
	 * 
	 * @param name The specified file <code>name</code>
	 * @return The generated <code>imageIcon</code>
	 */
	public final static ImageIcon getImageIcon(String name) {
		java.net.URL imageURL = TResourceManager.class.getResource("images/"
				+ name);

		if (imageURL != null) {
			return new ImageIcon(imageURL);
		}

		return null;
	}
	
	/**
	 * Returns the <code>image</code> generated with the specified file.
	 * The file name is specified by its <code>name</code> and the directory
	 * <i>images/</i> relative to this class.
	 * 
	 * @param name The specified file <code>name</code>
	 * @return The generated <code>image</code>
	 */
	public final static Image getImage(String name) {
		java.net.URL imageURL = TResourceManager.class.getResource("images/"
				+ name);

		if (imageURL != null) {
			try {
				return ImageIO.read(imageURL);
			} catch (IOException e) {
				return null;
			}
		}

		return null;
	}

}
