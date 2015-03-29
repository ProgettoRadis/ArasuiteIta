/*
 * File: TFileToolBar.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 17, 2006
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

package tico.editor.toolbars;

import tico.components.TToolBar;
import tico.components.TToolBarButton;
import tico.configuration.TLanguage;
import tico.editor.TActionSet;
import tico.editor.TEditor;

/**
 * Tool bar with the file action buttons.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFileToolBar extends TToolBar {
	// File buttons
	private TToolBarButton newButton;
	
	private TToolBarButton newAndroidButton;
	
	private TToolBarButton openButton;
	private TToolBarButton saveButton;
	private TToolBarButton saveAsButton;
	// Print button
	private TToolBarButton printButton;
	
	/**
	 * Creates a new <code>TFileToolBar</code>.
	 * 
	 * @param editor The <code>editor</code> actions receiver
	 */	
	public TFileToolBar(TEditor editor) {
		super(TLanguage.getString("TFileToolBar.NAME"));

		newButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.PROJECT_NEW_ACTION));
		
		newAndroidButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.PROJECT_NEW_ANDROID_ACTION));
		
		openButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.PROJECT_OPEN_ACTION));
		saveButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.PROJECT_SAVE_ACTION));
		saveAsButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.PROJECT_SAVE_AS_ACTION));
		printButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.PROJECT_PRINT_ACTION));

		add(newButton);
		add(newAndroidButton);
		addSeparator();
		add(openButton);
		add(saveButton);
		add(saveAsButton);
		addSeparator();
		add(printButton);
	}
}