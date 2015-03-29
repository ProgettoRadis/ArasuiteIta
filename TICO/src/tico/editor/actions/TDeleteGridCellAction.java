/*
 * File: TDeleteGridCellAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jan 30, 2006
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

package tico.editor.actions;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoardConstants;
import tico.board.TBoardLayoutCache;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich deletes the selected grid cell from a grid.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TDeleteGridCellAction extends TEditorAbstractAction {
	// The grid in which the new TGridCell is going to be deleted.
	private TGridCell cell;

	/**
	 * Constructor for TDeleteGridCellAction.
	 * 
	 * @param editor The boards' editor
	 * @param cell The cell to delete
	 */
	public TDeleteGridCellAction(TEditor editor, TGridCell cell) {
		super(editor, TLanguage.getString("TDeleteGridCellAction.NAME"));
		this.cell = cell;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Map nested = new Hashtable();
		Map attributeMap;

		TGrid grid = (TGrid)cell.getParent();
		List children = grid.getChildren();
		int delCol, delRow, curCol, curRow, colSize, rowSize;

		int order = TBoardConstants.getOrder(grid.getAttributes());

		// Get deleting position
		delCol = TBoardConstants.getColumn(cell.getAttributes());
		delRow = TBoardConstants.getRow(cell.getAttributes());

		// Get last deleting position column and row sizes
		colSize = grid.getLastRowColumn(delCol);
		rowSize = grid.getLastColumnRow(delRow);

		// For each TGridCell in the grid
		for (int i = 0; i < children.size(); i++) {
			// Get its position
			curCol = TBoardConstants.getColumn(((TGridCell)children.get(i))
					.getAttributes());
			curRow = TBoardConstants.getRow(((TGridCell)children.get(i))
					.getAttributes());
			// If the order is ROWS
			if (order == TGrid.ROWS) {
				// If is the last cell of the column, shift down all the column
				// values bigger than delCol
				if ((rowSize == 1) && (delRow < curRow)) {
					attributeMap = new AttributeMap();
					TBoardConstants.setRow(attributeMap, curRow - 1);
					nested.put(children.get(i), attributeMap);
				}
				// Shift down all the rows values
				// bigger than delRow in the delCol
				if ((delCol < curCol) && (delRow == curRow)) {
					attributeMap = new AttributeMap();
					TBoardConstants.setColumn(attributeMap, curCol - 1);
					nested.put(children.get(i), attributeMap);
				}
			} else { // If the order is COLUMNS, CUSTOM or SEQUENTIAL
				// If is the last cell of the row, shift down all the row
				// values bigger than delRow
				if ((colSize == 1) && (delCol < curCol)) {
					attributeMap = new AttributeMap();
					TBoardConstants.setColumn(attributeMap, curCol - 1);
					nested.put(children.get(i), attributeMap);
				}
				// Shift down all the cols values
				// bigger than delCol in the delRow
				if ((delRow < curRow) && (delCol == curCol)) {
					attributeMap = new AttributeMap();
					TBoardConstants.setRow(attributeMap, curRow - 1);
					nested.put(children.get(i), attributeMap);
				}
			}
		}

		((TBoardLayoutCache)getEditor().getCurrentBoard().getGraphLayoutCache())
				.editAndRemove(nested, new Object[] { cell });
	}
}
