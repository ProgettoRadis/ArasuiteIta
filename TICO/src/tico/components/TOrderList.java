/*
 * File: TOrderList.java
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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tico.components.events.OrderChangeEvent;
import tico.components.events.OrderChangeListener;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;

/**
 * Component who allows to order a list of classes.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TOrderList extends JPanel {
	// Component min size
	private static int LIST_MIN_HEIGHT = 122;
	private static int LIST_MIN_WIDTH = 200;

	// List scroll pane
	private JScrollPane listScroll;
	// The list
	private JList orderList;
	
	// The panel which contains the buttons to change
	// the list order
	private JPanel orderButtonPanel;
	// Move selected list element up
	private TButton upButton;
	// Move selected list element down
	private TButton downButton;

	/**
	 * Creates a new <code>TOrderList</code>.
	 */
	public TOrderList() {
		super();
		// Create components
		createOrderList();
		createButtonPanel();
		
		// Place components
		setLayout(new BorderLayout());

		add(listScroll, BorderLayout.CENTER);
		add(orderButtonPanel, BorderLayout.SOUTH);
		
		// Update components
		updateComponents();
	}

	// Creates the list
	private void createOrderList() {
		listScroll = new JScrollPane();
		// Set minimum scroll pane size
		listScroll.setPreferredSize(new Dimension(LIST_MIN_WIDTH,
				LIST_MIN_HEIGHT));
		listScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// Create the list
		orderList = new JList();
		orderList
				.setMinimumSize(new Dimension(LIST_MIN_WIDTH, LIST_MIN_HEIGHT));
		orderList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateComponents();
			}
		});
		listScroll.setViewportView(orderList);
	}

	// Creates the button panel
	private void createButtonPanel() {
		orderButtonPanel = new JPanel();
		// Creates up button
		upButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				selectedCellTo(orderList.getSelectedIndex() - 1);
			}
		});
		upButton.setIcon(TResourceManager.getImageIcon("move-up-16.png"));
		upButton.setMargin(new Insets(2, 2, 2, 2));
		upButton.setToolTipText(TLanguage.getString("TOrderList.UP_TOOLTIP"));
		// Creates down button
		downButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				selectedCellTo(orderList.getSelectedIndex() + 1);
			}
		});
		downButton.setIcon(TResourceManager.getImageIcon("move-down-16.png"));
		downButton.setMargin(new Insets(2, 2, 2, 2));
		downButton.setToolTipText(TLanguage.getString("TOrderList.DOWN_TOOLTIP"));
		// Add buttons to the panel
		orderButtonPanel.add(upButton);
		orderButtonPanel.add(downButton);
	}

	// Move the selected cell to index
	private void selectedCellTo(int index) {
		int listSize = orderList.getModel().getSize();

		if ((0 <= index) && (index < listSize)) {
			ArrayList newOrderList = new ArrayList();

			for (int i = 0; i < orderList.getModel().getSize(); i++)
				newOrderList.add(orderList.getModel().getElementAt(i));

			Object selectedObject = orderList.getSelectedValue();
			int currentPosition = orderList.getSelectedIndex();
			newOrderList.remove(selectedObject);
			newOrderList.add(index, selectedObject);

			setList(newOrderList);
			orderList.setSelectedIndex(index);
			updateComponents();

			fireOrderChanged(selectedObject, currentPosition, index);
		} else
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ listSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		orderList.setOpaque(enabled);
		orderList.setEnabled(enabled);
		updateComponents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#isEnabled()
	 */
	public boolean isEnabled() {
		return orderList.isEnabled();
	}

	/**
     * Returns <i>true</i> if nothing is selected.
     * 
     * @return <i>true</i> if nothing is selected
	 */
	public boolean isSelectionEmpty() {
		return orderList.isSelectionEmpty();
	}

	/**
	 * Returns the selected <code>value</code>, or <i>null</i> if the selection
	 * is empty.
	 * 
	 * @return The selected <code>value</code>
	 */
	public Object getSelectedValue() {
		return orderList.getSelectedValue();
	}

	/**
	 * Returns the selected <code>index</code>; returns <i>-1</i>
	 * if there is no selected item.
	 * 
	 * @return The selected value <code>index</code>
	 */
	public int getSelectedIndex() {
		return orderList.getSelectedIndex();
	}

	/**
	 * Selects a single <code>value</code>.
	 * 
	 * @param value The <code>value</code> to select
	 */
	public void setSelectedValue(Object value) {
		orderList.setSelectedValue(value, true);
	}

	/**
	 * Selects a single <code>value</code>.
	 * 
	 * @param index The <code>index</code> of the value to select
	 */
	public void setSelectedIndex(int index) {
		orderList.setSelectedIndex(index);
	}

	/**
	 * Remove the selected <code>value</code> from the list.
	 */
	public void removeSelectedValue() {
		ArrayList cellList = getList();
		cellList.remove(orderList.getSelectedValue());
		orderList.setListData(cellList.toArray());
	}

	/**
	 * Add an <code>value</code> to the end of the ordered list.
	 * 
	 * @param value The <code>value</code> to be added
	 */
	public void addElement(Object value) {
		ArrayList cellList = getList();
		if (!cellList.contains(value))
			cellList.add(value);
		orderList.setListData(cellList.toArray());
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
		orderList.setListData(cellList.toArray());
	}

	/**
	 * Remove the value in the specified <code>index</code> from the list.
	 * 
	 * @param index The <code>index</code> of the value to be removed
	 */
	public void removeElement(int index) {
		ArrayList cellList = getList();
		cellList.remove(index);
		orderList.setListData(cellList.toArray());
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
		orderList.addListSelectionListener(listener);
	}
	
	/**
	 * Removes an <code>ListSelectionListener</code>.
	 * 
	 * @param listener The <code>ListSelectionListener</code> to remove
	 */	
	public void removeListSelectionListener(ListSelectionListener listener) {
		orderList.removeListSelectionListener(listener);
	}

	/**
	 * Adds an <code>OrderChangeListener</code>.
	 * 
	 * The <code>OrderChangeListener</code> will receive an <code>OrderChangeEvent</code>
	 * when the order of the list values is changed.
	 * 
	 * @param listener The <code>OrderChangeListener</code> that is to be notified
	 */	
	public void addOrderChangeListener(OrderChangeListener listener) {
		listenerList.add(OrderChangeListener.class, listener);
	}
	
	/**
	 * Removes an <code>OrderChangeListener</code>.
	 * 
	 * @param listener The <code>OrderChangeListener</code> to remove
	 */	
	public void removeOrderChangeListener(OrderChangeListener listener) {
		listenerList.remove(OrderChangeListener.class, listener);
	}

	// Fire OrderChangedEvent
	private void fireOrderChanged(Object changedObject, int initialPosition,
			int finalPosition) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == OrderChangeListener.class) {
				((OrderChangeListener)listeners[i + 1])
						.orderChanged(new OrderChangeEvent(this,
								changedObject, initialPosition, finalPosition));
			}
		}
	}

	// Enables and disables the corresponding components
	private void updateComponents() {
		if (orderList.isEnabled()) {
			upButton.setEnabled(!orderList.isSelectionEmpty());
			downButton.setEnabled(!orderList.isSelectionEmpty());

			if (!orderList.isSelectionEmpty()) {
				upButton.setEnabled(!(orderList.getSelectedIndex() == 0));
				downButton
						.setEnabled(!(orderList.getSelectedIndex() == orderList
								.getModel().getSize() - 1));
			}
		} else {
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		}
	}

	/**
	 * Returns the selected <code>list</code>.
	 * 
	 * @return The selected <code>list</code>
	 */	
	public ArrayList getList() {
		ArrayList cellList = new ArrayList();

		for (int i = 0; i < orderList.getModel().getSize(); i++)
			cellList.add(orderList.getModel().getElementAt(i));

		return cellList;
	}

	/**
	 * Set the <code>list</code>. 
	 * 
	 * @param list The <code>list</code> to set
	 */	
	public void setList(ArrayList list) {
		orderList.setListData(list.toArray());
	}
}
