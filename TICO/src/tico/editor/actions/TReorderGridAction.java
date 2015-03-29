/*
 * File: TReorderGridAction.java
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
import java.util.Hashtable;
import java.util.Map;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoardConstants;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich reorder all the selected grid cells positions and sizes making
 * all the cells have the same size of the first cell and making equal its
 * distances with the prvious one.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TReorderGridAction extends TEditorAbstractAction {
	// The grid in which tis goinf to be reordered
	private TGrid grid;

	/**
	 * Constructor for TCopyAction.
	 * 
	 * @param editor The boards' editor
	 * @param grid The grid in which tis goinf to be reordered.
	 */
	public TReorderGridAction(TEditor editor, TGrid grid) {
		super(editor, TLanguage.getString("TReorderGridAction.NAME"));
		this.grid = grid;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Map nested = new Hashtable();

		TGridCell currentCell, followingColumnCell, followingRowCell;
		Rectangle2D referenceBounds, followingColumnBounds, followingRowBounds;
		double cellWidth, cellHeight;
		double cellHorizontalGap, cellVerticalGap;

		int rowsCount, columnsCount;

		// Get the maximum column value and tha maximum row value within all
		// grid's cells
		rowsCount = grid.getRowsCount();
		columnsCount = grid.getColumnsCount();

		// Get the first cell (1,1) as a reference for the other cells placement
		referenceBounds = TBoardConstants.getBounds(grid.getCell(1, 1)
				.getAttributes());

		// All cell size will be the same as the reference one
		cellWidth = referenceBounds.getWidth();
		cellHeight = referenceBounds.getHeight();

		// If there is any following cell in the row, the horizontal gap will be
		// the gap between the first and the following one. If not we are going
		// to use the DEFAULT_HORIZONTAL_GAP value.
		followingRowCell = grid.getFollowingRowCell(1, 2);
		if (followingRowCell != null) {
			followingRowBounds = TBoardConstants.getBounds(followingRowCell
					.getAttributes());
			cellHorizontalGap = followingRowBounds.getX()
					- (referenceBounds.getX() + referenceBounds.getWidth());
		} else
			cellHorizontalGap = TGrid.DEFAULT_HORIZONTAL_GAP;

		// If there is any following cell in the column, the vertical gap will
		// be
		// the gap between the first and the following one. If not we are going
		// to use the DEFAULT_VERTICAL_GAP value.
		followingColumnCell = grid.getFollowingColumnCell(1, 2);
		if (followingColumnCell != null) {
			followingColumnBounds = TBoardConstants
					.getBounds(followingColumnCell.getAttributes());
			cellVerticalGap = followingColumnBounds.getY()
					- (referenceBounds.getY() + referenceBounds.getHeight());
		} else
			cellVerticalGap = TGrid.DEFAULT_VERTICAL_GAP;

		// We check all the possible cell position
		for (int row = 1; row <= rowsCount; row++) {
			for (int col = 1; col <= columnsCount; col++) {
				currentCell = grid.getCell(row, col);
				// If the cell exists, calculate the new bounds
				if (currentCell != null) {
					Map attributeMap = new AttributeMap();
					// New bounds are calculated with whe size and the gaps
					// previously calculated and the cell's position
					TBoardConstants.setBounds(attributeMap,
							new Rectangle.Double(referenceBounds.getX()
									+ (cellHorizontalGap + cellWidth)
									* (col - 1), referenceBounds.getY()
									+ (cellVerticalGap + cellHeight)
									* (row - 1), cellWidth, cellHeight));
					nested.put(currentCell, attributeMap);
				}
			}
		}
		getEditor().getCurrentBoard().getGraphLayoutCache().edit(nested);
	}
}
