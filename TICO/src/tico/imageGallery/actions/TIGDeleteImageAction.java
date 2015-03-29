/*
 * File: TIGDeleteImageAction.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Apr 15, 2008
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

import javax.swing.JOptionPane;

import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;
import tico.imageGallery.dialogs.TIGDeleteImagesDialog;

public class TIGDeleteImageAction extends TIGAbstractAction{

	private TIGDataBase dataBase;
	/**
	 * Constructor for TIGOpenGallery.
	 * 
	 * @param editor The boards' editor
	 */
	public TIGDeleteImageAction(TEditor editor, TIGDataBase dataBase) {
		super(editor);
		this.dataBase = dataBase;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		TIGDataBase.conectDB();
		Vector<Vector<String>> data = TIGDataBase.imageSearchByName("*");
		TIGDataBase.closeDB();
		if (data.size() > 0){
			new TIGDeleteImagesDialog(getEditor(), dataBase);
		}
		else{
			JOptionPane.showConfirmDialog(null,
					TLanguage.getString("TIGManageImageAction.EMPTY"),
					TLanguage.getString("TIGManageImageAction.ERROR"),
					JOptionPane.CLOSED_OPTION ,JOptionPane.WARNING_MESSAGE );
		}
	}
}
