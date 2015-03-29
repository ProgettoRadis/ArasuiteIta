/*
 * File: TChangeGridOrderAction.java
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
import java.util.Map;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoardConstants;
import tico.board.components.TGrid;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich changes the order of a grid.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TChangeGridOrderAction extends TEditorAbstractAction {
	// The grid which is going to reordered.
	private TGrid grid;
	
	// The grid which is going to reordered.
	private int order;
	
	/**
	 * Constructor for TChangeGridOrderAction.
	 * 
	 * @param editor The boards' editor
	 * @param grid The grid which is going to reordered.
	 * @param order The new order of the grid.
	 */
	public TChangeGridOrderAction(TEditor editor, TGrid grid, int order) {
		super(editor, TLanguage.getString("TChangeGridOrderAction.NAME") +
				" " + ((order==TGrid.COLUMNS)?
						TLanguage.getString("TChangeGridOrderAction.COLUMNS"):
						TLanguage.getString("TChangeGridOrderAction.ROWS")));
		this.grid = grid;
		this.order = order;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Map nested = new Hashtable();
		
		int prevOrder = TBoardConstants.getOrder(grid.getAttributes());
		
		// If the order changes
		if ((order == TGrid.COLUMNS) && (prevOrder != order))
			// And changes to columns, get new order
			nested = grid.reorderColumns();
		else if ((order == TGrid.ROWS) && (prevOrder != order))
			// And changes to rows, get new order
			nested = grid.reorderRows();
		
		// Is the order has to been changed, change it on the TGrid
		if (prevOrder != order) {
			Map attributeMap = new AttributeMap();
			TBoardConstants.setOrder(attributeMap,order);
			nested.put(grid,attributeMap);
		}
		
		// If there is anything to change, change it
		if (!nested.isEmpty())
			getEditor().getCurrentBoard().getGraphLayoutCache()
				.edit(nested, null, null, null);
	}
}
