/*
 * File: TGridMarqueeHandler.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: May 23, 2006
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

package tico.editor.handler;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.editor.TBoardContainer;

/**
 * Marquee handler which allows to insert, in a <code>boardContainer</code>
 * board, a new <code>grid</code> of the selected dimension and which contains
 * a specified number of <code>gridCell</code>s rows and columns.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TGridMarqueeHandler extends TComponentMarqueeHandler {
	// New grid columns and rows
	private int columns;
	private int rows;

	/**
	 * Creates a new <code>TGridMarqueeHandler</code> for the specified
	 * <code>boardContainer</code> and <code>dimension</code>.
	 * 
	 * @param boardContainer The specified <code>boardContainer</code>
	 * @param dimension The <code>dimension</code> that determines the number of
	 * the new <code>grid</code> rows and columns
	 */
	public TGridMarqueeHandler(TBoardContainer boardContainer, Dimension dimension) {
		this(boardContainer, dimension.height, dimension.width);
	}

	/**
	 * Creates a new <code>TGridMarqueeHandler</code> for the specified
	 * <code>boardContainer</code>, <code>rows</code> and <code>columns</code>.
	 * 
	 * @param boardContainer The specified <code>boardContainer</code>
	 * @param rows The specified <code>rows</code>
	 * @param columns The specified <code>columns</code>
	 */
	public TGridMarqueeHandler(TBoardContainer boardContainer, int rows,
			int columns) {
		super(boardContainer);

		this.columns = columns;
		this.rows = rows;
	}

	/* (non-Javadoc)
	 * @see tico.editor.handler.TComponentMarqueeHandler#createDefaultComponent(java.awt.geom.Rectangle2D)
	 */
	protected TComponent createDefaultComponent(Rectangle2D bounds) {
		double cellHeight, cellWidth;
		double cellHorizontalGap = TGrid.DEFAULT_HORIZONTAL_GAP;
		double cellVerticalGap = TGrid.DEFAULT_VERTICAL_GAP;

		TGridCell[] childs = new TGridCell[columns * rows];

		// Calculate cell height and width
		cellHeight = (bounds.getHeight() - ((rows - 1) * cellVerticalGap))
				/ rows;
		cellWidth = (bounds.getWidth() - ((columns - 1) * cellHorizontalGap))
				/ columns;

		// Current Y bounds value for the next cell
		double currentY = bounds.getY();

		// Create all the grid cells
		for (int i = 0; i < rows; i++) {
			// Current X bounds value for the next cell
			double currentX = bounds.getX();

			for (int j = 0; j < columns; j++) {
				childs[columns * i + j] = new TGridCell(i + 1, j + 1);

				// Apply the attributes
				childs[columns * i + j].getAttributes().applyMap(
						getBoardContainer().getEditor().getCurrentAttributes());
				TBoardConstants.setBounds(childs[columns * i + j]
						.getAttributes(), new Rectangle.Double(currentX,
						currentY, cellWidth, cellHeight));

				// Update current X bounds value
				currentX += cellWidth + cellHorizontalGap;
			}
			// Update current Y bounds value
			currentY += cellHeight + cellVerticalGap;
		}

		TGrid grid = new TGrid(childs);

		return grid;
	}
}
