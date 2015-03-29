/*
 * File: TBoardDeleteAction.java
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

import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich deletes the editor selected board.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardDeleteAction extends TEditorAbstractAction {
	/**
	 * Constructor for TBoardDeleteAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TBoardDeleteAction(TEditor editor) {
		super(editor, TLanguage.getString("TBoardDeleteAction.NAME"),
				TResourceManager.getImageIcon("board-delete-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		// Check if a board is selected
		if (getEditor().getCurrentBoard() == null) {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TBoardDeleteAction.NO_BOARD_ERROR"),
					TLanguage.getString("WARNING") + "!",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// Check if the board to delete is the initial board
		if (getEditor().getProject().getInitialBoard().equals(getEditor().getCurrentBoard())){
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TBoardDeleteAction.INITIAL_BOARD"),
					TLanguage.getString("WARNING") + "!",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
	
		// Check if the user really want to delete the board
		if (!(JOptionPane.showConfirmDialog(getEditor(),
				TLanguage.getString("TBoardDeleteAction.CONFIRM_DELETE_BEGIN")
					+ " " + editor.getCurrentBoardContainer() + ",\n" +
					TLanguage.getString("TBoardDeleteAction.CONFIRM_DELETE_END")
					+ "\n" + TLanguage.getString("TBoardDeleteAction.CONFIRM_DELETE_QUESTION"),
					TLanguage.getString("TBoardDeleteAction.CONFIRM_DELETE_DELETE"),
				JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION))
			getEditor().removeCurrentBoard();
	}
}
