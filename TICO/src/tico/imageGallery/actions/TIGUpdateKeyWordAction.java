/*
 * File: TIGUpdateKeyWordAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
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



import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

/**
 * Action which exists from the editor application.
 * 
 * @author Patricia M. Jaray
 * @version 2.2 Apr 1, 2008
 */
public class TIGUpdateKeyWordAction extends TIGAbstractAction {
	
	protected int delete = 0;
	protected TIGDataBase dataBase;
	protected String old, newConcept;
	protected TEditor editor;
	
	/**
	 * Constructor for TEditorExitAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TIGUpdateKeyWordAction(TEditor editor, TIGDataBase dataBase, String old, String newConcept) {
		super(editor);
		this.editor = editor;
		this.dataBase = dataBase;
		this.old = old;
		this.newConcept = newConcept;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		TIGDataBase.updateConceptDB(this.old,this.newConcept);
	}
	
}
