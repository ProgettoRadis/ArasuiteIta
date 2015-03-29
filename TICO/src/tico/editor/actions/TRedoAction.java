/*
 * File: TRedoAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jan 30, 2006
 *  
 * Based on: @(#)EditRedo.java
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

import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich redoes the last undone current editor board edit.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TRedoAction extends TEditorAbstractAction {
	/**
	 * Constructor for TRedoAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TRedoAction(TEditor editor) {
		super(editor, TLanguage.getString("TRedoAction.NAME"),
				TResourceManager.getImageIcon("edit-redo-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			getEditor().getCurrentBoardContainer().getUndoManager().redo(
					getEditor().getCurrentBoard().getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			getEditor().updateHistoryButtons();
		}
	}
}
