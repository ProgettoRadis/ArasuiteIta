/*
 * File: TImageChooser.java
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

package tico.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import dialogs.AddImageFrame;

import tico.board.TBoardConstants;
import tico.components.resources.ImageFilter;
import tico.components.resources.TFileUtils;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.editor.TFileHandler;
import tico.imageGallery.TImageGalleryButton;


/**
 * Components to choose an image file.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TImageChooser extends JPanel {
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
	
	private JFileChooser fileChooser;

	// Preview icon label
	private JLabel iconLabel;

	// Panel which contains the properties toggle buttons
	private JPanel propertiesPanel;

	// Text position toggle buttons
	private JToggleButton topTextPositionToggleButton;

	private JToggleButton centerTextPositionToggleButton;

	private JToggleButton bottomTextPositionToggleButton;

	// Image resize toggle buttons
	private JToggleButton fitResizeStyleToggleButton;

	private JToggleButton scaleResizeStyleToggleButton;

	private JToggleButton centerResizeStyleToggleButton;

	// Choose and clear buttons panel container
	private JPanel buttonPanel;

	// Clear icon button
	private TButton clearIconButton;
	
	// Open Image Gallery button
	//private TButton openGalleryButton;
	private TImageGalleryButton galleryButton;
	// Open image chooser dialog button
	private TButton openChooserDialogButton;
	
	protected TEditor editor;

	// Allows save the directory where you get the last image
	private static File defaultDirectory = null;
	
	public static String path = null;
	
	//public static final File pluginsDir = new File("./plugins");
	
	//private PluginManager pluginManager;

	/**
	 * Creates a new <code>TImageChooser</code> with <i>NO_OPTIONS_TYPE</i>
	 * <code>type</code>.
	 */
	public TImageChooser(TEditor editor) {
		this(DEFAULT_TITLE, DEFAULT_TYPE, editor);
	}
	
	/**
	 * Creates a new <code>TImageChooser</code> with <i>NO_OPTIONS_TYPE</i>
	 * <code>type</code>.
	 */
	public TImageChooser() {
		this(DEFAULT_TITLE, DEFAULT_TYPE);
	}

	/**
	 * Creates a new <code>TImageChooser</code> with <i>NO_OPTIONS_TYPE</i>
	 * <code>type</code> and the specified <code>title</code>.
	 * 
	 * @param title
	 *            The specified <code>title</code>
	 */
	public TImageChooser(String title) {
		this(title, DEFAULT_TYPE);
	}
	
	/**
	 * Creates a new <code>TImageChooser</code> with <i>NO_OPTIONS_TYPE</i>
	 * <code>type</code> and the specified <code>title</code>.
	 * 
	 * @param title
	 *            The specified <code>title</code>
	 */
	public TImageChooser(String title, TEditor editor) {
		this(title, DEFAULT_TYPE, editor);
	}
	
	/**
	 * Creates a new <code>TImageChooser</code> with the specified
	 * <code>type</code>.
	 * 
	 * @param type
	 *            The specified <code>type</code>. The possible values are
	 *            <code>NO_OPTIONS_TYPE</code>, <code>TEXT_POSITION_TYPE or
	 * <code>RESIZE_STYLE_TYPE</code>
	 */
	public TImageChooser(int type, TEditor editor) {
		this(DEFAULT_TITLE, type, editor);
	}
	
	/**
	 * Creates a new <code>TImageChooser</code> with the specified
	 * <code>type</code>.
	 * 
	 * @param type
	 *            The specified <code>type</code>. The possible values are
	 *            <code>NO_OPTIONS_TYPE</code>, <code>TEXT_POSITION_TYPE or
	 * <code>RESIZE_STYLE_TYPE</code>
	 */
	public TImageChooser(int type) {
		this(DEFAULT_TITLE, type);
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
	public TImageChooser(String title, int type, TEditor editor) {
		super();
		this.editor = editor;
		
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createIconLabel();
		createButtonPanel();
		createPropertiesPanel(type);
		// Update the components
		updateComponents();

		// Places the components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		add(iconLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		add(propertiesPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		add(buttonPanel, c);

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
	public TImageChooser(String title, int type) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createIconLabel();
		createButtonPanel();
		createPropertiesPanel(type);
		// Update the components
		updateComponents();

		// Places the components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 0);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		add(iconLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 5, 0, 10);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		add(propertiesPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 10, 10);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
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

	// Create properties panel depending on the type
	private void createPropertiesPanel(int type) {
		propertiesPanel = new JPanel();

		ButtonGroup propertiesButtonGroup = new ButtonGroup();

		// Text position options
		topTextPositionToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-vertical-textposition-top-32.png"));
		topTextPositionToggleButton.setMargin(new Insets(2, 2, 2, 2));
		topTextPositionToggleButton.setToolTipText(TLanguage.getString("TImageChooser.TOP_TEXT_TOOLTIP"));
		
		centerTextPositionToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-vertical-textposition-center-32.png"));
		centerTextPositionToggleButton.setMargin(new Insets(2, 2, 2, 2));
		centerTextPositionToggleButton.setToolTipText(TLanguage.getString("TImageChooser.CENTER_TEXT_TOOLTIP"));
		
		bottomTextPositionToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-vertical-textposition-bottom-32.png"));
		bottomTextPositionToggleButton.setMargin(new Insets(2, 2, 2, 2));
		bottomTextPositionToggleButton.setToolTipText(TLanguage.getString("TImageChooser.BOTTOM_TEXT_TOOLTIP"));

		topTextPositionToggleButton.setEnabled(false);
		centerTextPositionToggleButton.setEnabled(false);
		bottomTextPositionToggleButton.setEnabled(false);
		
		// Resize image options
		centerResizeStyleToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-resize-style-center-32.png"));
		centerResizeStyleToggleButton.setMargin(new Insets(2, 2, 2, 2));
		centerResizeStyleToggleButton.setToolTipText(TLanguage.getString("TImageChooser.CENTER_RESIZE_TOOLTIP"));

		scaleResizeStyleToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-resize-style-scale-32.png"));
		scaleResizeStyleToggleButton.setMargin(new Insets(2, 2, 2, 2));
		scaleResizeStyleToggleButton.setToolTipText(TLanguage.getString("TImageChooser.SCALE_RESIZE_TOOLTIP"));

		fitResizeStyleToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-resize-style-fit-32.png"));
		fitResizeStyleToggleButton.setMargin(new Insets(2, 2, 2, 2));
		fitResizeStyleToggleButton.setToolTipText(TLanguage.getString("TImageChooser.FIT_RESIZE_TOOLTIP"));

		fitResizeStyleToggleButton.setEnabled(false);
		scaleResizeStyleToggleButton.setEnabled(false);
		centerResizeStyleToggleButton.setEnabled(false);

		// Depending on the time add one button set or the other
		switch (type) {
		case TEXT_POSITION_TYPE:
			propertiesButtonGroup.add(topTextPositionToggleButton);
			propertiesButtonGroup.add(centerTextPositionToggleButton);
			propertiesButtonGroup.add(bottomTextPositionToggleButton);

			propertiesPanel.add(topTextPositionToggleButton);
			propertiesPanel.add(centerTextPositionToggleButton);
			propertiesPanel.add(bottomTextPositionToggleButton);
			break;
		case RESIZE_STYLE_TYPE:
			propertiesButtonGroup.add(centerResizeStyleToggleButton);
			propertiesButtonGroup.add(scaleResizeStyleToggleButton);
			propertiesButtonGroup.add(fitResizeStyleToggleButton);

			propertiesPanel.add(centerResizeStyleToggleButton);
			propertiesPanel.add(scaleResizeStyleToggleButton);
			propertiesPanel.add(fitResizeStyleToggleButton);
			break;
		}
	}

	// Create button panel who allow to choose or clean the image
	private void createButtonPanel() {
		
		buttonPanel = new JPanel();
		galleryButton = new TImageGalleryButton();
		buttonPanel.add(galleryButton.createImageGalleryButton(editor,this));
		
		openChooserDialogButton = new TButton(TLanguage.getString("TImageChooser.BUTTON_SELECT"));
		openChooserDialogButton
				.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AddImageFrame f = new AddImageFrame();
                        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                        f.setResizable(false);
                        f.pack();
                        f.setModal(true);

                        f.setLocationRelativeTo(editor);
                        f.setVisible(true);
                    }
                });

		clearIconButton = new TButton(new AbstractAction(TLanguage.getString("TImageChooser.BUTTON_CLEAR")) {
			public void actionPerformed(ActionEvent e) {
				setIcon(null);
				path=null;
			}
		});
		
		/*openGalleryButton = new TButton(new AbstractAction(TLanguage.getString("TImageChooser.BUTTON_OPEN")) {
			public void actionPerformed(ActionEvent e) {
				TIGImageGalleryDialog imageGallery = new TIGImageGalleryDialog(editor);
				setIcon(imageGallery.getIcon());	
			}
		});

		buttonPanel.add(openGalleryButton);*/
		buttonPanel.add(openChooserDialogButton);
		buttonPanel.add(clearIconButton);
	}

	/**
	 * Update all the components. Updates the preview icon label and enables or
	 * disables the buttons.
	 */
	
	public void updateComponents() {
		if (icon != null) {
			clearIconButton.setEnabled(true);
			topTextPositionToggleButton.setEnabled(true);
			centerTextPositionToggleButton.setEnabled(true);
			bottomTextPositionToggleButton.setEnabled(true);
			fitResizeStyleToggleButton.setEnabled(true);
			scaleResizeStyleToggleButton.setEnabled(true);
			centerResizeStyleToggleButton.setEnabled(true);

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
			topTextPositionToggleButton.setEnabled(false);
			centerTextPositionToggleButton.setEnabled(false);
			bottomTextPositionToggleButton.setEnabled(false);
			fitResizeStyleToggleButton.setEnabled(false);
			scaleResizeStyleToggleButton.setEnabled(false);
			centerResizeStyleToggleButton.setEnabled(false);

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

	/**
	 * Returns the selected <code>verticalTextPosition</code>.
	 * 
	 * @return The selected <code>verticalTextPosition</code>. Must be one of
	 *         the following constants defined in <code>SwingConstants</code>:
	 *         TOP, CENTER or BOTTOM.
	 * 
	 * @see SwingConstants
	 */
	public int getVerticalTextPosition() {
		if (bottomTextPositionToggleButton.isSelected())
			return SwingConstants.BOTTOM;
		else if (centerTextPositionToggleButton.isSelected())
			return SwingConstants.CENTER;
		else
			return SwingConstants.TOP;
	}

	/**
	 * Set the <code>verticalTextPosition</code>.
	 * 
	 * @param verticalTextPosition
	 *            The <code>verticalTextPosition</code> to set. Must be one of
	 *            the following constants defined in <code>SwingConstants</code>:
	 *            TOP, CENTER or BOTTOM.
	 * 
	 * @see SwingConstants
	 */
	public void setVerticalTextPosition(int verticalTextPosition) {
		if (verticalTextPosition == SwingConstants.BOTTOM)
			bottomTextPositionToggleButton.setSelected(true);
		else if (verticalTextPosition == SwingConstants.CENTER)
			centerTextPositionToggleButton.setSelected(true);
		else
			topTextPositionToggleButton.setSelected(true);
	}

	/**
	 * Returns the selected <code>resizeStyle</code>.
	 * 
	 * @return The selected <code>resizeStyle</code>. Must be one of the
	 *         following constants defined in <code>TBoardConstants</code>:
	 *         IMAGE_CENTER, IMAGE_FIT or IMAGE_SCALE.
	 * 
	 * @see TBoardConstants
	 */
	public int getResizeStyle() {
		if (centerResizeStyleToggleButton.isSelected())
			return TBoardConstants.IMAGE_CENTER;
		else if (fitResizeStyleToggleButton.isSelected())
			return TBoardConstants.IMAGE_FIT;
		else
			return TBoardConstants.IMAGE_SCALE;
	}

	/**
	 * Set the <code>resizeStyle</code>.
	 * 
	 * @param resizeStyle
	 *            The <code>resizeStyle</code> to set. Must be one of the
	 *            following constants defined in <code>TBoardConstants</code>:
	 *            IMAGE_CENTER, IMAGE_FIT or IMAGE_SCALE.
	 * 
	 * @see TBoardConstants
	 */
	public void setResizeStyle(int resizeStyle) {
		if (resizeStyle == TBoardConstants.IMAGE_CENTER)
			centerResizeStyleToggleButton.setSelected(true);
		else if (resizeStyle == TBoardConstants.IMAGE_FIT)
			fitResizeStyleToggleButton.setSelected(true);
		else
			scaleResizeStyleToggleButton.setSelected(true);
	}
	
	public TImageGalleryButton getImageGalleryButton(){
		return galleryButton;
	}

	// Action listener for the openChooserDialogButton
	private class ChooseIconButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Open a JFileChooser
			fileChooser = new JFileChooser();
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
					// Import the file to the application directory
					selectedFile = TFileHandler.importFile(selectedFile);
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
