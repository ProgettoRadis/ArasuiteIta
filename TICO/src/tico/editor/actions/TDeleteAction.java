/*
 * File: TDeleteAction.java
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

import tico.board.components.TGridCell;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action which deletes the editor selected components.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TDeleteAction extends TEditorAbstractAction {
	/**
	 * Constructor for TDeleteAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TDeleteAction(TEditor editor) {
		super(editor, TLanguage.getString("TDeleteAction.NAME"),
				TResourceManager.getImageIcon("edit-delete-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Determines if the advice has to be shown.
		boolean adviceDeletion = false;
		// Determines if the cells have to be deleted
		boolean delete = true;
		
		// Get all selected cells
		Object[] cells = getEditor().getCurrentBoard().getSelectionCells();
		// Unselect any TGridCell, each TGrid with a TGridCell selected will be deleted
	
//cambio de funcionamiento a TGrid a simple conjunto de celdas
/*
		for (int i = 0; i < cells.length; i++) if (cells[i] instanceof TGridCell) {
			getEditor().getCurrentBoard().removeSelectionCell(cells[i]);
			adviceDeletion = true;
		}
		
		if (adviceDeletion) {
			if (JOptionPane.showConfirmDialog(getEditor(),
		             TLanguage.getString("TDeleteAction.ASK_DELETE_GRIDS_BEGIN") + "\n" +
		             TLanguage.getString("TDeleteAction.ASK_DELETE_GRIDS_END"),
		             TLanguage.getString("TDeleteAction.DELETION"),
		             JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
				delete = false;
		}
*/		
		if (delete) {
			// Change de new selection
			cells = getEditor().getCurrentBoard().getSelectionCells();
			if (cells != null) {
				cells = getEditor().getCurrentBoard().getDescendants(cells);
				getEditor().getCurrentBoard().getGraphLayoutCache().remove(cells);
			}
		}
	}
}
