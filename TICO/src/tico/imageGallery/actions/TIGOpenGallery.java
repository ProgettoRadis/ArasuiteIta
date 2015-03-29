/*
 * File: TIGOpenGallery.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Jan 18, 2008
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



import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dialogs.TIGImageGalleryDialog;

/**
 * Action wich opens the editor about dialog.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */

public class TIGOpenGallery extends TIGAbstractAction{

	/**
	 * Constructor for TIGOpenGallery.
	 * 
	 * @param editor The boards' editor
	 */
	public TIGOpenGallery(TEditor editor) {
		super(editor, TLanguage.getString("TIGOpenGallery.NAME"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		new TIGImageGalleryDialog(getEditor(),0);
	}
}
