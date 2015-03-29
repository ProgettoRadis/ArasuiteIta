/*
 * File: TAddGridRowAction.java
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

import org.jgraph.graph.AttributeMap;

import tico.board.TBoardConstants;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich adds a new full grid cells row to a grid of the previous
 * last row length.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TAddGridRowAction extends TEditorAbstractAction {
	// The grid in which the new TGrid row is going to be inserted.
	private TGrid grid;

	/**
	 * Constructor for TAddGridRowAction.
	 * 
	 * @param editor The boards' editor
	 * @param grid The grid in which the new TGrid row is going to be inserted.
	 */
	public TAddGridRowAction(TEditor editor, TGrid grid) {
		super(editor, TLanguage.getString("TAddGridRowAction.NAME"));
		this.grid = grid;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Number of cells to add
		int numNewCells = grid.getColumnsCount();
		// Array which will contain all the new cells
		TGridCell[] newCells = new TGridCell[numNewCells];
		// Default attributeMap
		AttributeMap currentAttributes = editor.getCurrentAttributes();

		// Last row of each column
		int lastColumnRow;
		// Dimension of the new cell, will correspond with de size of the column
		// previous cell size in that column
		Rectangle2D newCellDimension;
		// Vertical distance of the new cell to the begining last row cell.
		// This value is the vertical gap between the rows cells lastColumnRow
		// and lastColumnRow - 1 plus the width of the lastColumnRow cell.
		// If lastColumnRow don't exists is used the DEFAULT_VERTICAL_GAP
		double verticalDistance;

		// For each new cell
		for (int i = 1; i <= numNewCells; i++) {
			// Get the cell row
			lastColumnRow = grid.getLastRowColumn(i);
			// Create the cell and apply the default attributes
			newCells[i-1] = new TGridCell(lastColumnRow + 1, i);
			newCells[i-1].getAttributes().applyMap(currentAttributes);
			// Get the bounds of the last row cell
			newCellDimension = TBoardConstants.getBounds(grid.getCell(
					lastColumnRow, i).getAttributes());
			// If lastColumnRow cell don't exists in that row
			if (grid.getCell(lastColumnRow - 1, i) == null)
				// Use the DEFAULT_VERTICAL_GAP
				verticalDistance = TGrid.DEFAULT_VERTICAL_GAP
						+ newCellDimension.getHeight();
			else {
				// Get the horizontal gap between lastColumnRow and
				// lastColumnRow-1 cells
				Rectangle2D previousRowCell = (Rectangle2D) TBoardConstants
						.getBounds(grid.getCell(lastColumnRow - 1, i)
								.getAttributes());
				verticalDistance = newCellDimension.getY()
						- previousRowCell.getY()
						- previousRowCell.getHeight()
						+ newCellDimension.getHeight();
			}
			// Set the bounds to the cell
			TBoardConstants.setBounds(newCells[i-1].getAttributes(),
					new Rectangle.Double(newCellDimension.getX(),
							newCellDimension.getY() + verticalDistance,
							newCellDimension.getWidth(), newCellDimension
									.getHeight()));
		}
		// Insert all the new cells
		getEditor().getCurrentBoard().getGraphLayoutCache().insertGroup(grid,
				newCells);
	}
}
