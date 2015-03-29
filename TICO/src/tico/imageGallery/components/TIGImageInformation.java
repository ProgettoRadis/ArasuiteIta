/*
 * File: TIGImageInformation.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Mar 31, 2008
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import tico.configuration.TLanguage;

/*
 * This class displays the panel that contains the image and its name
 */

public class TIGImageInformation extends JPanel {

	// Preview icon preview label size constants
	private final static int ICON_LABEL_WIDTH = 100;

	private final static int ICON_LABEL_HEIGHT = 80;

	// Preview icon preview label margin
	private final static int ICON_LABEL_MARGIN = 3;

	// Preview icon label
	private JLabel iconLabel;
	
	private ImageIcon myIcon;
	
	private JTextField imageName;

	/**
	 * Creates a new <code>TImageChooser</code> with the specified
	 * <code>type</code> and <code>title</code>.
	 * 
	 * @param title
	 *            The specified <code>title</code>
	 * @param type
	 *            The specified <code>type</code>. The possible values are
	 *            <code>NO_OPTIONS_TYPE</code>, <code>TEXT_POSITION_TYPE or
	 * <code>RESIZE_STYLE_TYPE</code>
	 */
	public TIGImageInformation(String title,ImageIcon icon,String name) {
		super();
		
		myIcon = icon;
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createIconLabel();
		createNamePanel(name);
		// Update the components
		updateComponents();

		// Places the components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		add(iconLabel, c);

		c.fill = GridBagConstraints.NORTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 1;
		c.gridy = 0;
		add(imageName, c);

	}

	// Create preview icon label
	private void createIconLabel() {
		iconLabel = new JLabel();

		iconLabel.setMinimumSize(new Dimension(ICON_LABEL_WIDTH,
				ICON_LABEL_HEIGHT));
		iconLabel.setPreferredSize(new Dimension(ICON_LABEL_WIDTH,
				ICON_LABEL_HEIGHT));

		iconLabel.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		iconLabel.setVerticalAlignment(SwingConstants.CENTER);
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setBackground(Color.WHITE);
	}

	// Create the panel with the image and its name
	private void createNamePanel(String name) {
		
		imageName = new JTextField(name);
		//Disable the renaming of the image
		imageName.setEditable(false);
		imageName.setPreferredSize(new Dimension (180, 25));
	}

	/**
	 * Update all the components. Updates the preview icon label and enables or
	 * disables the buttons.
	 */
	
	protected void updateComponents() {
		if (myIcon != null) {
			ImageIcon thumbnail = myIcon;
			int maxImageWidth = iconLabel.getPreferredSize().width
					- ICON_LABEL_MARGIN;
			int maxImageHeight = iconLabel.getPreferredSize().height
					- ICON_LABEL_MARGIN;

			if (thumbnail.getIconWidth() > maxImageWidth) {
				thumbnail = new ImageIcon(thumbnail.getImage()
						.getScaledInstance(maxImageWidth, -1,
								Image.SCALE_SMOOTH));
			}
			if (thumbnail.getIconHeight() > maxImageHeight) {
				thumbnail = new ImageIcon(thumbnail.getImage()
						.getScaledInstance(-1, maxImageHeight,
								Image.SCALE_SMOOTH));
			}
			iconLabel.setText("");
			iconLabel.setIcon(thumbnail);
		} else {
			iconLabel.setText(TLanguage.getString("TIGModifyImageNameDialog.PREVIEW_EMPTY"));
			iconLabel.setIcon(null);
		}
	}
	
	public String returnName(){
		return imageName.getText();
	}
}
