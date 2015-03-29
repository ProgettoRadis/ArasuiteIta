/*
 * File: TGridCellRenderer.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 6, 2006
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

package tico.board.componentredenerer;

import java.util.Map;

import org.jgraph.graph.CellView;

import tico.board.TBoardConstants;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;

/**
 * <code>Swing</code> which implements the <code>TGridCell</code> visualization.
 * Its equal to <code>TCellRendered</code> but modifies the id to show its
 * parent grid order if its in the position (1,1).
 * 
 * @author bleras
 * @version 0.1 Aug 6, 2006
 */
public class TGridCellRenderer extends TCellRenderer {
	/* (non-Javadoc)
	 * @see org.jgraph.graph.VertexRenderer#installAttributes(org.jgraph.graph.CellView)
	 */
	protected void installAttributes(CellView view) {
		super.installAttributes(view);
		// Initialize id prefix
		String idOrder = "";
		// Get TGridCell attributes
		Map map = view.getAllAttributes();
		// If is the first cell (1,1) of the grid
		if ((TBoardConstants.getColumn(map) == 1)
				&& (TBoardConstants.getRow(map) == 1)) {
			// Add an arrow character that symbolizes the grid order
			switch (TBoardConstants
					.getOrder(((TGrid)((TGridCell)view.getCell()).getParent())
							.getAttributes())) {
			case TGrid.COLUMNS:
				idOrder = "↓ ";
				break;
			case TGrid.ROWS:
				idOrder = "→ ";
				break;
			case TGrid.CUSTOM:
				idOrder = "↺ ";
			}
		}
		// Append the string in the beginning of the grid cell id
		id = idOrder + id;
	}

}
