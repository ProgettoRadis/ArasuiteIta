/*
 * File: TProjectOpenAction.java
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

import tico.components.resources.ProjectAndroidFilter;
import tico.components.resources.ProjectFilter;
import tico.components.resources.TFileUtils;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TEditor;
import tico.editor.TProjectHandler;

/**
 * Action wich opens a project from a file and set it to the editor.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TProjectOpenAction extends TEditorAbstractAction {
	private static File defaultDirectory = null;

	/**
	 * Constructor for TProjectOpenAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TProjectOpenAction(TEditor editor) {
		super(editor, TLanguage.getString("TProjectOpenAction.NAME"),
				TResourceManager.getImageIcon("archive-open-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int choosenOption = JOptionPane.NO_OPTION;
		// Ask the user if wants to save the project before opening the new one
		if (getEditor().isModified())
			choosenOption = JOptionPane.showConfirmDialog(null,
					TLanguage.getString("TProjectOpenAction.ASK_SAVE") + "\n" +
					TLanguage.getString("TProjectOpenAction.ASK_SAVE_QUESTION"),
					TLanguage.getString("TProjectOpenAction.MODIFIED_PROJECT"),
					JOptionPane.YES_NO_CANCEL_OPTION);
		// If cancel, exit
		if (choosenOption == JOptionPane.CANCEL_OPTION)
			return;
		// If yes, run the project save action
		if (choosenOption == JOptionPane.YES_OPTION)
			new TProjectSaveAction(getEditor()).actionPerformed(e);

		// Open the project
		// Open a JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		// Customize JFileChooser
		fileChooser.setDialogTitle(TLanguage.getString("TProjectOpenAction.OPEN_PROJECT"));
		if (!TSetup.getEditorHome().equals("")){
			defaultDirectory = new File(TSetup.getEditorHome());
		}
		fileChooser.setCurrentDirectory(defaultDirectory);
		
		ProjectFilter ticoFilter=new ProjectFilter();
		fileChooser.addChoosableFileFilter(ticoFilter);
		
		ProjectAndroidFilter androidFilter=new ProjectAndroidFilter();
		fileChooser.addChoosableFileFilter(androidFilter);
		
		
		fileChooser.setAcceptAllFileFilterUsed(false);
		if(TEditor.get_android_mode()){
			fileChooser.setFileFilter(androidFilter);
		}else{
			fileChooser.setFileFilter(ticoFilter);
		}

		// Checks if the user has chosen a file
		int returnValue = fileChooser.showOpenDialog((Component)null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			
			// Waiting cursor while the project is being opened
			getEditor().changeToWaitingCursor();
			
			// Get the chosen file
			File selectedFile = fileChooser.getSelectedFile();
			// Set its directory as next first JFileChooser directory
			defaultDirectory = selectedFile.getParentFile();
			// Set the editor home directory
			TSetup.setEditorHome(selectedFile.getParent().toString());
			
			//anyadimos poner el modo correcto (normal o android)
			if(selectedFile.getName().endsWith(TFileUtils.TCOA))
				TEditor.set_android_mode(true);
			else
				TEditor.set_android_mode(false);

			try {
				getEditor().deleteProject();
				// Load the project
				getEditor().setProject(TProjectHandler.loadProject(selectedFile));
				// Set selectedFile as the project file
				getEditor().setProjectFile(selectedFile);
				// Set the default cursor
				getEditor().restoreCursor();
			} catch (Exception ex) {
				// If the import fails show an error message
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TProjectOpenAction.OPEN_ERROR"),
						TLanguage.getString("ERROR") + "!",
						JOptionPane.ERROR_MESSAGE);
				getEditor().restoreCursor();
			}
		}
	}
}
