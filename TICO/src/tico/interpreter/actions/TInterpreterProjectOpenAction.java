/*
 * File: TInterpreterProjectOpenAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez
 * 
 * Date:	May-2006 
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

package tico.interpreter.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tico.components.resources.ProjectFilter;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TProjectHandler;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterProject;

/**
 * Action which opens a project from a file and set it to the interpreter.
 * 
 * @author Antonio Rodríguez
 * @version 1.0 Nov 20, 2006
 */

public class TInterpreterProjectOpenAction extends TInterpreterAbstractAction{
	
	private static File defaultDirectory = null;
	
	/**
	 * Constructor for TInterpreterProjectOpenAction.
	 * 
	 * @param interpreter The boards' interpreter
	 */
	public TInterpreterProjectOpenAction(TInterpreter interpreter) {
		super(interpreter, TLanguage.getString("TProjectOpenAction.NAME"),
				TResourceManager.getImageIcon("archive-open-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(TLanguage.getString("TProjectOpenAction.OPEN_PROJECT"));
		if (!TSetup.getInterpreterHome().equals("")){
			defaultDirectory = new File(TSetup.getInterpreterHome());
		}
		fileChooser.setCurrentDirectory(defaultDirectory);
		fileChooser.addChoosableFileFilter(new ProjectFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);

		// Checks if the user has chosen a file
		int returnValue = fileChooser.showOpenDialog((Component)null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			// Waiting cursor while the project is being opened
			interpreter.TIntepreterWaitingCursor();
			// Delete accumulated cells
			TInterpreter.accumulatedCells.removeAll();
			TInterpreter.accumulatedCells.updateUI();
			// Get the chosen file			
			// Set its directory as next first JFileChooser directory		
			File selectedFile = fileChooser.getSelectedFile();
			defaultDirectory = selectedFile.getParentFile();
			// Set the editor home directory
			TSetup.setInterpreterHome(selectedFile.getParent().toString());
			try {
				getInterpreter().deleteProject();
				// Load the project				
				TInterpreterProject project = TProjectHandler.loadProjectInterpreter(selectedFile);
				getInterpreter().setProject(project);
				TInterpreter.setEnabledActions(true);
				getInterpreter().setTitle(TInterpreter.DEFAULT_TITLE + " - " + project.getName());
				// Set the default cursor
				interpreter.TInterpreterRestoreCursor();
			} catch (Exception ex) {
				// If the import fails show an error message
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TProjectOpenAction.OPEN_ERROR"),
						TLanguage.getString("ERROR") + "!",
						JOptionPane.ERROR_MESSAGE);
				interpreter.TInterpreterRestoreCursor();
			}
						
		}
	}
}
