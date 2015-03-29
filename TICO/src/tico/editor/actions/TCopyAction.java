/*
 * File: TCopyAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jan 30, 2006
 *  
 * Based on: @(#)EditCopy.java
 *           Copyright (c) 2001-2005, Gaudenz Alder
 *           From JGraphPad 5.7.3.1.1
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

import javax.swing.TransferHandler;

import tico.board.components.TGridCell;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich copies the editor selected components into the clipboard.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TCopyAction extends TEditorAbstractAction {
	/**
	 * Constructor for TCopyAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TCopyAction(TEditor editor) {
		super(editor, TLanguage.getString("TCopyAction.NAME"),
				TResourceManager.getImageIcon("edit-copy-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Get all selected cells
		Object[] cells = getEditor().getCurrentBoard().getSelectionCells();
		// Unselect any TGridCell, each TGrid with a TGridCell selected will be copied
		for (int i = 0; i < cells.length; i++) if (cells[i] instanceof TGridCell)
			getEditor().getCurrentBoard().removeSelectionCell(cells[i]);

		TransferHandler.getCopyAction().actionPerformed(
			new ActionEvent(getEditor().getCurrentBoard(), e.getID() , e.getActionCommand()));

	}
}
