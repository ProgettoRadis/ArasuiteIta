/*
 * File: TBoardNewAction.java
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

import javax.swing.JOptionPane;

import tico.board.TBoard;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich adds a new board for the editor project.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardNewAction extends TEditorAbstractAction {
	/**
	 * Constructor for TBoardNewAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TBoardNewAction(TEditor editor) {
		super(editor, TLanguage.getString("TBoardNewAction.NAME"),
				TResourceManager.getImageIcon("board-new-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Check if exists a project
		if (getEditor().getProject() == null) {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TBoardNewAction.NO_PROJECT_ERROR"),
					TLanguage.getString("WARNING") + "!",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// Create the new board in the current project
		getEditor().getProject().addBoard(new TBoard());
	}
}
