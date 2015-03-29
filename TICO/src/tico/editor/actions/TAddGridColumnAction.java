/*
 * File: TAddGridColumnAction.java
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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import tico.board.TBoardConstants;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich adds a new full grid cells column to a grid of the previous
 * last column length.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TAddGridColumnAction extends TEditorAbstractAction {
	// The grid in which the new TGrid column is going to be inserted.
	private TGrid grid;

	/**
	 * Constructor for TAddGridColumnAction.
	 * 
	 * @param editor The boards' editor
	 * @param grid The grid in which the new TGrid column is going to be
	 *            inserted.
	 */
	public TAddGridColumnAction(TEditor editor, TGrid grid) {
		super(editor, TLanguage.getString("TAddGridColumnAction.NAME"));
		this.grid = grid;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Number of cells to add
		int numNewCells = grid.getRowsCount();
		// Array which will contain all the new cells
		TGridCell[] newCells = new TGridCell[numNewCells];
		// Default attributeMap
		Map currentAttributes = editor.getCurrentAttributes();

		// Last column of each row
		int lastRowColumn;
		// Dimension of the new cell, will correspond with de size of the row
		// previous cell size
		Rectangle2D newCellDimension;
		// Horizontal distance of the new cell to the begining last row cell.
		// This value is the horizontal gap between the rows cells lastRowColumn
		// and lastRowColumn - 1 plus the width of the lastRowColumn cell.
		// If lastRowColumn don't exists is used the DEFAULT_HORIZONTAL_GAP
		double horizontalDistance;

		// For each new cell
		for (int i = 1; i <= numNewCells; i++) {
			// Get the cell column
			lastRowColumn = grid.getLastColumnRow(i);
			// Create the cell
			newCells[i - 1] = new TGridCell(i, lastRowColumn + 1);
			newCells[i - 1].getAttributes().applyMap(currentAttributes);
			// Get the bounds of the last column cell
			newCellDimension = TBoardConstants.getBounds(grid.getCell(i,
					lastRowColumn).getAttributes());
			// If lastRowColumn cell don't exists in that row
			if (grid.getCell(i, lastRowColumn - 1) == null)
				// Use the DEFAULT_HORIZONTAL_GAP
				horizontalDistance = TGrid.DEFAULT_HORIZONTAL_GAP
						+ newCellDimension.getWidth();
			else {
				// Get the horizontal gap between lastRowClumn and
				// lastRowColumn-1 cells
				Rectangle2D previousRowCell = (Rectangle2D) TBoardConstants
						.getBounds(grid.getCell(i, lastRowColumn - 1)
								.getAttributes());
				horizontalDistance = newCellDimension.getX()
						- previousRowCell.getX()
						- previousRowCell.getWidth()
						+ newCellDimension.getWidth();
			}
			// Set the bounds to the cell
			TBoardConstants.setBounds(newCells[i - 1].getAttributes(),
					new Rectangle.Double(newCellDimension.getX()
							+ horizontalDistance, newCellDimension.getY(),
							newCellDimension.getWidth(), newCellDimension
									.getHeight()));
		}
		// Insert all the new cells
		getEditor().getCurrentBoard().getGraphLayoutCache().insertGroup(grid,
				newCells);
	}
}
