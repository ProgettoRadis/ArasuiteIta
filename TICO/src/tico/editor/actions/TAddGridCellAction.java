/*
 * File: TAddGridCellAction.java
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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import tico.board.TBoardConstants;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich adds a new grid cell in a grid asking its position.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TAddGridCellAction extends TEditorAbstractAction {
	// The grid in which the new TGridCell is going to be inserted.
	private TGrid grid;

	/**
	 * Constructor of the TAddGridCellAction.
	 * 
	 * @param editor The boards' editor
	 * @param grid The grid in which the new <code>TGridCell</code> is going to
	 * be inserted.
	 */
	public TAddGridCellAction(TEditor editor, TGrid grid) {
		super(editor, TLanguage.getString("TAddGridCellAction.NAME"));
		this.grid = grid;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		ArrayList options = new ArrayList();
		int rows, columns;
		int order = TBoardConstants.getOrder(grid.getAttributes());
		Rectangle2D bounds;
		// Default attributeMap
		Map currentAttributes = editor.getCurrentAttributes();

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
		TPoint option = (TPoint) JOptionPane.showInputDialog(getEditor(),
				TLanguage.getString("TAddGridCellAction.CHOOSE_POSITION"),
				TLanguage.getString("TAddGridCellAction.POSITION"),
				JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options
						.get(0));

		// Check if the user has chose a position or has press Cancel button
		if (option != null) {
			int newCol, newRow;
			TGridCell newCell;
			Rectangle2D newCellDimension;
			double horizontalDistance, verticalDistance;

			// Create the new grid cell in the chossen possition
			newCol = (int) option.getY();
			newRow = (int) option.getX();
			newCell = new TGridCell(newRow, newCol);

			// Calculate the new cell bounds. The size and gap with the previous
			// cell depends on the order of the grid
			if ((order == TGrid.COLUMNS) ||
			   ((order == TGrid.CUSTOM) && (grid.getLastColumnRow(newRow) == 0)) ||
			   ((order == TGrid.SEQUENTIAL) && (grid.getLastColumnRow(newRow) == 0))) {
				// If the order is columns or if its custom and is the first
				// cell of a row we get the position of the previous column cell
				// and the vertical gap between it and the previous one
				int newCellColumnDimension = grid.getLastRowColumn(newCol);
				if (newCellColumnDimension > 0) {
					newCellDimension = TBoardConstants.getBounds(grid.getCell(
							newCellColumnDimension, newCol).getAttributes());
					if (newCellColumnDimension == 1)
						// If there is only one cell in the column the use the
						// default vertical gap
						verticalDistance = TGrid.DEFAULT_VERTICAL_GAP
								+ newCellDimension.getHeight();
					else {
						Rectangle2D previousRowCell = (Rectangle2D) TBoardConstants
								.getBounds(grid.getCell(
										newCellColumnDimension - 1, newCol)
										.getAttributes());
						verticalDistance = newCellDimension.getY()
								- previousRowCell.getY()
								- previousRowCell.getHeight()
								+ newCellDimension.getHeight();
					}
					bounds = new Rectangle.Double(newCellDimension.getX(),
							newCellDimension.getY() + verticalDistance,
							newCellDimension.getWidth(), newCellDimension
									.getHeight());
				} else {
					// If it is the first cell in that column we take the
					// default grid cell horizontal gap and the size of the
					// first cell of the previous column
					newCellDimension = TBoardConstants.getBounds(grid.getCell(
							newRow, newCol - 1).getAttributes());
					horizontalDistance = TGrid.DEFAULT_HORIZONTAL_GAP
							+ newCellDimension.getWidth();
					bounds = new Rectangle.Double(newCellDimension.getX()
							+ horizontalDistance, newCellDimension.getY(),
							newCellDimension.getWidth(), newCellDimension
									.getHeight());
				}
			} else {
				// Otherwise we get the position of the previous row cell and
				// the horizontal gap between it and the previous one
				int newCellRowDimension = grid.getLastColumnRow(newRow);
				if (newCellRowDimension > 0) {
					newCellDimension = TBoardConstants.getBounds(grid.getCell(
							newRow, newCellRowDimension).getAttributes());
					if (newCellRowDimension == 1)
						// If there is only one cell in the column the use the
						// default horizontal gap
						horizontalDistance = TGrid.DEFAULT_HORIZONTAL_GAP
								+ newCellDimension.getWidth();
					else {
						Rectangle2D previousRowCell = (Rectangle2D) TBoardConstants
								.getBounds(grid.getCell(newRow,
										newCellRowDimension - 1)
										.getAttributes());
						horizontalDistance = newCellDimension.getX()
								- previousRowCell.getX()
								- previousRowCell.getWidth()
								+ newCellDimension.getWidth();
					}
					bounds = new Rectangle.Double(newCellDimension.getX()
							+ horizontalDistance, newCellDimension.getY(),
							newCellDimension.getWidth(), newCellDimension
									.getHeight());
				} else {
					// If it is the first cell in that row we take the default
					// grid cell vertical gap and the size of the first cell of
					// the previous row
					newCellDimension = TBoardConstants.getBounds(grid.getCell(
							newRow - 1, newCol).getAttributes());
					verticalDistance = TGrid.DEFAULT_VERTICAL_GAP
							+ newCellDimension.getHeight();
					bounds = new Rectangle.Double(newCellDimension.getX(),
							newCellDimension.getY() + verticalDistance,
							newCellDimension.getWidth(), newCellDimension
									.getHeight());
				}
			}
			// Apply attributes and bounds
			newCell.getAttributes().applyMap(currentAttributes);
			TBoardConstants.setBounds(newCell.getAttributes(), bounds);
			// Insert the cell
			getEditor().getCurrentBoard().getGraphLayoutCache().insertGroup(
					grid, new Object[] { newCell });
		}
	}

	private class TPoint extends Point {
		public TPoint(int x, int y) {
			super(x, y);
		}

		public String toString() {
			return "(" + (int) getX() + "," + (int) getY() + ")";
		}
	}
}
