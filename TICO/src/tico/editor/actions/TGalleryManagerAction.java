/*
 * File: TProjectPropertiesAction.java
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

import java.awt.event.ActionEvent;

import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TEditor;
import dialogs.mainFrame;

/**
 * Action wich opens the current editor project properties dialog.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TGalleryManagerAction extends TEditorAbstractAction {
	/**
	 * Constructor for TProjectPropertiesAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TGalleryManagerAction(TEditor editor) {
		super(editor, TLanguage.getString("TImageGalleryDialog.GALLERY_MANAGER"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		mainFrame f = new mainFrame(TSetup.getLanguage());
		f.setVisible(true);
	    f.pack();
	}
}
