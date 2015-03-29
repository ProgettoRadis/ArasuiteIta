/*
 * File: TImagePreview.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 17, 2006
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

package tico.components;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import tico.components.resources.TFileUtils;

/**
 * Preview component to be shown ona a <code>JFileChooser</code>.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TImagePreview extends JLabel implements PropertyChangeListener {
	// Component size constants
	private final static int PREVIEW_WIDTH = 100;
	private final static int PREVIEW_HEIGHT = 100;
	// Component margin
	private final static int PREVIEW_MARGIN = 15;

	/**
	 * Creates a new <code>TImagePreview</code> to be shown on the specified
	 * <code>fileChooser</code>.
	 * 
	 * @param fileChooser The specified <code>fileChooser</code>
	 */
	public TImagePreview(JFileChooser fileChooser) {
		setPreferredSize(new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT));
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		
		fileChooser.addPropertyChangeListener(this);
	}

	// Sets the image
	private void setImageFile(File imageFile) {
		// Temporal image
		ImageIcon image = null;

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

			// Resize image is needed
			int maxPreviewHeight = PREVIEW_HEIGHT - PREVIEW_MARGIN;
			if (image.getIconHeight() > maxPreviewHeight)
				image = new ImageIcon(image.getImage().getScaledInstance(-1,
						maxPreviewHeight, Image.SCALE_SMOOTH));
			
			int maxPreviewWidth = PREVIEW_WIDTH - PREVIEW_MARGIN;
			if (image.getIconWidth() > maxPreviewWidth)
				image = new ImageIcon(image.getImage().getScaledInstance(
						maxPreviewWidth, -1, Image.SCALE_SMOOTH));
		}

		setIcon(image);
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		// If the directory changed, don't show an image.
		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop))
			setImageFile(null);
		// If a file became selected, get it
		else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop))
			setImageFile((File)e.getNewValue());
	}
}