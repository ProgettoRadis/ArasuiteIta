/*
 * File: TBorderSelectionPanel.java
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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import tico.configuration.TLanguage;

/**
 * Component to choose a border color and border width to create a line border.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBorderSelectionPanel extends JPanel {
	// Default initial border size pameter
	private static final int DEFAULT_BORDER_SIZE = 1;
	// Default initial border color parameter
	private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

	// Border color combo box chooser
	private TColorComboBox colorComboBox;
	// Border size combo box chooser
	private TBorderSizeComboBox borderSizeComboBox;
	
	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with
	 * <code>borderColor</code> defaults to <i>black</i> and
	 * <code>borderSize</code> to <i>1</i>.
	 */
	public TBorderSelectionPanel() {
		this(DEFAULT_BORDER_COLOR, DEFAULT_BORDER_SIZE);
	}

	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with the specified
	 * initial <code>borderColor</code> and <code>borderSize</code> defaults
	 * to <i>1</i>.
	 * 
	 * @param borderColor The specified initial <code>borderColor</code>
	 */
	public TBorderSelectionPanel(Color borderColor) {
		this(borderColor, DEFAULT_BORDER_SIZE);
	}
	
	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with
	 * <code>borderColor</code> defaults to <i>black</i> and
	 * the specified initial <code>borderSize</code>.
	 * 
	 * @param borderSize The specified initial <code>borderSize</code> value.
	 */
	public TBorderSelectionPanel(int borderSize) {
		this(DEFAULT_BORDER_COLOR, borderSize);
	}
	
	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with the specified
	 * initial <code>borderColor</code> and <code>borderSize</code>.
	 * 
	 * @param borderColor The specified initial <code>borderColor</code>
	 * @param borderSize The specified initial <code>borderSize</code> value.
	 */
	public TBorderSelectionPanel(Color borderColor,int borderSize) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.WHITE,
				new Color(165, 163, 151)), TLanguage.getString("TBorderSelectionPanel.TITLE")));
		// Creates the components
		JLabel colorLabel = new JLabel( TLanguage.getString("TBorderSelectionPanel.COLOR"));
		colorComboBox = new TColorComboBox(true);
		JLabel borderSizeLabel = new JLabel( TLanguage.getString("TBorderSelectionPanel.SIZE"));
		borderSizeComboBox = new TBorderSizeComboBox();
		
		// Place the components in the panel
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		// Place colorLabel
		colorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		add(colorLabel, c);
		// Place colorComboBox
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 0);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		add(colorComboBox, c);
		// Place borderSizeLabel
		borderSizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 20, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 2;
		c.gridy = 0;
		add(borderSizeLabel, c);
		// Place borderSizeComboBox
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 10);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 3;
		c.gridy = 0;
		add(borderSizeComboBox, c);
		// Add change color listener
		colorComboBox.addActionListener(new ChangeColorListener());
		// Set initial values
		setBorderColor(borderColor);
		setBorderSize(borderSize);
	}

	/**
	 * Returns the selected <code>borderColor</code>.
	 * 
	 * @return The selected <code>borderColor</code>
	 */	
	public Color getBorderColor() {
		return colorComboBox.getColor();
	}

	/**
	 * Set the <code>borderColor</code>. 
	 * 
	 * @param borderColor The <code>borderColor</code> to set
	 */	
	public void setBorderColor(Color borderColor) {
		colorComboBox.setColor(borderColor);
	}

	/**
	 * Returns the selected <code>borderSize</code>.
	 * 
	 * @return The selected <code>borderSize</code>
	 */		
	public int getBorderSize() {
		return borderSizeComboBox.getBorderSize();
	}

	/**
	 * Set the <code>borderSize</code>. 
	 * 
	 * @param borderSize The <code>borderSize</code> to set
	 */	
	public void setBorderSize(int borderSize) {
		borderSizeComboBox.setBorderSize(borderSize);
	}
	
	/**
	 * Set a <i>line border</i> created with the components
	 * <code>borderColor</code> and <code>borderSize</code>. 
	 * 
	 * @return The created <i>line border</i>
	 */	
	public Border getSelectedBorder() {
		// If the border color is null, return a null border
		if (getBorderColor() != null)
			return BorderFactory.createLineBorder(getBorderColor(),
					getBorderSize());
		else
			return null;
	}

	/**
	 * Adds an <code>ActionListener</code>.
	 * 
	 * The <code>ActionListener</code>. will receive an <code>ActionEvent</code>
	 * when any of <code>borderColor</code> or <code>borderSize</code> selection
	 * has been made.
	 * 
	 * @param listener The <code>ActionListener</code> that is to be notified
	 */	
	public void addActionListener(ActionListener listener) {
		colorComboBox.addActionListener(listener);
		borderSizeComboBox.addActionListener(listener);
	}
	
	/**
	 * Removes an <code>ActionListener</code>.
	 * 
	 * @param listener The <code>ActionListener</code> to remove
	 */	
	public void removeActionListener(ActionListener listener) {
		colorComboBox.removeActionListener(listener);
		borderSizeComboBox.removeActionListener(listener);
	}
	
	// A listener who enables or disables the borderSizeComboBox depending
	// on the value of the borderColor.	
	private class ChangeColorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (getBorderColor() == null) borderSizeComboBox.setEnabled(false);
			else borderSizeComboBox.setEnabled(true);			
		}
	}
}
