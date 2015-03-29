/*
 * File: TComponentRenderer.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 3, 2006
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

package tico.board.componentredenerer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

import tico.board.TBoardConstants;

/**
 * <code>Swing</code> which implements the components visualization. Is created
 * from <code>VertexRendered</code> but implements again the
 * <code>getRenderedComponent</code> method in order to avoid
 * <code>UserObject</code> usage.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TComponentRenderer extends VertexRenderer {
	protected final static Font ID_FONT = TBoardConstants.DEFAULTFONT;
	
	/* (non-Javadoc)
	 * @see org.jgraph.graph.CellViewRenderer#getRendererComponent(org.jgraph.JGraph, org.jgraph.graph.CellView, boolean, boolean, boolean)
	 */
	public Component getRendererComponent(JGraph graph, CellView view,
			boolean sel, boolean focus, boolean preview) {
		gridColor = graph.getGridColor();
		highlightColor = graph.getHighlightColor();
		lockedHandleColor = graph.getLockedHandleColor();
		isDoubleBuffered = graph.isDoubleBuffered();
		if (view instanceof VertexView) {
			this.view = (VertexView)view;
			this.hasFocus = focus;
			this.childrenSelected = graph.getSelectionModel()
					.isChildrenSelected(view.getCell());
			this.selected = sel;
			this.preview = preview;
			if (this.view.isLeaf()
					|| GraphConstants.isGroupOpaque(view.getAllAttributes()))
				installAttributes(view);
			else
				resetAttributes();
			return this;
		}
		return null;
	}
	
	// Returns the highlightColor
	protected Color getHighlightColor() {
		return highlightColor;
	}
}
