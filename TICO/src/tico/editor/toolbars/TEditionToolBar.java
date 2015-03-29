/*
 * File: TEditionToolBar.java
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
 * Tool bar with the edit action buttons.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TEditionToolBar extends TToolBar {
	// Delete button
	private TToolBarButton deleteButton;
	// Copy paste buttons
	private TToolBarButton cutButton;
	private TToolBarButton copyButton;
	private TToolBarButton pasteButton;
	// Undo buttons
	private TToolBarButton undoButton;
	private TToolBarButton redoButton;
	
	/**
	 * Creates a new <code>TEditionToolBar</code>.
	 * 
	 * @param editor The <code>editor</code> actions receiver
	 */	
	public TEditionToolBar(TEditor editor) {
		super(TLanguage.getString("TEditionToolBar.NAME"));

		deleteButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.DELETE_ACTION));
		cutButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.CUT_ACTION));
		copyButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.COPY_ACTION));
		pasteButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.PASTE_ACTION));
		undoButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.UNDO_ACTION));
		redoButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.REDO_ACTION));

		add(cutButton);
		add(copyButton);
		add(pasteButton);
		addSeparator();
		add(undoButton);
		add(redoButton);
		addSeparator();
		add(deleteButton);
	}
}