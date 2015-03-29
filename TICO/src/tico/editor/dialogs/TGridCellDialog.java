/*
 * File: TGridCellDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Mar 6, 2006
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

package tico.editor.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.board.components.TGrid;
import tico.components.TAlternativeBorderSelectionPanel;
import tico.components.TGridOrderPanel;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;

/**
 * Dialog to change <code>TGridCellDialog</code> attributes.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TGridCellDialog extends TCellDialog {
	private static String DEFAULT_TITLE = TLanguage.getString("TGridCellDialog.TITLE");

	private JPanel gridPanel;

	private TGridOrderPanel orderSelectionPanel;
	
	private TAlternativeBorderSelectionPanel borderPanel;
	/**
	 * Creates a new <code>TGridCellDialog</code> to edit the <code>gridCell</code>
	 * properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the
	 * cell to be edited
	 * @param gridCell The <code>gridCell</code> to be edited
	 */
	public TGridCellDialog(TBoardContainer boardContainer, TComponent gridCell) {
		super(boardContainer, DEFAULT_TITLE, gridCell);
	}

	private void createAnotherBorderSelectionPanel() {
		Map map = ((TGrid) getComponent().getParent()).getAttributes();

		borderPanel = new TAlternativeBorderSelectionPanel();

		borderPanel.setBorderColor(TBoardConstants.getChangeColorGrid(map));
		
		borderPanel.setBorderSize(Math.max(4,Math.round(TBoardConstants.getChangeLineWidthGrid(map))));

	}
	//
	protected void createIdField() {
		super.createIdField();

//		idTextField.setText(((TGrid) getComponent().getParent()).getId());
		idTextField.setText(getComponent().getId());//mio
		
		Map map = getComponent().getAttributes();
		
/*		JLabel positionLabel = new JLabel("(" + TBoardConstants.getRow(map) +
				"," + TBoardConstants.getColumn(map) + ")");
		
		idFieldPanel.add(positionLabel);
*/
	}

	protected void createTabbedPane() {
		super.createTabbedPane();

//		createGridOrderPanel();

//		tabbedPane.addTab(TLanguage.getString("TGridCellDialog.TAB_GRID"), gridPanel);
	}

	private void createGridOrderPanel() {
		gridPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		gridPanel.setLayout(new GridBagLayout());

		createOrderSelectionPanel();
		createAnotherBorderSelectionPanel();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		gridPanel.add(borderPanel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		gridPanel.add(orderSelectionPanel, c);
	}
	 
	private void createOrderSelectionPanel() {
		Map map = ((TGrid) getComponent().getParent()).getAttributes();

		orderSelectionPanel = new TGridOrderPanel(TBoardConstants
				.getOrder(map));

		orderSelectionPanel.setOrderedCellList(TBoardConstants
				.getOrderedCellList(map));
	}

	protected Map newComponentsAttributeMap() {
		Map nested = super.newComponentsAttributeMap();
		Map attributeMap = new Hashtable();

		// Clean the ID of the cell
//		((Map) nested.get(getComponent())).remove(TBoardConstants.ID);

		// Asign the ID to the grid
//		TGrid grid = (TGrid) getComponent().getParent();
//		TBoardConstants.setId(attributeMap, idTextField.getText());
		
/*		TBoardConstants.setChangeColorGrid(attributeMap,borderPanel.getBorderColor());
		TBoardConstants.setChangeLineWidthGrid(attributeMap,borderPanel.getBorderSize());
		
		// Get the new grid order
		int order = orderSelectionPanel.getOrder();
		// Asign it to the grid
		TBoardConstants.setOrder(attributeMap, order);
		TBoardConstants.setOrderedCellList(attributeMap, orderSelectionPanel
				.getOrderedCellList());
		nested.put(grid, attributeMap);

		// If the order...
		if (order == TGrid.COLUMNS)
			// ...changes to columns, get new order
			nested.putAll(grid.reorderColumns());
		else if (order == TGrid.ROWS)
			// ...changes to rows, get new order
			nested.putAll(grid.reorderRows());
*/
		return nested;
	}

}
