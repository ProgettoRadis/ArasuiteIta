/*
 * File: TIGDeleteKeyWordAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Apr 1, 2008
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
import javax.swing.JOptionPane;



import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

/**
 * Action which exists from the editor application.
 * 
 * @author Patricia M. Jaray
 * @version 2.2 Apr 1, 2008
 */
public class TIGDeleteKeyWordAction extends TIGAbstractAction {
	
	protected int delete = 0;
	protected TIGDataBase dataBase;
	protected String text;
	protected TEditor editor;
	
	
	/**
	 * Constructor for TEditorExitAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TIGDeleteKeyWordAction(TEditor editor, String text, TIGDataBase dataBase) {
		super(editor);
		this.editor = editor;
		this.dataBase = dataBase;
		this.text = text;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int choosenOption = JOptionPane.NO_OPTION;
		// Ask the user if he is sure to delete the keyword
		choosenOption = JOptionPane.showConfirmDialog(null,
				TLanguage.getString("TIGDeleteKeyWordAction.ASK"),
				TLanguage.getString("TIGDeleteKeyWordAction.NAME"),
				JOptionPane.YES_NO_CANCEL_OPTION);
		// If cancel or no, exit
		if ((choosenOption == JOptionPane.CANCEL_OPTION) || (choosenOption == JOptionPane.NO_OPTION))
			return;
		// Delete the keyword
		delete = 1;
		TIGDataBase.deleteConceptDB(text);
	}
	
	public int delete(){
		return delete;
	}
}
