/*
 * File: TCellMarqueeHandler.java
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

import java.awt.geom.Rectangle2D;

import tico.board.TBoardConstants;
import tico.board.components.TCell;
import tico.board.components.TComponent;
import tico.editor.TBoardContainer;

/**
 * Marquee handler which allows to insert, in a <code>boardContainer</code>
 * board, a new <code>cell</code> of the selected dimension.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TCellMarqueeHandler extends TComponentMarqueeHandler {
	/**
	 * Creates a new <code>TCellMarqueeHandler</code> for the specified
	 * <code>boardContainer</code>.
	 * 
	 * @param boardContainer The specified <code>boardContainer</code>
	 */
	public TCellMarqueeHandler(TBoardContainer boardContainer) {
		super(boardContainer);
	}

	/* (non-Javadoc)
	 * @see tico.editor.handler.TComponentMarqueeHandler#createDefaultComponent(java.awt.geom.Rectangle2D)
	 */
	protected TComponent createDefaultComponent(Rectangle2D bounds) {
		TComponent cell = new TCell();

		cell.getAttributes().applyMap(
				getBoardContainer().getEditor().getCurrentAttributes());

		TBoardConstants.setBounds(cell.getAttributes(), bounds);

		return cell;
	}
}
