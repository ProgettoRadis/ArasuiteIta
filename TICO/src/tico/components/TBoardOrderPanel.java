/*
 * File: TBoardOrderPanel.java
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
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;

/**
 * Components who allows to order a TBoard browseable components list.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardOrderPanel extends JPanel {
	// The unordered components list
	private TUnorderList unorderedList;
	// Panel with the buttons to add or remove elements from order list
	private JPanel buttonPanel;
	// Add element to ordered list button
	private TButton addOrderedButton;
	// Remove element from ordered list button
	private TButton removeOrderedButton;
	// The ordered components list
	private TOrderList orderedList;

	/**
	 * Creates a new <code>TBoardOrderPanel</code>.
	 */
	public TBoardOrderPanel() {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), TLanguage.getString("TBoardOrderPanel.TITLE")));
		// Creates the components
		JLabel unorderedLabel = new JLabel(TLanguage.getString("TBoardOrderPanel.WITHOUT_ORDER"));
		createUnorderedCellList();
		createButtonPanel();
		JLabel orderedLabel = new JLabel(TLanguage.getString("TBoardOrderPanel.WITH_ORDER"));
		createOrderedCellList();
		
		// Places the components
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// Places the unorderedLabel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		add(unorderedLabel, c);
		// Places the unorderedList		
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 1;
		add(unorderedList, c);
		// Places the buttonPanel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 2;
		add(buttonPanel, c);
		// Places the orderedLabel		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 3;
		add(orderedLabel, c);
		// Places the orderedList
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 4;
		add(orderedList, c);
		
		// Update the components
		updateComponents();
	}

	// Create the unordered cell list component
	private void createUnorderedCellList() {
		unorderedList = new TUnorderList();
		// update the components after each change
		unorderedList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateComponents();
			}
		});
	}

	// Create the create button panel 
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		// Create the add button with the corresponding action
		addOrderedButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addOrdered();
			}
		});
		addOrderedButton.setIcon(TResourceManager
				.getImageIcon("move-add-16.png"));
		addOrderedButton.setMargin(new Insets(2, 2, 2, 2));
		addOrderedButton.setToolTipText(TLanguage.getString("TBoardOrderPanel.ADD_TO_ORDER_TOOLTIP"));
		// Create the remove button with the corresponding action
		removeOrderedButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				removeOrdered();				
			}
		});
		removeOrderedButton.setIcon(TResourceManager
				.getImageIcon("move-remove-16.png"));
		removeOrderedButton.setMargin(new Insets(2, 2, 2, 2));
		removeOrderedButton.setToolTipText(TLanguage.getString("TBoardOrderPanel.REMOVE_FROM_ORDER_TOOLTIP"));
		// Add the buttons to the panel
		buttonPanel.add(addOrderedButton);
		buttonPanel.add(removeOrderedButton);
	}

	// Create the ordered cell list component
	private void createOrderedCellList() {
		orderedList = new TOrderList();
		// update the components after each change
		orderedList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateComponents();
			}
		});
	}

	// Move the element selected in the unordered list to the ordered list
	private void addOrdered() {
		if (!unorderedList.isSelectionEmpty()) {
			Object object = unorderedList.getSelectedValue();
			unorderedList.removeSelectedValue();
		
			orderedList.addElement(object);
		}
	}

	// Move the element selected in the ordered list to the unordered list
	private void removeOrdered() {
		if (!orderedList.isSelectionEmpty()) {
			Object object = orderedList.getSelectedValue();
			orderedList.removeSelectedValue();

			unorderedList.addElement(object);
		}
	}
	
	// Enables and disables the corresponding components
	private void updateComponents() {
		addOrderedButton.setEnabled(!unorderedList.isSelectionEmpty());
		removeOrderedButton.setEnabled(!orderedList.isSelectionEmpty());
	}

	/**
	 * Returns the selected <code>orderedList</code>.
	 * 
	 * @return The selected <code>orderedList</code>
	 */
	public ArrayList getOrderedList() {
		return orderedList.getList();
	}

	/**
	 * Set the <code>orderedList</code>. 
	 * 
	 * @param cellList The <code>orderedList</code> to set
	 */
	public void setOrderedList(ArrayList cellList) {
		orderedList.setList(cellList);
	}

	/**
	 * Returns the selected <code>unorderedList</code>.
	 * 
	 * @return The selected <code>unorderedList</code>
	 */
	public ArrayList getUnorderedList() {
		return unorderedList.getList();
	}

	/**
	 * Set the <code>unorderedList</code>. 
	 * 
	 * @param cellList The <code>unorderedList</code> to set
	 */
	public void setUnorderedList(ArrayList cellList) {
		unorderedList.setList(cellList);
	}
}
