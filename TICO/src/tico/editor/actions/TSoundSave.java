package tico.editor.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tico.board.TProject;
import tico.components.resources.ProjectFilter;
import tico.components.resources.TFileUtils;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.editor.TProjectHandler;

public class TSoundSave extends TEditorAbstractAction {
	private static File defaultDirectory = null;

	/**
	 * Constructor for TProjectSaveAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TSoundSave(TEditor editor) {
		super(editor, TLanguage.getString("Guardar Fichero"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		File selectedFile = getEditor().getProjectFile();
		// If there is no a selected file
		if (selectedFile == null) {
			// Open a JFileChooser
			JFileChooser fileChooser = new JFileChooser();
			// Customoze JFileChooser
			fileChooser.setDialogTitle(TLanguage.getString("TProjectSaveAction.SAVE_PROJECT"));
			fileChooser.setSelectedFile(new File(getEditor().getProject()
					.getName() + "." + TFileUtils.TCO));
			fileChooser.setCurrentDirectory(defaultDirectory);
			fileChooser.addChoosableFileFilter(new ProjectFilter());
			fileChooser.setAcceptAllFileFilterUsed(false);

			// Checks if the user has chosen a file
			int returnValue = fileChooser.showSaveDialog((Component)null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				// Get the chosen file
				selectedFile = fileChooser.getSelectedFile();
				// Set its directory as next first JFileChooser directory
				defaultDirectory = selectedFile.getParentFile();
				// Check the extension and if it has not, add it
				selectedFile = new File(TFileUtils.setExtension(selectedFile
						.getAbsolutePath(), TFileUtils.TCO));
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
