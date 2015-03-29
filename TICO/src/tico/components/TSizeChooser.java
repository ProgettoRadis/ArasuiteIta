/*
 * File: TSizeChooser.java
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import tico.configuration.TLanguage;

/**
 * Component to choose size, divided in width and height.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TSizeChooser extends JPanel {
	// Default initial size value
	private static final Dimension DEFAULT_DIMENSION = new Dimension(800, 600);
	
	// The width input spinner
	private JSpinner widthSpinner;
	// The height input spinner
	private JSpinner heightSpinner;

	/**
	 * Creates a new <code>TSizeChooser</code> with <code>size</code> defaults
	 * to <i>800x600</i>. 
	 */
	public TSizeChooser() {
		this(DEFAULT_DIMENSION);
	}

	/**
	 * Creates a new <code>TSizeChooser</code> with the specified initial
	 * <code>size</code>. 
	 * 
	 * @param size The specified initial <code>size</code>
	 */
	public TSizeChooser(Dimension size) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), TLanguage.getString("TSizeChooser.TITLE")));
		
		// Creates the components
		// Creates the spinner models
		SpinnerModel widthSpinnerModel = new SpinnerNumberModel(1, 1, 10000, 10);
		SpinnerModel heightSpinnerModel = new SpinnerNumberModel(1, 1, 10000,
				10);
		// Creates the components
		JLabel widthLabel = new JLabel(TLanguage.getString("TSizeChooser.WIDTH"));
		widthSpinner = new JSpinner(widthSpinnerModel);
		JLabel heightLabel = new JLabel(TLanguage.getString("TSizeChooser.HEIGHT"));
		heightSpinner = new JSpinner(heightSpinnerModel);
		
		// Places the components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		widthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		add(widthLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 0);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		add(widthSpinner, c);

		heightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 20, 10, 0);
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 2;
		c.gridy = 0;
		add(heightLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 10);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 3;
		c.gridy = 0;
		add(heightSpinner, c);

		setSize(size);
	}

	/**
	 * Returns the selected <code>size</code>.
	 * 
	 * @return The selected <code>size</code>
	 */	
	public Dimension getSelectedSize() {
		
		return new Dimension(((Integer)widthSpinner.getValue()).intValue(),
				((Integer)heightSpinner.getValue()).intValue());
	}

	/**
	 * Set the <code>size</code>. 
	 * 
	 * @param size The <code>size</code> to set
	 */
	public void setSelectedSize(Dimension size) {
		widthSpinner.getModel().setValue(new Integer(size.width));
		heightSpinner.getModel().setValue(new Integer(size.height));
	}
}
