/* File: TProjectValidatonAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Mar 21, 2007
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

package tico.rules.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.editor.actions.TEditorAbstractAction;
import tico.editor.actions.TProjectSaveAction;
import tico.rules.dialogs.TValidationDialog;

/**
 * Action which opens the project validation dialog.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Mar 21, 2007
 */
public class TProjectValidationAction extends TEditorAbstractAction {
	/**
	 * Constructor for TProjectValidationAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TProjectValidationAction(TEditor editor) {
		super(editor, TLanguage.getString("TProjectValidationAction.NAME"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int choosenOption = JOptionPane.NO_OPTION;
		// Ask the user if wants to save the project before validating it
		if (getEditor().isModified())
			choosenOption = JOptionPane.showConfirmDialog(null,
					TLanguage.getString("TProjectValidationAction.ASK_SAVE") + "\n" +
					TLanguage.getString("TProjectValidationAction.ASK_SAVE_QUESTION"),
					TLanguage.getString("TProjectValidationAction.MODIFIED_PROJECT"),
					JOptionPane.YES_NO_CANCEL_OPTION);
		// If cancel, exit
		if (choosenOption == JOptionPane.CANCEL_OPTION)
			return;
		// If yes, run the project save action
		if (choosenOption == JOptionPane.YES_OPTION)
			new TProjectSaveAction(getEditor()).actionPerformed(e);

		new TValidationDialog(getEditor(), getEditor().getProject());
	}
}
