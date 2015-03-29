/*
 * File: TBasicMarqueeHandler.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: May 23, 2006
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

package tico.editor.handler;

import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.jgraph.graph.BasicMarqueeHandler;

import tico.board.TBoard;
import tico.board.components.TComponent;
import tico.board.componentview.TComponentView;
import tico.components.TMenuItem;
import tico.configuration.TLanguage;
import tico.editor.TActionSet;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;
import tico.editor.actions.TBoardPropertiesAction;

/**
 * New version of <code>BasicMarqueeHandler</code> which add the creation
 * of popup menus with right button and the opening of components dialogs with
 * double click.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBasicMarqueeHandler extends BasicMarqueeHandler {
	/**
	 * Marquee handler <code>TBoardContainer</code>
	 */
	protected TBoardContainer boardContainer;

	// Last created popup menu
	private JPopupMenu menu;

	/**
	 * Creates a new <code>TBasicMarqueeHandler</code> for the specified
	 * <code>boardContainer</code>.
	 * 
	 * @param boardContainer The specified <code>boardContainer</code>
	 */
	public TBasicMarqueeHandler(TBoardContainer boardContainer) {
		super();
		this.boardContainer = boardContainer;
	}

	/**
	 * Returns the marquee handler <code>bardContainer</code>
	 * 
	 * @return The marquee handler <code>boardContainer</code>
	 */
	protected TBoardContainer getBoardContainer() {
		return boardContainer;
	}

	/**
	 * Returns the marquee handler <code>board</code>
	 * 
	 * @return The marquee handler <code>board</code>
	 */
	protected TBoard getBoard() {
		return boardContainer.getBoard();
	}

	/* (non-Javadoc)
	 * @see org.jgraph.graph.BasicMarqueeHandler#isForceMarqueeEvent(java.awt.event.MouseEvent)
	 */
	public boolean isForceMarqueeEvent(MouseEvent e) {
		// If is shift down
		if (e.isShiftDown())
			return false;
		// If right mouse button we want to display the popup menu
		if (SwingUtilities.isRightMouseButton(e))
			return true;
		if (e.getClickCount() > 1)
			return true;
		// Else call superclass
		return super.isForceMarqueeEvent(e);
	}

	// Overriden to create popup menu and display component dialogs
	/* (non-Javadoc)
	 * @see org.jgraph.graph.BasicMarqueeHandler#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// If right mouse button
		if (SwingUtilities.isRightMouseButton(e)) {
			// Find cell in current board
			TComponentView view = (TComponentView)getBoard().getNextViewAt(
					null, e.getX(), e.getY(), true);
			// If on a cell
			if (view != null) {
				// Select the current component
				getBoard().setSelectionCell(view.getCell());
				// Create the popup menu
				menu = view.createPopupMenu(getBoardContainer().getEditor());
			} else {
				// Create standard popup menu
				menu = createDefaultPopupMenu(getBoardContainer().getEditor());
			}
		} else if (e.getClickCount() > 1) {
			// Find cell in current board
			TComponent component = (TComponent)getBoard()
					.getFirstCellForLocation(e.getX(), e.getY());
			// If on a cell
			if (component != null) {
				// Create the component dialog of the current component
				getBoardContainer().getComponentDialogFactory()
						.createComponentDialog(component);
			}
		} else
			// Else call superclass
			super.mousePressed(e);
	}

	// Overriden to display popup menu
	/* (non-Javadoc)
	 * @see org.jgraph.graph.BasicMarqueeHandler#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// If right mouse button
		if (SwingUtilities.isRightMouseButton(e)) {
			// Display popup menu
			menu.show(getBoard(), e.getX(), e.getY());
			// Consume event
			e.consume(); 
		} else{
			// Else call superclass
			super.mouseReleased(e);
		}
		
	}

	/**
	 * Create the a default popup menu with the basic actions.
	 * 
	 * @param editor The actions target editor 
	 * @return The default popup menu
	 */
	private JPopupMenu createDefaultPopupMenu(TEditor editor) {
		JPopupMenu menu = new JPopupMenu();

		// Copy paste actions
		menu.add(new TMenuItem(new TBoardPropertiesAction(editor)));

		menu.add(new JSeparator());
		menu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.PASTE_ACTION)));

		menu.add(new JSeparator());
		menu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.UNDO_ACTION)));
		menu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.REDO_ACTION)));

		// Align actions
		menu.add(new JSeparator());
		JMenu subMenu = new JMenu(TLanguage.getString("TBasicMarqueeHandler.ALIGNMENT"));
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.ALIGN_TOP_ACTION)));
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.ALIGN_BOTTOM_ACTION)));
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.ALIGN_LEFT_ACTION)));
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.ALIGN_RIGHT_ACTION)));
		subMenu.add(new JSeparator());
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.ALIGN_HORIZONTAL_ACTION)));
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.ALIGN_VERTICAL_ACTION)));
		subMenu.add(new JSeparator());
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.FIT_WIDTH_ACTION)));
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.FIT_HEIGHT_ACTION)));
		subMenu.add(new JSeparator());
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.HORIZONTAL_GAP_ACTION)));
		subMenu.add(new TMenuItem(editor.getActionSet().getAction(
				TActionSet.VERTICAL_GAP_ACTION)));
		menu.add(subMenu);

		return menu;
	}
}
