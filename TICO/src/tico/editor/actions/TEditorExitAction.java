/*
 * File: TEditorExitAction.java
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
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TEditor;
import tico.editor.TFileHandler;

/**
 * Action which exists from the editor application.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TEditorExitAction extends TEditorAbstractAction {
	/**
	 * Constructor for TEditorExitAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TEditorExitAction(TEditor editor) {
		super(editor, TLanguage.getString("TProjectExitAction.NAME"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int choosenOption = JOptionPane.NO_OPTION;
		// Ask the user if wants to save the project before opening the new one
		if (getEditor().isModified())
			choosenOption = JOptionPane.showConfirmDialog(null,
					TLanguage.getString("TProjectExitAction.ASK_SAVE") + "\n" +
					TLanguage.getString("TProjectExitAction.ASK_SAVE_QUESTION"),
					TLanguage.getString("TProjectExitAction.MODIFIED_PROJECT"),
					JOptionPane.YES_NO_CANCEL_OPTION);
		// If cancel, exit 
		if (choosenOption == JOptionPane.CANCEL_OPTION) {
			return;
		}
		// If yes, run the project save action
		if (choosenOption == JOptionPane.YES_OPTION)
			new TProjectSaveAction(getEditor()).actionPerformed(e);
		// Delete the project
		getEditor().deleteProject();
		TFileHandler.deleteCurrentDirectory();
		try {
			TSetup.save();
		} catch (Exception ex) {
		}
		getEditor().dispose();	
	}
}
