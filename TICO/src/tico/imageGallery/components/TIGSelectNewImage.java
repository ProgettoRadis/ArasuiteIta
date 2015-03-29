/*
 * File: TIGSelectNewImage.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Mar 12, 2008
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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Color;
import java.io.File;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import tico.components.TButton;
import tico.components.TImagePreview;
import tico.components.resources.ImageFilter;
import tico.components.resources.TFileUtils;
import tico.configuration.TLanguage;

/*
 * This class displays the panel that pops-up a file chooser to choose the image to insert
 * in the Data Base. 
 */
public class TIGSelectNewImage extends JPanel {

	// Preview icon preview label size constants
	private final static int ICON_LABEL_WIDTH = 100;

	private final static int ICON_LABEL_HEIGHT = 80;

	// Preview icon preview label margin
	private final static int ICON_LABEL_MARGIN = 3;

	// TImageChooser type option constants
	/**
	 * TImageChooser type value. Does not show any option.
	 */
	public final static int NO_OPTIONS_TYPE = 0;

	/**
	 * TImageChooser type value. Shows text above or below options.
	 */
	public final static int TEXT_POSITION_TYPE = 1;

	/**
	 * TImageChooser type value. Shows resize image options.
	 */
	public final static int RESIZE_STYLE_TYPE = 2;

	// Default initial TImageChooser type
	private final static int DEFAULT_TYPE = NO_OPTIONS_TYPE;

	// Default component border title
	private final static String DEFAULT_TITLE = TLanguage
			.getString("TImageChooser.TITLE");

	// Current selected image icon
	private ImageIcon icon;

	// Preview icon label
	private JLabel iconLabel;

	// Choose and clear buttons panel container
	private JPanel buttonPanel;

	// Clear icon button
	private TButton clearIconButton;

	// Open image chooser dialog button
	private TButton openChooserDialogButton;

	// Allows save the directory where you get the last image
	private static File defaultDirectory = null;
	
	public static String path = null;

	/**
	 * Creates a new <code>TImageChooser</code> with <i>NO_OPTIONS_TYPE</i>
	 * <code>type</code>.
	 */
	public TIGSelectNewImage() {
		this(DEFAULT_TITLE);
	}

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
	public TIGSelectNewImage(String title) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createIconLabel();
		createButtonPanel();
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

		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 1;
		c.gridy = 0;
		add(buttonPanel, c);

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

	// Create button panel who allow to choose or clean the image
	private void createButtonPanel() {
		buttonPanel = new JPanel();

		openChooserDialogButton = new TButton(TLanguage.getString("TImageChooser.BUTTON_SELECT"));
		openChooserDialogButton
				.addActionListener(new ChooseIconButtonListener());

		clearIconButton = new TButton(new AbstractAction(TLanguage.getString("TImageChooser.BUTTON_CLEAR")) {
			public void actionPerformed(ActionEvent e) {
				setIcon(null);
				path=null;
			}
		});
		
		buttonPanel.add(openChooserDialogButton);
		buttonPanel.add(clearIconButton);
	}
	
	public String returnImage(){
		return path;
	}

	/**
	 * Update all the components. Updates the preview icon label and enables or
	 * disables the buttons.
	 */
	
	public void updateComponents() {
		if (icon != null) {
			clearIconButton.setEnabled(true);

			ImageIcon thumbnail = icon;

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
			clearIconButton.setEnabled(false);

			iconLabel.setText(TLanguage.getString("TImageChooser.PREVIEW_EMPTY"));
			iconLabel.setIcon(null);
		}
	}

	/**
	 * Returns the selected <code>icon</code>.
	 * 
	 * @return The selected <code>icon</code>
	 */
	public ImageIcon getIcon() {
		return icon;
	}

	/**
	 * Set the <code>icon</code>.
	 * 
	 * @param icon
	 *            The <code>icon</code> to set
	 */
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
		updateComponents();
	}

	// Action listener for the openChooserDialogButton
	private class ChooseIconButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Open a JFileChooser
			JFileChooser fileChooser = new JFileChooser();
			// Customize JFileChooser
			fileChooser.setDialogTitle(TLanguage.getString("TImageChooser.CHOOSE_IMAGE"));
			fileChooser.setCurrentDirectory(defaultDirectory);
			fileChooser.addChoosableFileFilter(new ImageFilter());
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setAccessory(new TImagePreview(fileChooser));

			// Checks if the user has chosen a file
			int returnValue = fileChooser.showOpenDialog((Component)null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				// Get the chosen file
				File selectedFile = fileChooser.getSelectedFile();
				// Set its directory as next first JFileChooser directory
				defaultDirectory = selectedFile.getParentFile();
				

				try {
					ImageIcon newImageIcon;
					
					// Test if need to be loaded with JAI libraries (different
					// format than JPG, PNG and GIF)
					if (TFileUtils.isJAIRequired(selectedFile)) {
						// Load it with JAI class
						RenderedOp src = JAI.create("fileload", selectedFile
								.getAbsolutePath());
						BufferedImage bufferedImage = src.getAsBufferedImage();
						newImageIcon = new ImageIcon(bufferedImage,
								selectedFile.getAbsolutePath());
						path = selectedFile.getAbsolutePath();
					} else {
						// Create it as usual
						newImageIcon = new ImageIcon(selectedFile
								.getAbsolutePath(), selectedFile
								.getAbsolutePath());
						path = selectedFile.getAbsolutePath();
					}

					// Sets the new imageIcon to the TImageChooser
					setIcon(newImageIcon);
				} catch (Exception ex) {
					// If the import fails show an error message
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TImageChooser.CHOOSE_IMAGE_ERROR"),
							TLanguage.getString("ERROR")+"!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
}
