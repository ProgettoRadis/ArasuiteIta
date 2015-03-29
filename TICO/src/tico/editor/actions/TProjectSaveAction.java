/*
 * File: TProjectSaveAction.java
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

import tico.board.TProject;
import tico.components.resources.ProjectAndroidFilter;
import tico.components.resources.ProjectFilter;
import tico.components.resources.TFileUtils;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TEditor;
import tico.editor.TProjectHandler;

/**
 * Action wich saves the current editor project into a file.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TProjectSaveAction extends TEditorAbstractAction {
	private static File defaultDirectory = null;

	/**
	 * Constructor for TProjectSaveAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TProjectSaveAction(TEditor editor) {
		super(editor, TLanguage.getString("TProjectSaveAction.NAME"),
				TResourceManager.getImageIcon("archive-save-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		File selectedFile = getEditor().getProjectFile();
		// If there is no a selected file (the project is new)
		if (selectedFile == null) {
			// Open a JFileChooser
			JFileChooser fileChooser = new JFileChooser();
			// Customize JFileChooser
			fileChooser.setDialogTitle(TLanguage.getString("TProjectSaveAction.SAVE_PROJECT"));
			fileChooser.setSelectedFile(new File(getEditor().getProject()
					.getName() /*+ "." + TFileUtils.TCO*/));
			fileChooser.setCurrentDirectory(defaultDirectory);
			

			ProjectFilter ticoFilter = new ProjectFilter();
			fileChooser.addChoosableFileFilter(ticoFilter);
			
			ProjectAndroidFilter androidFilter=new ProjectAndroidFilter(); 
			fileChooser.addChoosableFileFilter(androidFilter);
			
			fileChooser.setAcceptAllFileFilterUsed(false);
			
			//ponemos por defecto el tipo de archivo en el que estamos trabajando
			if(TEditor.get_android_mode()){
				fileChooser.setFileFilter(androidFilter);
			}else{
				fileChooser.setFileFilter(ticoFilter);
			}
			
			

			// Checks if the user has chosen a file
			int returnValue = fileChooser.showSaveDialog((Component)null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				// Get the chosen file
				selectedFile = fileChooser.getSelectedFile();
				// Set its directory as next first JFileChooser directory
				defaultDirectory = selectedFile.getParentFile();
				// Check the extension and if it has not, add it
				if(fileChooser.getFileFilter() instanceof ProjectFilter){
					selectedFile = new File(TFileUtils.setExtension(selectedFile
						.getAbsolutePath(), TFileUtils.TCO));
					TEditor.set_android_mode(false);
					//this.getEditor().updateBoardButtons();
				}else if(fileChooser.getFileFilter() instanceof ProjectAndroidFilter){
					selectedFile = new File(TFileUtils.setExtension(selectedFile
							.getAbsolutePath(), TFileUtils.TCOA));
					TEditor.set_android_mode(true);
					//this.getEditor().updateBoardButtons();
				}
				// Check if the file exists and if the user wants to overwrite it
				if (selectedFile.exists())
					if (JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TProjectSaveAction.CHOOSE_FILE_EXISTS") + "\n" +
							TLanguage.getString("TProjectSaveAction.CHOOSE_FILE_EXISTS_QUESTION"),
							TLanguage.getString("TProjectSaveAction.CHOOSE_FILE_OVERWRITE"),
							JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
						return;
				
				
			}
		}

		if (selectedFile != null) {
			try {
				// Set the selected name to the project
				getEditor().getProject().setName(TFileUtils.getFilename(selectedFile));
				// Get the project
				TProject project = getEditor().getProject();
				// Save the file
				TProjectHandler.saveProject(project, selectedFile);
				// Set modified to false
				getEditor().setModified(false);
				// Set the selected file as the base file for the project
				getEditor().setProjectFile(selectedFile);
				// Set the editor home directory
				TSetup.setEditorHome(selectedFile.getParent().toString());
				
				//actualizamos los botones de proyecto por si hemos cambiado de modo
				this.getEditor().updateBoardButtons();
				this.getEditor().getContentPane().repaint();
			} catch (Exception ex) {
				// If the import fails show an error message
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TProjectSaveAction.SAVE_ERROR"),
						TLanguage.getString("ERROR") + "!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
