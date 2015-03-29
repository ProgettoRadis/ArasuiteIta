/*
 * File: TIGModifyImageAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Apr 28, 2008
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

package tico.imageGallery.actions;

import java.awt.event.ActionEvent;
import java.util.Vector;
import java.io.File;

import javax.swing.JOptionPane;



import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

/**
 * Action which exists from the editor application.
 * 
 * @author Patricia M. Jaray
 * @version 2.2 Apr 2, 2008
 */
public class TIGModifyImageAction extends TIGAbstractAction {
	
	protected TIGDataBase dataBase;
	
	protected Vector theConcepts;
	
	protected String imageName;
	
	protected String imagePath;
	
	/**
	 * Constructor for TEditorExitAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TIGModifyImageAction(TEditor editor, TIGDataBase dataBase, String path, String name, Vector concepts) {
		super(editor);
		this.dataBase = dataBase;
		theConcepts = (Vector) concepts.clone();
		this.imagePath = path;
		this.imageName = name;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int error = TIGDataBase.modifyImage(imagePath, theConcepts);
		if (error != 0)
			JOptionPane.showConfirmDialog(null,
					TLanguage.getString("TIGModifyImageAction.ERROR"),
					TLanguage.getString("TIGModifyImageAction.NAME"),
					JOptionPane.CLOSED_OPTION);
	}
}