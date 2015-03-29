/*
 * File: TBoardContainer.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jul 10, 2006
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

package tico.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.RepaintManager;

import org.jgraph.event.GraphLayoutCacheEvent;
import org.jgraph.event.GraphLayoutCacheListener;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.AttributeMap;

import tico.board.TBoard;
import tico.board.TBoardConstants;
import tico.board.TBoardModel;
import tico.board.TBoardUndoManager;
import tico.board.components.TComponent;
import tico.editor.dialogs.TComponentDialogFactory;
import tico.editor.handler.TBasicMarqueeHandler;

/**
 * An interface class that makes the interaction between individual project
 * boards and the main editor window.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardContainer extends JPanel implements GraphSelectionListener,
	GraphModelListener, GraphLayoutCacheListener, PropertyChangeListener, Printable {

	// Board
	private TBoard board;

	// TEditor who owns this board container
	private TEditor editor;

	// Board undo manager
	private TBoardUndoManager undoManager;

	// The boards dialog factory
	protected TComponentDialogFactory componentDialogFactory;

	/**
	 * Creates a new <code>TBoardContainer</code> that will interact between
	 * the specified <code>editor</code> and <code>board</code>
	 * 
	 * @param editor The specified <code>editor</code>
	 * @param board The specified <code>board</code>
	 */
	public TBoardContainer(TEditor editor, TBoard board) {
		super();

		this.editor = editor;
		this.board = board;

		// Set component dialog factory
		setComponentDialogFactory(new TComponentDialogFactory(this));
		// Set the initial marquee handler
		board.setMarqueeHandler(new TBasicMarqueeHandler(this));

		setLayout(new BorderLayout());

		add(board, BorderLayout.CENTER);
		registerListeners();
	}

	/**
	 * Set the board marquee handler to <code>TBasicMarqueeHandler</code>.
	 */
	public void resetMarqueeHandler() {
		board.setMarqueeHandler(new TBasicMarqueeHandler(this));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (board != null)
			return board.getBoardName();

		return null;
	}

	/**
	 * Gets the <code>board</code> of this <code>TBoardContainer</code>.
	 * 
	 * @return Returns the <code>board</code>.
	 */
	public TBoard getBoard() {
		return board;
	}

	// Register the listeners that will listen this TBoardContainer
	private void registerListeners() {
		if (board != null) {
			board.getSelectionModel().addGraphSelectionListener(this);
			board.getModel().addGraphModelListener(this);
			board.getGraphLayoutCache().addGraphLayoutCacheListener(this);
			board.addPropertyChangeListener(this);

			undoManager = new TBoardUndoManager();
			board.getModel().addUndoableEditListener(undoManager);
		}
	}

	/**
	 * Gets the <code>editor</code> of this <code>TBoardContainer</code>.
	 * 
	 * @return Returns the <code>editor</code>.
	 */
	public TEditor getEditor() {
		return editor;
	}

	/**
	 * Sets the <code>componentDialogFactory</code>.
	 * 
	 * @param factory The <code>componentDialogFactory</code> to set
	 */
	public void setComponentDialogFactory(TComponentDialogFactory factory) {
		this.componentDialogFactory = factory;
	}

	/**
	 * Returns the boardContainer's <code>componentDialogFactory</code>.
	 * 
	 * @return The boardContainer's <code>componentDialogFactory</code>
	 */
	public TComponentDialogFactory getComponentDialogFactory() {
		return componentDialogFactory;
	}

	// Undo manager methods
	/**
	 * Gets the <code>undoManager</code> of this <code>TBoardContainer</code>.
	 * 
	 * @return Returns the <code>undoManager</code>.
	 */
	public TBoardUndoManager getUndoManager() {
		return undoManager;
	}

	/**
	 * Cleans the <code>undoManager</code> edits history.
	 */
	public void resetUndoManager() {
		undoManager.discardAllEdits();
	}

	// Listener methods
	/* (non-Javadoc)
	 * @see org.jgraph.event.GraphSelectionListener#valueChanged(org.jgraph.event.GraphSelectionEvent)
	 */
	public void valueChanged(GraphSelectionEvent e) {
		editor.updateSelectionButtons();
	}

	/* (non-Javadoc)
	 * @see org.jgraph.event.GraphModelListener#graphChanged(org.jgraph.event.GraphModelEvent)
	 */
	public void graphChanged(GraphModelEvent e) {
		editor.updateHistoryButtons();

		// Update the editor ordered list
		boolean updateCellList = false;
		// Check if the ordered browseable component list has been modified
		Map attributes = (Map)e.getChange().getAttributes();
		
		Map modelAttributes;
		if (attributes != null) {
			// Check the model attributes
			modelAttributes = (Map)attributes.get(getBoard().getModel());
			if (modelAttributes != null)
				if (modelAttributes.containsKey(TBoardConstants.ORDERED_CELL_LIST))
					updateCellList = true;
			// Check the component attributes
			// For each modified component
			Iterator componentIterator = attributes.entrySet().iterator();
			while (componentIterator.hasNext()) {
				Map.Entry entry = (Map.Entry)componentIterator.next();

				if (entry.getKey() instanceof TComponent) {
					TComponent component = (TComponent)entry.getKey();
					AttributeMap componentAttributes = (AttributeMap)entry.getValue();
					
					if (TBoardConstants.isBrowseable(component.getAttributes()) &&
							componentAttributes.containsKey(TBoardConstants.ID))
						updateCellList = true;
				}
			}
		}
		// Check if any grid or cell has been removed
		Object[] removed = e.getChange().getRemoved();
		if (removed != null)
			for (int i = 0; i < removed.length && !updateCellList; i++) {
				
				if (TBoardConstants.isBrowseable(((TComponent)removed[i])
						.getAttributes()))
					updateCellList = true;
			}

		if (updateCellList)
			editor.updateCellOrderList();

		editor.updateUI();
	}

	/* (non-Javadoc)
	 * @see org.jgraph.event.GraphLayoutCacheListener#graphLayoutCacheChanged(org.jgraph.event.GraphLayoutCacheEvent)
	 */
	public void graphLayoutCacheChanged(GraphLayoutCacheEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == TBoard.MARQUEE_HANDLER_PROPERTY)
			editor.updateToolBar();

		if (evt.getPropertyName().equals("transferHandler"))
			board.setMinimumSize(TBoardConstants.getSize(((TBoardModel)board
					.getModel()).getAttributes()));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		// Get current page printing board
		TBoard printBoard = getBoard();
		// Get board bounds
		Dimension bounds = TBoardConstants.getSize(printBoard
				.getAttributes(printBoard.getModel()));
		// Transform graphics to 2D
		Graphics2D g2 = (Graphics2D) graphics;
		// Scale the image to fit the impession size
		double scaleWidth = pageFormat.getImageableWidth() / bounds.getWidth();
		double scaleHeight = pageFormat.getImageableHeight() / bounds.getHeight();
		// Set the scale size to the minimum of both
		if (scaleWidth > scaleHeight)
			scaleWidth = scaleHeight;
		else
			scaleHeight = scaleWidth;
		g2.scale(scaleWidth, scaleHeight);
		// Translate the graphics origin
		g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		// Disable doube buffering
		RepaintManager currentManager = RepaintManager
				.currentManager(printBoard);
		currentManager.setDoubleBufferingEnabled(false);
		// Print
		printBoard.paint(g2);
		g2.translate(0f,0f);
		g2.drawString(printBoard.toString(), 30, 38);
		// Enable double buffering
		currentManager.setDoubleBufferingEnabled(true);
		return PAGE_EXISTS;
	}
}