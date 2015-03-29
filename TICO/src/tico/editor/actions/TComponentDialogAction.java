/*
 * File:TComponentDialogAction.java
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

import tico.board.components.TComponent;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich opens a components properties dialog.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TComponentDialogAction extends TEditorAbstractAction {
	private TComponent component;

	/**
	 * Constructor for TComponentDialogAction.
	 * 
	 * @param editor The boards' editor
	 * @param component The properties dialog component
	 */
	public TComponentDialogAction(TEditor editor, TComponent component) {
		super(editor, TLanguage.getString("TComponentDialogAction.NAME"));
		this.component = component;
	}

	public void actionPerformed(ActionEvent e) {
		getEditor().getCurrentBoardContainer().getComponentDialogFactory()
				.createComponentDialog(component);
	}
}
