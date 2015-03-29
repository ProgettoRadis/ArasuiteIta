/*
 * File: TComponentDialogFactory.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 10, 2006
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

package tico.editor.dialogs;

import tico.board.components.TCell;
import tico.board.components.TComponent;
import tico.board.components.TControllerCell;
import tico.board.components.TGridCell;
import tico.board.components.TLabel;
import tico.board.components.TLine;
import tico.board.components.TOval;
import tico.board.components.TRectangle;
import tico.board.components.TRoundRect;
import tico.board.components.TTextArea;
import tico.editor.TBoardContainer;

/**
 * Defines the what component dialog edits what component class.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TComponentDialogFactory {
	// The TBoardContainer which uses this dialog factory
	private TBoardContainer boardContainer;
	
	/**
	 * Creates a new <code>TComponentDialogFactory</code> for the specified
	 * <code>boardContainer</code>
	 * 
	 * @param boardContainer The specified <code>boardContainer</code>
	 */
	public TComponentDialogFactory(TBoardContainer boardContainer) {
		this.boardContainer = boardContainer;
	}
	
	/**
	 * Constructs a <code>TComponentDialog</code> for the specified <code>component</code>.
	 * 
	 * @param component The specified <code>component</code>
	 */
	public void createComponentDialog(TComponent component) {
		if (component instanceof TCell)
			new TCellDialog(boardContainer, component);
		if (component instanceof TControllerCell)
			new TControllerCellDialog(boardContainer, component);
		if (component instanceof TGridCell)
			new TGridCellDialog(boardContainer, component);
		if (component instanceof TTextArea)
			new TTextAreaDialog(boardContainer, component);
		if (component instanceof TLabel)
			new TLabelDialog(boardContainer, component);
		if (component instanceof TRectangle)
			new TPolygonDialog(boardContainer, component);
		if (component instanceof TRoundRect)
			new TPolygonDialog(boardContainer, component);
		if (component instanceof TOval)
			new TPolygonDialog(boardContainer, component);
		if (component instanceof TLine)
			new TLineDialog(boardContainer, component);
	}
}
