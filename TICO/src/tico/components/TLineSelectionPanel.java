/*
 * File: TLineSelectionPanel.java
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
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import tico.configuration.TLanguage;

/**
 * Component to choose a line color and width.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TLineSelectionPanel extends JPanel {
	// Default initial border size
	private static final int DEFAULT_BORDER_SIZE = 1;
	// Default initial border color
	private static final Color DEFAULT_BORDER_COLOR = null;

	// Line color combo box chooser
	private TColorComboBox colorComboBox;
	// Line width combo box chooser	
	private TBorderSizeComboBox sizeComboBox;
	
	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with
	 * <code>lineColor</code> defaults to <i>null</i> and
	 * <code>lineSize</code> to <i>1</i>.
	 */
	public TLineSelectionPanel() {
		this(DEFAULT_BORDER_COLOR, DEFAULT_BORDER_SIZE);
	}
	
	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with the specified
	 * initial <code>lineColor</code> and <code>lineSize</code> defaults
	 * to <i>1</i>.
	 * 
	 * @param color The specified initial <code>borderColor</code>
	 */
	public TLineSelectionPanel(Color color) {
		this(color, DEFAULT_BORDER_SIZE);
	}
	
	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with
	 * <code>lineColor</code> defaults to <i>null</i> and
	 * the specified initial <code>borderSize</code>.
	 * 
	 * @param borderSize The specified initial <code>borderSize</code> value
	 */
	public TLineSelectionPanel(int borderSize) {
		this(DEFAULT_BORDER_COLOR, borderSize);
	}
	
	/**
	 * Creates a new <code>TBorderSelectionPanel</code> with the specified
	 * initial <code>lineColor</code> and <code>lineSize</code>.
	 * 
	 * @param color The specified initial <code>lineColor</code>
	 * @param borderSize The specified initial <code>lineSize</code> value
	 */
	public TLineSelectionPanel(Color color,int borderSize) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.WHITE,
				new Color(165, 163, 151)), TLanguage.getString("TLineSelectionPanel.TITLE")));
		// Creates the components
		JLabel colorLabel = new JLabel( TLanguage.getString("TLineSelectionPanel.COLOR"));
		colorComboBox = new TColorComboBox(false);
		JLabel borderSizeLabel = new JLabel( TLanguage.getString("TLineSelectionPanel.SIZE"));
		sizeComboBox = new TBorderSizeComboBox();

		// Place the components in the panel
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		colorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		add(colorLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 0);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		add(colorComboBox, c);

		borderSizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 20, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 2;
		c.gridy = 0;
		add(borderSizeLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 10);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 3;
		c.gridy = 0;
		add(sizeComboBox, c);
		// Add change color listener
		colorComboBox.addActionListener(new ChangeColorListener());
		// Set initial values
		setLineColor(color);
		setLineSize(borderSize);
	}
	
	/**
	 * Returns the selected <code>lineColor</code>.
	 * 
	 * @return The selected <code>lineColor</code>
	 */		
	public Color getLineColor() {
		return colorComboBox.getColor();
	}

	/**
	 * Set the <code>lineColor</code>. 
	 * 
	 * @param lineColor The <code>lineColor</code> to set
	 */	
	public void setLineColor(Color lineColor) {
		if (lineColor == null) lineColor = UIManager.getColor("Tree.textForeground");
		colorComboBox.setColor(lineColor);
	}
	
	/**
	 * Returns the selected <code>lineSize</code>.
	 * 
	 * @return The selected <code>lineSize</code>
	 */		
	public int getLineSize() {
		return sizeComboBox.getBorderSize();
	}

	/**
	 * Set the <code>lineSize</code>. 
	 * 
	 * @param lineSize The <code>lineSize</code> to set
	 */		
	public void setLineSize(int lineSize) {
		sizeComboBox.setBorderSize(lineSize);
	}

	/**
	 * Adds an <code>ActionListener</code>.
	 * 
	 * The <code>ActionListener</code> will receive an <code>ActionEvent</code>
	 * when any of <code>lineColor</code> or <code>lineSize</code> selection
	 * has been made.
	 * 
	 * @param listener The <code>ActionListener</code> that is to be notified
	 */	
	public void addActionListener(ActionListener listener) {
		colorComboBox.addActionListener(listener);
		sizeComboBox.addActionListener(listener);
	}
	
	/**
	 * Removes an <code>ActionListener</code>.
	 * 
	 * @param listener The <code>ActionListener</code> to remove
	 */	
	public void removeActionListener(ActionListener listener) {
		colorComboBox.removeActionListener(listener);
		sizeComboBox.removeActionListener(listener);
	}
	
	// A listener who enables or disables the lineSizeComboBox depending
	// on the value of the lineColor.		
	private class ChangeColorListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (getLineColor() == null) sizeComboBox.setEnabled(false);
			else sizeComboBox.setEnabled(true);
		}
	}
}
