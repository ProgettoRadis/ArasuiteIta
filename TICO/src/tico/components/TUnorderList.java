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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

/**
 * Component who contains a list of classes.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TUnorderList extends JPanel {
	// Component min size
	private static int LIST_MIN_HEIGHT = 91;
	private static int LIST_MIN_WIDTH = 200;

	// List scroll pane
	private JScrollPane listScroll;
	// The list
	private JList unorderList;

	/**
	 * Creates a new <code>TUnorderList</code>.
	 */
	public TUnorderList() {
		super();

		createUnorderList();

		setLayout(new BorderLayout());

		add(listScroll, BorderLayout.CENTER);
	}

	// Creates the list
	private void createUnorderList() {
		listScroll = new JScrollPane();

		listScroll.setPreferredSize(new Dimension(LIST_MIN_WIDTH,
				LIST_MIN_HEIGHT));
		listScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		unorderList = new JList();
		unorderList.setMinimumSize(new Dimension(LIST_MIN_WIDTH,
				LIST_MIN_HEIGHT));
		listScroll.setViewportView(unorderList);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		unorderList.setOpaque(enabled);
		unorderList.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#isEnabled()
	 */
	public boolean isEnabled() {
		return unorderList.isEnabled();
	}

	/**
     * Returns <i>true</i> if nothing is selected.
     * 
     * @return <i>true</i> if nothing is selected
	 */
	public boolean isSelectionEmpty() {
		return unorderList.isSelectionEmpty();
	}

	/**
	 * Returns the selected <code>value</code>, or <i>null</i> if the selection
	 * is empty.
	 * 
	 * @return The selected <code>value</code>
	 */
	public Object getSelectedValue() {
		return unorderList.getSelectedValue();
	}

	/**
	 * Returns the selected <code>index</code>; returns <i>-1</i>
	 * if there is no selected item.
	 * 
	 * @return The selected value <code>index</code>
	 */
	public int getSelectedIndex() {
		return unorderList.getSelectedIndex();
	}

	/**
	 * Selects a single <code>value</code>.
	 * 
	 * @param value The <code>value</code> to select
	 */
	public void setSelectedValue(Object value) {
		unorderList.setSelectedValue(value, true);
	}

	/**
	 * Selects a single <code>value</code>.
	 * 
	 * @param index The <code>index</code> of the value to select
	 */
	public void setSelectedIndex(int index) {
		unorderList.setSelectedIndex(index);
	}

	/**
	 * Remove the selected <code>value</code> from the list.
	 */
	public void removeSelectedValue() {
		ArrayList cellList = getList();
		cellList.remove(unorderList.getSelectedValue());
		unorderList.setListData(cellList.toArray());
	}

	/**
	 * Add an <code>value</code> to the end of the ordered list.
	 * 
	 * @param value The <code>value</code> to be added
	 */
	public void addElement(Object value) {
		ArrayList cellList = getList();
		// If is repeared do not add it
		if (!cellList.contains(value))
			cellList.add(value);
		// Set the new data list
		unorderList.setListData(cellList.toArray());
		// Set the added value selected
		setSelectedValue(value);
	}

	/**
	 * Remove the specified <code>value</code> from the list.
	 * 
	 * @param value The <code>value</code> to be removed
	 */
	public void removeElement(Object value) {
		ArrayList cellList = getList();
		cellList.remove(value);
		unorderList.setListData(cellList.toArray());
	}

	/**
	 * Remove the value in the specified <code>index</code> from the list.
	 * 
	 * @param index The <code>index</code> of the value to be removed
	 */
	public void removeElement(int index) {
		ArrayList cellList = getList();
		cellList.remove(index);
		unorderList.setListData(cellList.toArray());
	}

	/**
	 * Adds an <code>ListSelectionListener</code>.
	 * 
	 * The <code>ListSelectionListener</code> will receive an <code>ListSelectionEvent</code>
	 * when the list selection is changed.
	 * 
	 * @param listener The <code>ListSelectionListener</code> that is to be notified
	 */	
	public void addListSelectionListener(ListSelectionListener listener) {
		unorderList.addListSelectionListener(listener);
	}
	
	/**
	 * Removes an <code>ListSelectionListener</code>.
	 * 
	 * @param listener The <code>ListSelectionListener</code> to remove
	 */	
	public void removeListSelectionListener(ListSelectionListener listener) {
		unorderList.removeListSelectionListener(listener);
	}

	/**
	 * Returns the selected <code>list</code>.
	 * 
	 * @return The selected <code>list</code>
	 */	
	public ArrayList getList() {
		ArrayList cellList = new ArrayList();

		for (int i = 0; i < unorderList.getModel().getSize(); i++)
			cellList.add(unorderList.getModel().getElementAt(i));

		return cellList;
	}

	/**
	 * Set the <code>list</code>. 
	 * 
	 * @param list The <code>list</code> to set
	 */	
	public void setList(ArrayList list) {
		unorderList.setListData(list.toArray());
	}
}
