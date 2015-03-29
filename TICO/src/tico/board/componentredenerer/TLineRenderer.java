/*
 * File: TLineRenderer.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 6, 2006
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphConstants;

import tico.board.TBoardConstants;

/**
 * <code>Swing</code> which implements the <code>TLineRenderer</code> visualization.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TLineRenderer extends TComponentRenderer {
	transient protected Color borderColor;

	transient protected int lineWidth;

	transient protected int startCorner;

	/* (non-Javadoc)
	 * @see org.jgraph.graph.VertexRenderer#installAttributes(org.jgraph.graph.CellView)
	 */
	protected void installAttributes(CellView view) {
		Map map = view.getAllAttributes();

		startCorner = TBoardConstants.getStartCorner(map);

		borderColor = TBoardConstants.getBorderColor(map);
		if (borderColor == null)
			borderColor = TBoardConstants.DEFAULT_FOREGROUND;

		lineWidth = Math.max(1, Math.round(TBoardConstants.getLineWidth(map)));
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		boolean tmp = selected;

		try {
			setBorder(null);
			setOpaque(false);
			selected = false;
			super.paint(g);
		} finally {
			selected = tmp;
		}

		if (borderColor != null) {
			g2.setStroke(new BasicStroke(lineWidth));
			g.setColor(borderColor);
			drawThisLine(g);
		}

		if (selected) {
			g2.setStroke(GraphConstants.SELECTION_STROKE);
			g.setColor(getHighlightColor());
			drawThisLine(g);
		}
	}

	// Function to draw the line
	private void drawThisLine(Graphics g) {
		Dimension dimension = getSize();

		switch (startCorner) {
		case TBoardConstants.TOP_LEFT_CORNER:
			g.drawLine(lineWidth / 2, lineWidth / 2, dimension.width
					- (lineWidth / 2), dimension.height - (lineWidth / 2));
			break;
		case TBoardConstants.TOP_RIGHT_CORNER:
			g.drawLine(dimension.width - (lineWidth / 2), lineWidth / 2,
					lineWidth / 2, dimension.height - (lineWidth / 2));
			break;
		case TBoardConstants.BOTTOM_LEFT_CORNER:
			g.drawLine(lineWidth / 2, dimension.height - (lineWidth / 2),
					dimension.width - (lineWidth / 2), lineWidth / 2);
			break;
		case TBoardConstants.BOTTOM_RIGHT_CORNER:
			g.drawLine(dimension.width - (lineWidth / 2), dimension.height
					- (lineWidth / 2), lineWidth / 2, lineWidth / 2);
		}
	}
}
