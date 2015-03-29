/*
 * File: TToolBarContainer.java
 * 		This file is part of Tico, an application to create and perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Sep 6, 2006
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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Vector;

import javax.swing.JPanel;

/**
 * Component which contains floating tool bars. The component height changes to
 * display all component tool bars.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TToolBarContainer extends JPanel {
	// Minimum component hight to allow floating tool bars to be inserted
	// again in the container
	private final static int MINIMUM_HEIGHT = 5;

	// Tool bar event listener to add to any new tool bar
	private ToolBarEventsListener toolBarListener;
	// Containers tool bar list
	private Vector toolBarsList;

	/**
	 * Creates a new <code>TToolBarContainer</code>.
	 */
	public TToolBarContainer() {
		super();

		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

		ToolBarContainerEventsListener listener = new ToolBarContainerEventsListener();
		addComponentListener(listener);
		addContainerListener(listener);

		toolBarListener = new ToolBarEventsListener();

		toolBarsList = new Vector();
	}

	/**
	 * Add a new <code>TToolBar</code> to the end of the container
	 * 
	 * @param toolBar The <code>TToolBar</code> to be added
	 */
	public void addToolBar(TToolBar toolBar) {
		toolBar.addComponentListener(toolBarListener);
		toolBarsList.add(toolBar);
		super.add(toolBar);
	}

	/**
	 * Add a new <code>toolBar</code> in the specified <code>index</code>.
	 * 
	 * @param toolBar The <code>toolBar</code> to be added
	 * @param index The tool bar insertion <code>index</code>
	 */
	public void addToolBar(TToolBar toolBar, int index) {
		toolBar.addComponentListener(toolBarListener);
		toolBarsList.add(index, toolBar);
		super.add(toolBar, index);
	}
	
	/**
	 * Returns the <code>toolBar</code> in the specified <code>index</code>.
	 * 
	 * @param index The <code>index</code> of the value to return
	 */
	public TToolBar getToolBar(int index) {
		return (TToolBar)toolBarsList.elementAt(index);
	}

	/**
	 * Remove the specified <code>toolBar</code> from the container.
	 * 
	 * @param toolBar The <code>toolBar</code> to be removed
	 */
	public void removeToolBar(TToolBar toolBar) {
		super.remove(toolBar);
		toolBar.removeComponentListener(toolBarListener);
		toolBarsList.remove(toolBar);
	}

	/**
	 * Remove the toolBar in the specified <code>index</code> from the container.
	 * 
	 * @param index The <code>index</code> of the toolBar to be removed
	 */
	public void removeToolBar(int index) {
		TToolBar removalToolBar = (TToolBar)toolBarsList.elementAt(index);
		super.remove(removalToolBar);
		removalToolBar.removeComponentListener(toolBarListener);
		toolBarsList.remove(index);
	}

	// Update containers size to display all its tool bars
	private void updateContainerSize() {
		// Panel current width. It's the space I have to put all the tool bars
		int toolPanelWidth = getWidth();
		// Accumulates the current row width
		int acumulatedRowWidth = 0;

		// Contains the maximum tool bar height of the current row
		int currentRowHeight = 0;
		// Sum of the maximum row height of all rows
		int newToolPanelHeight = 0;

		for (int i = 0; i < getComponentCount(); i++)
			if (((TToolBar)getComponent(i)).isVisible()) {
				// Ger current tool bar properties
				int currentWidth = ((TToolBar)getComponent(i)).getWidth();
				int currentHeight = ((TToolBar)getComponent(i)).getHeight();

				// Accumulates current tool bar width
				acumulatedRowWidth += currentWidth;

				// If the current tool bar fills the row
				if (acumulatedRowWidth > toolPanelWidth) {
					// Create new row with the current width equal to the last
					// tool bar width
					acumulatedRowWidth = currentWidth;
					newToolPanelHeight += currentRowHeight;
					// Initializes the new row height
					currentRowHeight = 0;
				}

				if (currentRowHeight < currentHeight)
					currentRowHeight = currentHeight;
			}

		// Add the last line height
		newToolPanelHeight += currentRowHeight;

		// Check if is empty and set the minimum height in order to let the
		// possibility of add a tool bar when it's empty
		if (newToolPanelHeight < MINIMUM_HEIGHT)
			newToolPanelHeight = MINIMUM_HEIGHT;

		setPreferredSize(new Dimension(0, newToolPanelHeight));
		updateUI();
	}

	// Listener for tool bars to update containers size
	private class ToolBarEventsListener implements ComponentListener {
		public void componentHidden(ComponentEvent e) {
			updateContainerSize();
		}

		public void componentMoved(ComponentEvent e) {
		}

		public void componentResized(ComponentEvent e) {
			updateContainerSize();
		}

		public void componentShown(ComponentEvent e) {
			updateContainerSize();
		}
	}

	// Listener for tool bar container to update its size
	private class ToolBarContainerEventsListener implements ComponentListener,
			ContainerListener {
		public void componentHidden(ComponentEvent e) {
		}

		public void componentMoved(ComponentEvent e) {
		}

		public void componentShown(ComponentEvent e) {
		}

		public void componentResized(ComponentEvent e) {
			updateContainerSize();
		}

		public void componentAdded(ContainerEvent e) {
			// Get the added tool bar
			TToolBar addedToolBar = (TToolBar) e.getChild();
			// Reset the correct orientation
			addedToolBar.setOrientation(TToolBar.HORIZONTAL);
			// Asign it on the correct place
			setComponentZOrder(addedToolBar, toolBarsList.indexOf(addedToolBar));
			// Update the container size
			updateContainerSize();
		}

		public void componentRemoved(ContainerEvent e) {
			updateContainerSize();
		}
	}
}
