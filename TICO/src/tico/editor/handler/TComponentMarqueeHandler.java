/*
 * File: TComponentMarqueeHandler.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Mar 6, 2006
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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.JGraph;

import tico.board.components.TComponent;
import tico.editor.TBoardContainer;

/**
 * Marquee handler which allows to insert, in a <code>TBoardContainer</code>
 * board, new components of the selected dimension.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public abstract class TComponentMarqueeHandler extends TBasicMarqueeHandler {
	// Determine the minimum size of a cell that allows it to be resized.
	/**
	 * Minimum new component height.
	 */
	protected static int MIN_HEIGHT = 5;
		
	/**
	 * Minimum new component width.
	 */
	protected static int MIN_WIDTH = 5;
	
	/**
	 * Creates a new <code>TComponentMarqueeHandler</code> for the specified
	 * <code>boardContainer</code>.
	 * 
	 * @param boardContainer The specified <code>boardContainer</code>
	 */
	public TComponentMarqueeHandler(TBoardContainer boardContainer) {
		super(boardContainer);
	}
	
	/* (non-Javadoc)
	 * @see tico.editor.handler.TBasicMarqueeHandler#isForceMarqueeEvent(java.awt.event.MouseEvent)
	 */
	public boolean isForceMarqueeEvent(MouseEvent e) {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.graph.BasicMarqueeHandler#handleMarqueeEvent(java.awt.event.MouseEvent,
	 *      org.jgraph.JGraph, java.awt.geom.Rectangle2D)
	 */
	public void handleMarqueeEvent(MouseEvent e, JGraph graph,
			Rectangle2D bounds) {
		// Snap the Point to the Grid
		Rectangle2D snapBounds = graph.snap(bounds);

		if ((bounds.getHeight() >= MIN_HEIGHT)
			|| (bounds.getWidth() >= MIN_WIDTH)) {
			// Create default TComponent vector
			TComponent component = createDefaultComponent(snapBounds);

			getBoard().getGraphLayoutCache().insert(component);
			e.consume();
			// Restore the marquee handler
			getBoard().setMarqueeHandler(
					new TBasicMarqueeHandler(getBoardContainer()));
		}
	}

	// Don't allow to draw outside the graph.
	/* (non-Javadoc)
	 * @see org.jgraph.graph.BasicMarqueeHandler#processMouseDraggedEvent(java.awt.event.MouseEvent)
	 */
	protected void processMouseDraggedEvent(MouseEvent e) {
		Point2D eventPoint = e.getPoint();
				
		if (eventPoint.getX() < 0) e.translatePoint((int)-eventPoint.getX(), 0);
		if (eventPoint.getY() < 0) e.translatePoint(0, (int)-eventPoint.getY());
				
		super.processMouseDraggedEvent(e);
	}
	
	/**
	 * Hook for subclasses. Allow subclassers to determine which component is
	 * going to be added.
	 * 
	 * @param bounds The <code>bounds</code> of the new component
	 * @return The new <code>component</code>
	 */
	protected abstract TComponent createDefaultComponent(Rectangle2D bounds);
}
