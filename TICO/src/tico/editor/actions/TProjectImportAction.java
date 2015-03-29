/*
 * File: TProjectImportAction.java
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tico.components.resources.TFileUtils;
import tico.components.resources.TicoFilter;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.editor.TFileHandler;
import tico.editor.TProjectHandler;

/**
 * Action wich imports a project from a file to the current editor project.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TProjectImportAction extends TEditorAbstractAction {
	private static File defaultDirectory = TFileHandler.getDefaultImportDirectory();//null;

	/**
	 * Constructor for TProjectImportAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TProjectImportAction(TEditor editor) {
		super(editor, TLanguage.getString("TProjectImportAction.NAME"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Open a JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		// Customoze JFileChooser
		fileChooser.setDialogTitle(TLanguage.getString("TProjectImportAction.IMPORT"));
		fileChooser.setCurrentDirectory(defaultDirectory);
		fileChooser.addChoosableFileFilter(new TicoFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);

		// Checks if the user has chosen a file
		int returnValue = fileChooser.showOpenDialog((Component)null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			// Get the chosen file
			File selectedFile = fileChooser.getSelectedFile();
			// Set its directory as next first JFileChooser directory
			defaultDirectory = selectedFile.getParentFile();

			try {
				if (TFileUtils.isBoardFile(selectedFile)) {
					// A board has been selected
					getEditor().getProject().addBoard(
							TProjectHandler.loadBoard(selectedFile));
				} else if (TFileUtils.isProjectFile(selectedFile)) {
					// A project has been selected
					getEditor().getProject().addProject(
							TProjectHandler.loadProject(selectedFile));
				}
			} catch (Exception ex) {
				// If the import fails show an error message
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TProjectImportAction.IMPORT_ERROR"),
						TLanguage.getString("Error") + "!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
