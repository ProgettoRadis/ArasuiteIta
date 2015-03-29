/*
 * File: TGridOrderPanel.java
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
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tico.board.components.TGrid;
import tico.configuration.TLanguage;

/**
 * Components to select to order a TGrid.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TGridOrderPanel extends JPanel {
	// Default initial order
	private static final int DEFAULT_ORDER = TGrid.SEQUENTIAL;

	// Order selection radio button panel
	private JPanel radioButtonOrderPanel;
	// Order radio buttons
	private JRadioButton sequentialOrderRadioButton;
	private JRadioButton columnsOrderRadioButton;
	private JRadioButton rowsOrderRadioButton;
	private JRadioButton customOrderRadioButton;
	// Custom order cell list
	private TOrderList orderedCellList;

	/**
	 * Creates a new <code>TGridOrderPanel</code> with <code>order</code>
	 * defaults to <i>SEQUENTIAL</i>.
	 */
	public TGridOrderPanel() {
		this(DEFAULT_ORDER);
	}

	/**
	 * Creates a new <code>TGridOrderPanel</code> with the specified
	 * initial <code>order</code>.
	 */
	public TGridOrderPanel(int order) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), TLanguage.getString("TGridOrderPanel.TITLE")));
		// Creates the components
		createRadioButtonOrderPanel();
		orderedCellList = new TOrderList();

		// Places the components
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		add(radioButtonOrderPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		add(orderedCellList, c);

		setOrder(order);
	}

	// Create the order panel choser
	private void createRadioButtonOrderPanel() {
		radioButtonOrderPanel = new JPanel();

		radioButtonOrderPanel.setLayout(new GridLayout(4, 1, 0, 5));

		sequentialOrderRadioButton = new JRadioButton(TLanguage.getString("TGridOrderPanel.ORDER_SEQUENTIAL"));
		columnsOrderRadioButton = new JRadioButton(TLanguage.getString("TGridOrderPanel.ORDER_COLUMNS"));
		rowsOrderRadioButton = new JRadioButton(TLanguage.getString("TGridOrderPanel.ORDER_ROWS"));
		customOrderRadioButton = new JRadioButton(TLanguage.getString("TGridOrderPanel.ORDER_CUSTOM"));

		customOrderRadioButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateComponents();
			}
		});

		ButtonGroup orderButtonGroup = new ButtonGroup();
		orderButtonGroup.add(sequentialOrderRadioButton);
		orderButtonGroup.add(columnsOrderRadioButton);
		orderButtonGroup.add(rowsOrderRadioButton);
		orderButtonGroup.add(customOrderRadioButton);
		
		radioButtonOrderPanel.add(sequentialOrderRadioButton);
		radioButtonOrderPanel.add(columnsOrderRadioButton);
		radioButtonOrderPanel.add(rowsOrderRadioButton);
		radioButtonOrderPanel.add(customOrderRadioButton);
	}

	// Enables and disables the corresponding components
	private void updateComponents() {
		orderedCellList.setEnabled(customOrderRadioButton.isSelected());
	}

	/**
	 * Set the <code>order</code>. 
	 * 
	 * @return The <code>order</code> to set. Must be one of the following
	 * constants defined in <code>TGrid</code>: SEQUENTIAL (the default value),
	 * COLUMNS, ROWS or CUSTOM.
	 * 
	 * @see TGrid
	 */
	public int getOrder() {
		// Return a value depending on the selected radio button
		if (columnsOrderRadioButton.isSelected())
			return TGrid.COLUMNS;
		else if (rowsOrderRadioButton.isSelected())
			return TGrid.ROWS;
		else if (customOrderRadioButton.isSelected())
			return TGrid.CUSTOM;
		else
			return TGrid.SEQUENTIAL;
	}

	/**
	 * Returns the selected <code>order</code>.
	 * 
	 * @param order The selected <code>order</code>. One of the following
	 * constants defined in TGrid: SEQUENTIAL (the default value), COLUMNS,
	 * ROWS or CUSTOM.
	 * 
	 * @see TGrid
	 */
	public void setOrder(int order) {
		// Select the corresponding radio button based on the order
		if (order == TGrid.COLUMNS)
			columnsOrderRadioButton.setSelected(true);
		else if (order == TGrid.ROWS)
			rowsOrderRadioButton.setSelected(true);
		else if (order == TGrid.CUSTOM)
			customOrderRadioButton.setSelected(true);
		else
			sequentialOrderRadioButton.setSelected(true);
		// Update the components
		updateComponents();
	}

	/**
	 * Returns the selected <code>cellList</code>.
	 * 
	 * @return The selected <code>cellList</code>
	 */
	public ArrayList getOrderedCellList() {
		return orderedCellList.getList();
	}

	/**
	 * Set the <code>cellList</code>. 
	 * 
	 * @param cellList The <code>cellList</code> to set
	 */
	public void setOrderedCellList(ArrayList cellList) {
		orderedCellList.setList(cellList);
	}
}
