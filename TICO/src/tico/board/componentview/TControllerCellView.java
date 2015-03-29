/*
 * File: TCellView.java
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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.jgraph.graph.CellViewRenderer;

import tico.board.componentredenerer.TCellRenderer;
import tico.board.componentredenerer.TControllerCellRenderer;
import tico.board.components.TCell;
import tico.board.components.TGrid;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.editor.actions.TInsertGridCellAction;

/**
 * Implementation of a <code>TCell</code> view.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TControllerCellView extends TComponentView {
	/**
	 * <code>TCellRendered</code> that displays the <code>TCell</code> of this view.
	 */
	public static transient TControllerCellRenderer renderer = new TControllerCellRenderer();
	
	/**
	 * Creates a new <code>TCellView</code>.
	 */
	public TControllerCellView() {
		super();
	}

	/**
	 * Creates a new <code>TCellView</code> for the specified <code>cell</code>.
	 * 
	 * @param cell The specified <code>cell</code>
	 */
	public TControllerCellView(Object cell) {
		super(cell);
	}

	/* (non-Javadoc)
	 * @see org.jgraph.graph.AbstractCellView#getRenderer()
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
	/* (non-Javadoc)
	 * @see tico.board.componentview.TComponentView#createPopupMenu(tico.editor.TEditor)
	 */
	public JPopupMenu createPopupMenu(TEditor editor) {
		JPopupMenu menu = super.createPopupMenu(editor);
		JMenuItem menuItem;
		JMenu subMenu;
		
		subMenu = new JMenu(TLanguage.getString("TCellView.INSERT_INTO") + "...");
		
		Object[] components = editor.getCurrentBoard().getGraphLayoutCache().getCells(
			editor.getCurrentBoard().getGraphLayoutCache().getCellViews());
		for (int i = 0; i < components.length; i++)
			if (components[i] instanceof TGrid) {
				menuItem = new JMenuItem(
						new TInsertGridCellAction(editor,(TCell)this.getCell(),
								(TGrid)components[i]));
				subMenu.add(menuItem);
		}
		
		if (subMenu.getItemCount() > 0) {
			menu.add(new JSeparator());
			menu.add(subMenu);
		}
		
		return menu; 
	}	
}
