/*
 * File: TBackgroundSelectionPanel.java
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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Component to choose a background and gradient colors.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBackgroundSelectionPanel extends JPanel {
	// Default initial background color parameter
	private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	// Default initial gradient color parameter
	private static final Color DEFAULT_GRADIENT_COLOR = null;
	// Default initial allow transparency parameter
	private static final boolean DEFAULT_ALLOW_TRANSPARENT = true;
	
	// Background color combo box chooser
	private TColorComboBox backgroundColorComboBox;
	// Gradient color combo box chooser
	private TColorComboBox gradientColorComboBox;

	/**
	 * Creates a new <code>TBackgroundSelectionPanel</code>;
	 * <code>backgroundColor</code> defaults to <i>white</i>, no
	 * <code>gradientColor</code> and <code>allowinTransparent</code>
	 * to <i>true</i>.
	 */
	public TBackgroundSelectionPanel() {
		this(DEFAULT_BACKGROUND_COLOR);
	}

	/**
	 * Creates a new <code>TBackgroundSelectionPanel</code> with the specified
	 * initial <code>backgroundColor</code>, no gradient color and allowing
	 * transparent background.
	 *
	 * @param backgroundColor The specified initial <code>backgroundColor</code>
	 */
	public TBackgroundSelectionPanel(Color backgroundColor) {
		this(backgroundColor, DEFAULT_GRADIENT_COLOR);
	}

	/**
	 * Create a new <code>TBackgroundSelectionPanel</code> with the specified
	 * initial <code>backgroundColor</code> and <code>gradientColor</code>,
	 * allowing transparent background.
	 * 
	 * @param backgroundColor The specified initial <code>backgroundColor</code>
	 * @param gradientColor The specified initial <code>gradientColor</code>
	 */
	public TBackgroundSelectionPanel(Color backgroundColor, Color gradientColor) {
		this(backgroundColor, gradientColor, DEFAULT_ALLOW_TRANSPARENT);
	}

	/**
	 * Create a new <code>TBackgroundSelectionPanel</code> with background color
	 * defaults to <i>white</i>, no gradient color and with the specified
	 * <code>allowTransparent</code> value.
	 * 
	 * @param allowTransparent The specified initial <code>allowTransparent</code> value
	 */
	public TBackgroundSelectionPanel(boolean allowTransparent) {
		this(DEFAULT_BACKGROUND_COLOR, DEFAULT_GRADIENT_COLOR, allowTransparent);
	}

	/**
	 * Create a new <code>TBackgroundSelectionPanel</code> with the specified
	 * initial <code>backgroundColor</code>, <code>gradientColor</code> and
	 * <code>allowTransparent</code> value.
	 * 
	 * @param backgroundColor The specified initial <code>backgroundColor</code>
	 * @param gradientColor The specified initial <code>gradientColor</code>
	 * @param allowTransparent The specified initial <code>allowTransparent</code> value
	 */
	public TBackgroundSelectionPanel(Color backgroundColor, Color gradientColor,
			boolean allowTransparent) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), TLanguage.getString("TBackgroundSelectionPanel.TITLE")));
		// Creates the components
		JLabel backgroundColorLabel = new JLabel(TLanguage.getString("TBackgroundSelectionPanel.COLOR"));
		backgroundColorComboBox = new TColorComboBox(allowTransparent);
		JLabel gradientColorLabel = new JLabel(TLanguage.getString("TBackgroundSelectionPanel.GRADIENT"));
		// Gradient color always allows transparent value
		gradientColorComboBox = new TColorComboBox(true);
		
		// Place the components in the panel
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		// Place backgroundColorLabel
		backgroundColorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		add(backgroundColorLabel, c);
		// Place backgroundColorComboBox
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 0);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		add(backgroundColorComboBox, c);
		// Place gradientColorLabel
		gradientColorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 20, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 2;
		c.gridy = 0;
		add(gradientColorLabel, c);
		// Place gradientColorComboBox
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 10);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 3;
		c.gridy = 0;
		add(gradientColorComboBox, c);
		// Add change color listener
		backgroundColorComboBox.addActionListener(new ChangeColorListener());
		// Set initial values
		setBackgroundColor(backgroundColor);
		setGradientColor(gradientColor);
		
		
		if(TEditor.get_android_mode()) gradientColorComboBox.setEnabled(false);
	}

	/**
	 * Returns the selected <code>backgrodundColor</code>.
	 * 
	 * @return The selected <code>backgrodundColor</code>
	 */
	public Color getBackgroundColor() {
		return backgroundColorComboBox.getColor();
	}

	/**
	 * Set the <code>backgrodundColor</code>. 
	 * 
	 * @param backgrodundColor The <code>backgrodundColor</code> to set
	 */
	public void setBackgroundColor(Color backgrodundColor) {
		backgroundColorComboBox.setColor(backgrodundColor);
	}

	/**
	 * Returns the selected <code>gradientColor</code>.
	 * 
	 * @return The selected <code>gradientColor</code>
	 */
	public Color getGradientColor() {
		return gradientColorComboBox.getColor();
	}

	/**
	 * Set the <code>gradientColor</code>. 
	 * 
	 * @param gradientColor The <code>gradientColor</code> to set
	 */
	public void setGradientColor(Color gradientColor) {
		gradientColorComboBox.setColor(gradientColor);
	}

	/**
	 * Adds an <code>ActionListener</code>.
	 * 
	 * The <code>ActionListener</code>. will receive an <code>ActionEvent</code> when
	 * any of both color selection has been made.
	 * 
	 * @param listener The <code>ActionListener</code> that is to be notified
	 */
	public void addActionListener(ActionListener listener) {
		backgroundColorComboBox.addActionListener(listener);
		gradientColorComboBox.addActionListener(listener);
	}
	
	/**
	 * Removes an <code>ActionListener</code>.
	 * 
	 * @param listener The <code>ActionListener</code> to remove
	 */
	public void removeActionListener(ActionListener listener) {
		backgroundColorComboBox.removeActionListener(listener);
		gradientColorComboBox.removeActionListener(listener);
	}

	// A listener who enables or disables the gradientColorComboBox depending
	// on the value of the backgroundColor.
	private class ChangeColorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (getBackgroundColor() == null)
				gradientColorComboBox.setEnabled(false);
			else
				/*if(!TEditor.get_android_mode())*/
				gradientColorComboBox.setEnabled(true);
		}
	}
}
