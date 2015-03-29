/*
 * File: TGridCellView.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 			David Ramos
 * 			Fernando Negre
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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.jgraph.graph.CellViewRenderer;

import tico.board.componentredenerer.TGridCellRenderer;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.editor.TEditor;
import tico.editor.actions.TAddGridCellAction;
import tico.editor.actions.TAddGridColumnAction;
import tico.editor.actions.TAddGridRowAction;
import tico.editor.actions.TChangeGridOrderAction;
import tico.editor.actions.TDeleteGridCellAction;
import tico.editor.actions.TExtractGridCellAction;
import tico.editor.actions.TReorderGridAction;

/**
 * Implementation of a <code>TGridCellView</code> view.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TGridCellView extends TComponentView {
	/**
	 * <code>TGridCellRendered</code> that displays the <code>TGridCell</code> of this view.
	 */
	public static transient TGridCellRenderer renderer = new TGridCellRenderer();

	/**
	 * Creates a new <code>TGridCellView</code>.
	 */
	public TGridCellView() {
		super();
	}

	/**
	 * Creates a new <code>TGridCellView</code> for the specified <code>gridCell</code>.
	 * 
	 * @param gridCell The specified <code>gridCell</code>
	 */
	public TGridCellView(Object gridCell) {
		super(gridCell);
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

		// Grid actions
		menu.add(new JSeparator());
		menuItem = new JMenuItem(new TAddGridCellAction(editor,
				((TGridCell)this.getCell()).getGrid()));
		menu.add(menuItem);
		menuItem = new JMenuItem(new TAddGridColumnAction(editor,
				((TGridCell)this.getCell()).getGrid()));
		menu.add(menuItem);
		menuItem = new JMenuItem(new TAddGridRowAction(editor, ((TGridCell)this
				.getCell()).getGrid()));
		menu.add(menuItem);
		menu.add(new JSeparator());
		menuItem = new JMenuItem(new TChangeGridOrderAction(editor,
				((TGridCell)this.getCell()).getGrid(), TGrid.COLUMNS));
		menu.add(menuItem);
		menuItem = new JMenuItem(new TChangeGridOrderAction(editor,
				((TGridCell)this.getCell()).getGrid(), TGrid.ROWS));
		menu.add(menuItem);
		menu.add(new JSeparator());
		menuItem = new JMenuItem(new TReorderGridAction(editor,
				((TGridCell)this.getCell()).getGrid()));
		menu.add(menuItem);

		// TGridCell actions
		menu.add(new JSeparator());
		menuItem = new JMenuItem(new TDeleteGridCellAction(editor,
				(TGridCell)this.getCell()));
		menu.add(menuItem);
		menuItem = new JMenuItem(new TExtractGridCellAction(editor,
				(TGridCell)this.getCell()));
		menu.add(menuItem);

		return menu;
	}
}
