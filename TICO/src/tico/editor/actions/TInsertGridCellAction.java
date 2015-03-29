/*
 * File: TInsertGridCellAction.java
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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import tico.board.TBoardConstants;
import tico.board.TBoardLayoutCache;
import tico.board.components.TCell;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich inserts a grid cell into a grid.
 * 
 * @author Pablo Muñoz
 * @version 1.1 May 09, 2009
 */
public class TInsertGridCellAction extends TEditorAbstractAction {
	// The grid in which the new TGridCell is going to be deleted.
	private TGrid grid;

	// The cell that is going to be inserted in the grid.
	private TCell cell;

	/**
	 * Constructor of the TInsertGridCellAction.
	 * 
	 * @param editor The boards' editor
	 * @param cell The cell to insert 
	 * @param grid The grid in which the cell is going to be inserted.
	 */
	public TInsertGridCellAction(TEditor editor, TCell cell, TGrid grid) {
		super(editor, TBoardConstants.getId(grid.getAttributes()));
		this.cell = cell;
		this.grid = grid;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		ArrayList options = new ArrayList();
		int rows, columns;
		int order = TBoardConstants.getOrder(grid.getAttributes());

		// First of all we must make a list of the possible places in which
		// the TGridCell can be inserted. This places depend on the order
		// of the grid
		switch (order) {
		case TGrid.COLUMNS:
			// In a grid ordered by columns the grid cell can be placed at
			// the end of any of its columns or as the first cell of a new one
			rows = grid.getLastColumnRow(1);
			for (int i = 1; i <= rows; i++)
				options.add(new TPoint(grid.getLastRowColumn(i) + 1, i));
			options.add(new TPoint(1, rows + 1));
			break;
		case TGrid.ROWS:
			// In a grid ordered by rows the grid cell can be placed at
			// the end of any of its rows or as the first cell of a new one
			columns = grid.getLastRowColumn(1);
			for (int i = 1; i <= columns; i++)
				options.add(new TPoint(i, grid.getLastColumnRow(i) + 1));
			options.add(new TPoint(columns + 1, 1));
			break;
		default:
			// In a grid custom or sequential ordered can be placed at the end
			// of any column or any row
			columns = grid.getLastRowColumn(1);
			for (int i = 1; i <= columns; i++)
				options.add(new TPoint(i, grid.getLastColumnRow(i) + 1));
			rows = grid.getLastColumnRow(1);
			for (int i = 1; i <= rows; i++)
				options.add(new TPoint(grid.getLastRowColumn(i) + 1, i));
		}

		// We let the user to chose the place of the cell.
		TPoint option = (TPoint)JOptionPane.showInputDialog(editor,
				TLanguage.getString("TInsertGridCellAction.CHOOSE_POSITION"),
				TLanguage.getString("TInsertGridCellAction.POSITION"),
				JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options
						.get(0));

		// Check if the user has chose a position or has press Cancel button
		if (option != null) {
			int newCol, newRow;
			TGridCell newCell;

			// Create the new grid cell in the chossen possition
			newCol = (int)option.getY();
			newRow = (int)option.getX();
			newCell = new TGridCell(newRow, newCol);
			Map newCellAttributes = (Map)cell.getAttributes().clone();
			TBoardConstants.setBrowseable(newCellAttributes, false);
			
			newCell.getAttributes().applyMap(newCellAttributes);
			TBoardConstants.setColumn(newCell.getAttributes(), newCol);
			TBoardConstants.setRow(newCell.getAttributes(), newRow);

			((TBoardLayoutCache)getEditor().getCurrentBoard()
					.getGraphLayoutCache()).removeAndInsertGroup(
					new Object[] { cell }, grid, new Object[] { newCell });
		}
	}

	private class TPoint extends Point {
		public TPoint(int x, int y) {
			super(x, y);
		}

		public String toString() {
			return "(" + (int)getX() + "," + (int)getY() + ")";
		}
	}
}
