/*
 * File: THideToolBarAction.java
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

import javax.swing.JCheckBoxMenuItem;

import tico.components.TToolBar;
import tico.editor.TEditor;

/**
 * Action wich shows or hides the specified tool bar.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class THideToolBarAction extends TEditorAbstractAction {
	// The grid in which the new TGridCell is going to be inserted.
	private TToolBar toolBar;
	
	/**
	 * Constructor of the THideToolBarAction.
	 * 
	 * @param editor The boards' editor
	 * @param toolBar The toolBar to be hidden or shown
	 */
	public THideToolBarAction(TEditor editor, TToolBar toolBar) {
		super(editor,toolBar.getName());
		
		this.toolBar = toolBar;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBoxMenuItem)
			toolBar.setVisible(((JCheckBoxMenuItem)e.getSource()).getState());
		else
			toolBar.setVisible(!toolBar.isVisible());
	}

}
