/*
 * File: TComponentView.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
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

package tico.board.componentview;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

import tico.board.components.TComponent;
import tico.components.TMenuItem;
import tico.editor.TActionSet;
import tico.editor.TEditor;
import tico.editor.actions.TComponentDialogAction;

/**
 * Default implementation of a <code>TComponent</code> view.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TComponentView extends VertexView {
	/**
	 * Creates a new <code>TComponentView</code>.
	 */
	public TComponentView() {
		super();
	}

	/**
	 * Creates a new <code>TComponentView</code> for the specified <code>component</code>.
	 * 
	 * @param component The specified <code>component</code>
	 */
	public TComponentView(Object component) {
		super(component);
	}

	/* (non-Javadoc)
	 * @see org.jgraph.graph.AbstractCellView#getRenderer()
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
	// TUNE Try to change editor to board
	/**
	 * Creates the <code>popupMenu</code> for the component.
	 * 
	 * @param editor <code>editor</code> that is editing the component's board
	 * @return The created <code>popupMenu</code>
	 */
	public JPopupMenu createPopupMenu(TEditor editor) {
		JPopupMenu menu = new JPopupMenu();

		menu.add(new TMenuItem(new TComponentDialogAction(editor,
				(TComponent)this.getCell())));
		menu.add(new JSeparator());
		menu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.COPY_ACTION)));
		menu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.CUT_ACTION)));
		menu.add(new JSeparator());
		menu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.FRONT_ACTION)));
		menu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.BACK_ACTION)));

		return menu;
	}
}
