/*
 * File: TBoardExportImageAction
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jan 30, 2006
 *  
 * Based on: @(#)EditUndo.java
 *           Copyright (c) 2001-2005, Gaudenz Alder
 *           From JGraphPad 5.7.3.1.1
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
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tico.components.resources.CustomFilter;
import tico.components.resources.TFileUtils;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich exports to image the editor selected board using the specified
 * image file format.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardExportImageAction extends TEditorAbstractAction {
	private static File defaultDirectory = null;

	private String fileType = TFileUtils.PNG;

	/**
	 * Constructor for TBoardExportImageAction.
	 * 
	 * @param editor The boards' <code>editor</code>
	 * @param fileType The <code>fileType</code> of the export image
	 */
	public TBoardExportImageAction(TEditor editor, String fileType) {
		super(editor, TLanguage.getString("TBoardExportImageAction.NAME") + " "
				+ fileType.toUpperCase());

		this.fileType = fileType;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Check if the board exists
		if (getEditor().getCurrentBoard() == null) {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TBoardExportImageAction.NO_BOARD_ERROR"),
					TLanguage.getString("ERROR") + "!",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Open a JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		// Customoze JFileChooser
		fileChooser.setDialogTitle(TLanguage.getString("TBoardExportImageAction.NAME") +
				" " + fileType.toUpperCase());
		fileChooser.setCurrentDirectory(defaultDirectory);
		fileChooser.setSelectedFile(new File(getEditor().getCurrentBoard()
				.getBoardName() + "." + fileType));
		fileChooser.addChoosableFileFilter(new CustomFilter(fileType));
		fileChooser.setAcceptAllFileFilterUsed(false);

		// Checks if the user has chosen a file
		int returnValue = fileChooser.showSaveDialog((Component)null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			// Get the chosen file
			File selectedFile = fileChooser.getSelectedFile();
			// Set its directory as next first JFileChooser directory
			defaultDirectory = selectedFile.getParentFile();
			// Check the extension and if it has not, add it
			selectedFile = new File(TFileUtils.setExtension(selectedFile
					.getAbsolutePath(), fileType));
			// Check if the file exists and if the user wants to overwrite it
			if (selectedFile.exists())
				if (JOptionPane.showConfirmDialog(null,
						TLanguage.getString("TBoardExportImageAction.CHOOSE_FILE_EXISTS") + "\n" +
						TLanguage.getString("TBoardExportImageAction.CHOOSE_FILE_EXISTS_QUESTION"),
						TLanguage.getString("TBoardExportImageAction.CHOOSE_FILE_OVERWRITE"),
						JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
					return;

			try {
				// Clear selecction
				getEditor().getCurrentBoard().clearSelection();
				// Create the image
				BufferedImage img = getEditor().getCurrentBoard().getImage();
				// Depending on the file type
				ImageIO.write(img, fileType, selectedFile);
			} catch (Exception ex) {
				// If the import fails show an error message
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TBoardExportImageAction.EXPORT_ERROR"),
						TLanguage.getString("ERROR") + "!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
