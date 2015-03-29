/*
 * File: TFontModelChooser.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Apr 28, 2006
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Component to choose a font with all the common properties.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFontModelChooser extends JPanel {
	// Default initial font face
	private static final String DEFAULT_FACE = "Default";
	// Default initial font style
	private static final int DEFAULT_STYLE = Font.PLAIN;
	// Default initial font color
	private static final Color DEFAULT_COLOR = Color.BLACK;
	// Default initial font size
	private static final int DEFAULT_SIZE = 12;
	
	// The font sample color
	private static final Color FONT_SAMPLE_FONT_COLOR = Color.BLACK;
	// The font sample text
	private static String SAMPLE_TEXT = TLanguage.getString("TFontModelChooser.SAMPLE_TEXT");

	// Sample panel min size
	private static int SAMPLE_MIN_HEIGHT = 50;
	private static int SAMPLE_MIN_WIDTH = 100;
	// Properties panel size
	private static int PROPERTIES_HEIGHT = 100;
	private static int PROPERTIES_WIDTH = 120;
	// TFontModelChooser size 
	private static int MIN_HEIGHT = 200;
	private static int MIN_WIDTH = 200;

	// The font color combo box
	private TColorComboBox faceColorComboBox;
	// The font size combo box
	private TFontSizeComboBox fontSizeComboBox;
	// The font face list
	private TFontFaceList fontFaceList;

	// The sample panel
	private JTextPane fontSamplePane;
	// The font properties panel
	private JPanel fontPropertiesPanel;
	
	// The style selection toggle buttons
	private JToggleButton fontBoldToggleButton;
	private JToggleButton fontItalicToggleButton;

	/**
	 * Creates a new <code>TFontModelChooser</code> with  <code>face</code>
	 * defaults to <i>default</i>, <code>color</code> to <i>black</i>,
	 * <code>size</code> to <i>12</i> and <code>style</code> to <i>plain</i>.
	 */
	public TFontModelChooser() {
		this(DEFAULT_FACE, DEFAULT_COLOR, DEFAULT_SIZE, DEFAULT_STYLE);
	}

	/**
	 * Creates a new <code>TFontModelChooser</code> with  the specified
	 * initial <code>face</code>, <code>color</code>, <code>size</code> and
	 * <code>style</code>.
	 * 
	 * @param face The specified initial <code>face</code>
	 * @param size The specified initial <code>color</code>
	 * @param style The specified initial <code>size</code>
	 * @param color The specified initial <code>style</code>
	 */
	public TFontModelChooser(String face, Color color, int size, int style) {
		super();

		setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), TLanguage.getString("TFontModelChooser.TITLE")));

		createFontFaceList();
		
		//if(TEditor.get_android_mode()) fontFaceList.setEnabled(false);
		
		createFontSample();
		createFontPropertiesPanel();

		placeComponents();

		addListeners();

		setFontFace(face);
		setFontSize(size);
		setFontColor(color);
		setFontStyle(style);
	}

	/**
	 * Returns the selected <code>font</code> made with all the other selected
	 * values.
	 * 
	 * @return The selected <code>font</code>
	 */
	public Font getSelectedFont() {
		return new Font(getFontFace(), getFontStyle(), getFontSize());
	}

	/**
	 * Returns the selected <code>face</code>.
	 * 
	 * @return The selected <code>face</code>
	 */
	public String getFontFace() {
		return fontFaceList.getFontFace();
	}

	/**
	 * Set the <code>face</code>.
	 * 
	 * @param face The <code>face</code> to set
	 */
	public void setFontFace(String face) {
		fontFaceList.setFontFace(face);
		updateFontSample();
	}

	/**
	 * Returns the selected <code>size</code>.
	 * 
	 * @return The selected <code>size</code>
	 */
	public int getFontSize() {
		return fontSizeComboBox.getFontSize();
	}

	/**
	 * Set the <code>size</code>.
	 * 
	 * @param size The <code>size</code> to set
	 */
	public void setFontSize(int size) {
		fontSizeComboBox.setFontSize(size);
		updateFontSample();
	}

	/**
	 * Returns the selected <code>style</code>.
	 * 
	 * @return The selected <code>style</code>
	 */
	public int getFontStyle() {
		int style = 0;

		if (fontBoldToggleButton.isSelected())
			style |= Font.BOLD;
		if (fontItalicToggleButton.isSelected())
			style |= Font.ITALIC;

		return style;
	}

	/**
	 * Set the <code>style</code>.
	 * 
	 * @param style The <code>style</code> to set
	 */
	public void setFontStyle(int style) {
		if ((style & Font.BOLD) != 0)
			fontBoldToggleButton.setSelected(true);
		else
			fontBoldToggleButton.setSelected(false);

		if ((style & Font.ITALIC) != 0)
			fontItalicToggleButton.setSelected(true);
		else
			fontItalicToggleButton.setSelected(false);

		updateFontSample();
	}

	/**
	 * Returns the selected <code>color</code>.
	 * 
	 * @return The selected <code>color</code>
	 */
	public Color getFontColor() {
		return faceColorComboBox.getColor();
	}

	/**
	 * Set the <code>color</code>.
	 * 
	 * @param color The <code>color</code> to set
	 */
	public void setFontColor(Color color) {
		if (color == null)
			color = DEFAULT_COLOR;

		faceColorComboBox.setColor(color);
		updateFontSample();
	}

	// Creates the fontFaceList
	private void createFontFaceList() {
		fontFaceList = new TFontFaceList();
	}

	// Creates the fontPropertiesPanel which contains the color, size and
	// style selection compnoents
	private void createFontPropertiesPanel() {
		fontPropertiesPanel = new JPanel();
		fontPropertiesPanel.setMinimumSize(new Dimension(PROPERTIES_WIDTH,
				PROPERTIES_HEIGHT));

		JLabel colorLabel = new JLabel(TLanguage.getString("TFontModelChooser.COLOR"));
		faceColorComboBox = new TColorComboBox();
		JLabel sizeLabel = new JLabel(TLanguage.getString("TFontModelChooser.SIZE"));
		fontSizeComboBox = new TFontSizeComboBox();

		JPanel fontTypesPanel = new JPanel();

		fontBoldToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-text-bold-32.png"));
		fontBoldToggleButton.setMargin(new Insets(2, 2, 2, 2));
		fontBoldToggleButton.setToolTipText(TLanguage.getString("TFontModelChooser.BOLD"));
		
		fontItalicToggleButton = new JToggleButton(TResourceManager
				.getImageIcon("format-text-italic-32.png"));
		fontItalicToggleButton.setMargin(new Insets(2, 2, 2, 2));
		fontItalicToggleButton.setToolTipText(TLanguage.getString("TFontModelChooser.ITALIC"));

		fontTypesPanel.add(fontBoldToggleButton);
		fontTypesPanel.add(fontItalicToggleButton);

		GridBagConstraints c = new GridBagConstraints();
		fontPropertiesPanel.setLayout(new GridBagLayout());

		colorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.weighty = 0.3;
		fontPropertiesPanel.add(colorLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 0);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.3;
		fontPropertiesPanel.add(faceColorComboBox, c);

		sizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 0);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.0;
		c.weighty = 0.3;
		fontPropertiesPanel.add(sizeLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 0);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0.3;
		fontPropertiesPanel.add(fontSizeComboBox, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 0);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 0.4;
		fontPropertiesPanel.add(fontTypesPanel, c);
	}

	// Creates the fontSamplePane
	private void createFontSample() {
		fontSamplePane = new JTextPane();

		fontSamplePane.setMinimumSize(new Dimension(SAMPLE_MIN_WIDTH,
				SAMPLE_MIN_HEIGHT));
		fontSamplePane.setPreferredSize(new Dimension(SAMPLE_MIN_WIDTH,
				SAMPLE_MIN_HEIGHT));
		fontSamplePane.setBorder(BorderFactory.createEtchedBorder());
		fontSamplePane.setEditable(false);
		fontSamplePane.setFocusable(false);
		fontSamplePane.setForeground(FONT_SAMPLE_FONT_COLOR);

		fontSamplePane.setText(SAMPLE_TEXT);
	}

	// Updates the font sample with the parameters of the other fontModelChooser
	// selected values
	private void updateFontSample() {
		fontSamplePane.setFont(new Font(getFontFace(), getFontStyle(),
				getFontSize()));
	}

	// Place the components in the panel
	private void placeComponents() {
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.5;
		add(fontFaceList, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 10);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.0;
		c.weighty = 0.5;
		add(fontPropertiesPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0.5;
		add(fontSamplePane, c);
	}

	// Add the corresponding listeners to all the fontModelChooser components
	private void addListeners() {
		EventListener listener = new UpdateFontSampleListener();
		fontFaceList.addListSelectionListener((ListSelectionListener)listener);
		faceColorComboBox.addActionListener((ActionListener)listener);
		fontSizeComboBox.addActionListener((ActionListener)listener);
		fontBoldToggleButton.addActionListener((ActionListener)listener);
		fontItalicToggleButton.addActionListener((ActionListener)listener);
	}

	// Update the font sample
	private class UpdateFontSampleListener implements ActionListener,
			ListSelectionListener {
		public void actionPerformed(ActionEvent e) {
			updateFontSample();
		}

		public void valueChanged(ListSelectionEvent e) {
			updateFontSample();
		}
	}
}
