/*
 * File: TLineView.java
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

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

import tico.board.TBoardConstants;
import tico.board.componentredenerer.TLineRenderer;

/**
 * Implementation of a <code>TLineView</code> view.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TLineView extends TComponentView {
	/**
	 * <code>TLineRendered</code> that displays the <code>TLine</code> of this view.
	 */
	public static transient TLineRenderer renderer = new TLineRenderer();
	

	/**
	 * Creates a new <code>TLineView</code>.
	 */
	public TLineView() {
		super();
	}

	/**
	 * Creates a new <code>TLineView</code> for the specified <code>line</code>.
	 * 
	 * @param line The specified <code>line</code>
	 */
	public TLineView(Object line) {
		super(line);
	}

	/* (non-Javadoc)
	 * @see org.jgraph.graph.AbstractCellView#getRenderer()
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
	/* (non-Javadoc)
	 * @see org.jgraph.graph.CellView#getHandle(org.jgraph.graph.GraphContext)
	 */
	public CellHandle getHandle(GraphContext context) {
		if (TBoardConstants.isSizeable(getAllAttributes())
				&& !TBoardConstants.isAutoSize(getAllAttributes())
				&& context.getGraph().isSizeable()) {
			return new TLineHandle(this, context);
		}
		return null;
	}	
	
	// Modifications: Updates de StartCorner line and restricts
	// a minimum size for the line depending on the line width.
	// Allows resizing only of the start corner and its oposite one.
	public static class TLineHandle extends VertexView.SizeHandle {
		protected transient int startCorner;
		
		public TLineHandle(VertexView vertexview, GraphContext ctx) {
			super(vertexview, ctx);
			
			int corner = TBoardConstants.getStartCorner(vertexview.getAllAttributes());
			int[] myCursors = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			
			for (int i = 0; i < cursors.length; i++)
				if ((i == corner) || (i == opositeCorner(corner)))
					myCursors[i] = Cursor.HAND_CURSOR;	
			cursors = myCursors;
		}
		
		/** Process mouse pressed event. */
		public void mousePressed(MouseEvent event) {
			startCorner = TBoardConstants.getStartCorner(vertex.getAllAttributes());
			super.mousePressed(event);
		}
		
		/** Process mouse dragged event. */
		public void mouseDragged(MouseEvent event) {
			if (firstDrag && graph.isDoubleBuffered() && cachedBounds == null) {
				initOffscreen();
				firstDrag = false;
			}
			Rectangle2D dirty = null;
			Graphics g = (offgraphics != null) ? offgraphics : graph
					.getGraphics();
			if (index == -1)
				return;
			Rectangle2D newBounds = computeBounds(event);
			int newCorner = computeStartCorner(event);
			g.setColor(graph.getForeground());
			g.setXORMode(graph.getBackground().darker());
			overlay(g);
			if (offgraphics != null) {
				dirty = graph
						.toScreen((Rectangle2D) vertex.getBounds().clone());
				Rectangle2D t = graph.toScreen(AbstractCellView
						.getBounds(contextViews));
				if (t != null)
					dirty.add(t);
			}
			if (cachedBounds != null)
				cachedBounds = newBounds;
			else {
				// Reset old Bounds
				CellView[] all = AbstractCellView
						.getDescendantViews(new CellView[] { vertex });
				for (int i = 0; i < all.length; i++) {
					CellView orig = graph.getGraphLayoutCache().getMapping(
							all[i].getCell(), false);
					if (orig != null) {
						AttributeMap origAttr = (AttributeMap) orig
								.getAllAttributes().clone();
						all[i].changeAttributes(origAttr);
						all[i].refresh(graph.getModel(), context, false);
					}
				}
				vertex.setBounds(newBounds);
				TBoardConstants.setStartCorner(vertex.getAttributes(),newCorner);
				if (vertex != null)
					graph.getGraphLayoutCache().update(vertex);
				if (contextViews != null)
					graph.getGraphLayoutCache().update(contextViews);
			}
			overlay(g);
			if (offscreen != null) {
				dirty.add(graph.toScreen((Rectangle2D) vertex.getBounds()
						.clone()));
				Rectangle2D t = graph.toScreen(AbstractCellView
						.getBounds(contextViews));
				if (t != null)
					dirty.add(t);
				int border = PortView.SIZE + 10;
				if (graph.isPortsScaled())
					border = (int) (graph.getScale() * border);
				int border2 = border / 2;
				dirty.setFrame(dirty.getX() - border2, dirty.getY() - border2,
						dirty.getWidth() + border, dirty.getHeight() + border);
				double sx1 = Math.max(0, dirty.getX());
				double sy1 = Math.max(0, dirty.getY());
				double sx2 = sx1 + dirty.getWidth();
				double sy2 = sy1 + dirty.getHeight();
				graph.getGraphics().drawImage(offscreen, (int) sx1, (int) sy1,
						(int) sx2, (int) sy2, (int) sx1, (int) sy1, (int) sx2,
						(int) sy2, graph);
			}
		}
		
		protected Rectangle2D computeBounds(MouseEvent event) {
			Rectangle2D r = super.computeBounds(event);
			float min = TBoardConstants.getLineWidth(vertex.getAttributes());
			
			// TUNE Check modifications to width and height
			r = new Rectangle2D.Double(r.getX(), r.getY(), 
					((min < r.getWidth())?r.getWidth():min) + 1, 
					((min < r.getHeight())?r.getHeight():min) + 1);
				
			return r;
		}
		
		protected int computeStartCorner(MouseEvent event) {			
			double left = initialBounds.getX();
			double right = initialBounds.getX() + initialBounds.getWidth() - 1;
			double top = initialBounds.getY();
			double bottom = initialBounds.getY() + initialBounds.getHeight() - 1;
			
			Point2D p = graph.fromScreen(graph.snap((Point2D) event.getPoint()
					.clone()));
			// Not into negative coordinates
			p.setLocation(Math.max(0, p.getX()), Math.max(0, p.getY()));
			// Bottom row
			if (index > 4)
				bottom = p.getY();
			// Top row
			else if (index < 3)
				top = p.getY();
			// Left col
			if (index == 0 || index == 3 || index == 5)
				left = p.getX();
			// Right col
			else if (index == 2 || index == 4 || index == 7)
				right = p.getX();
			double width = right - left;
			double height = bottom - top;
			
			if (isConstrainedSizeEvent(event)
					|| GraphConstants.isConstrained(vertex.getAllAttributes())) {
				if (index == 3 || index == 4 || index == 5)
					height = width;
				else if (index == 1 || index == 6 || index == 2 || index == 7)
					width = height;
				else {
					height = width;
				}
			}
			
			if ((width < 0) && (height < 0)) switch (startCorner) {	// Flip over both sides
				case TBoardConstants.TOP_LEFT_CORNER:
					return TBoardConstants.BOTTOM_RIGHT_CORNER;
				case TBoardConstants.TOP_RIGHT_CORNER:
					return TBoardConstants.BOTTOM_LEFT_CORNER;
				case TBoardConstants.BOTTOM_LEFT_CORNER:
					return TBoardConstants.TOP_RIGHT_CORNER;
				case TBoardConstants.BOTTOM_RIGHT_CORNER:
					return TBoardConstants.TOP_LEFT_CORNER;
			}
			if (width < 0) switch (startCorner) {	// Flip over left side
				case TBoardConstants.TOP_LEFT_CORNER:
					return TBoardConstants.TOP_RIGHT_CORNER;
				case TBoardConstants.TOP_RIGHT_CORNER:
					return TBoardConstants.TOP_LEFT_CORNER;
				case TBoardConstants.BOTTOM_LEFT_CORNER:
					return TBoardConstants.BOTTOM_RIGHT_CORNER;
				case TBoardConstants.BOTTOM_RIGHT_CORNER:
					return TBoardConstants.BOTTOM_LEFT_CORNER;
			}
			if (height < 0) switch (startCorner) {	// Flip over top side
				case TBoardConstants.TOP_LEFT_CORNER:
					return TBoardConstants.BOTTOM_LEFT_CORNER;
				case TBoardConstants.TOP_RIGHT_CORNER:
					return TBoardConstants.BOTTOM_RIGHT_CORNER;
				case TBoardConstants.BOTTOM_LEFT_CORNER:
					return TBoardConstants.TOP_LEFT_CORNER;
				case TBoardConstants.BOTTOM_RIGHT_CORNER:
					return TBoardConstants.TOP_RIGHT_CORNER;
			}
			return startCorner;
		}
		
		public void mouseReleased(MouseEvent e) {
			if (index != -1) {
				cachedBounds = computeBounds(e);
				int cachedStartPoint = computeStartCorner(e);
				vertex.setBounds(cachedBounds);
				TBoardConstants.setStartCorner(vertex.getAttributes(),
						cachedStartPoint);
				CellView[] views = AbstractCellView
						.getDescendantViews(new CellView[] { vertex });
				Map attributes = GraphConstants.createAttributes(views, null);
				graph.getGraphLayoutCache().edit(attributes, null, null, null);
			}
			e.consume();
			cachedBounds = null;
			initialBounds = null;
			firstDrag = true;
		}
	}

	protected static int opositeCorner(int corner) {
		switch (corner) {
			case TBoardConstants.TOP_LEFT_CORNER:
				return TBoardConstants.BOTTOM_RIGHT_CORNER;
			case TBoardConstants.TOP_RIGHT_CORNER:
				return TBoardConstants.BOTTOM_LEFT_CORNER;
			case TBoardConstants.BOTTOM_LEFT_CORNER:
				return TBoardConstants.TOP_RIGHT_CORNER;
			case TBoardConstants.BOTTOM_RIGHT_CORNER:
				return TBoardConstants.TOP_LEFT_CORNER;
		}
		return -1;
	}
}
