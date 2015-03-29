/*
 * File: TOvalRenderer.java
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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphConstants;

import tico.board.TBoardConstants;

/**
 * <code>Swing</code> which implements the <code>TOvalRenderer</code> visualization.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TOvalRenderer extends TComponentRenderer {
	
	transient protected Color gradientColor;
	
	transient protected Color backgroundColor;
	
	transient protected Color borderColor;
	
	transient protected int borderWidth;
	
	/* (non-Javadoc)
	 * @see org.jgraph.graph.VertexRenderer#installAttributes(org.jgraph.graph.CellView)
	 */
	protected void installAttributes(CellView view) {
		// Get the oval attributes
		Map map = view.getAllAttributes();
		
		// Apply all the component properties		
		// Apply border
		borderColor = TBoardConstants.getBorderColor(map);
		if (borderColor != null)
			borderWidth = Math.max(1, Math.round(TBoardConstants.getLineWidth(map)));
		else borderWidth = 0;
		
		// Apply background and gradient
		backgroundColor = TBoardConstants.getBackground(map);
		setOpaque((backgroundColor != null));

		gradientColor = TBoardConstants.getGradientColor(map);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		Dimension dimension = getSize();
		
		boolean tmp = selected;
		
		if (isOpaque()) {
			g.setColor(backgroundColor);
			if (gradientColor != null) {
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, backgroundColor,
						getWidth(), getHeight(), gradientColor, true));
			}
			g.fillOval(borderWidth, borderWidth,
					dimension.width - 2*borderWidth,
					dimension.height - 2*borderWidth);
		}
		
		try {
			setBorder(null);
			setOpaque(false);
			selected = false;
			super.paint(g);
		} finally {
			selected = tmp;
		}
		
		if (borderColor != null) {
			g.setColor(borderColor);
			g2.setStroke(new BasicStroke(borderWidth));
			g.drawOval(borderWidth - 1, borderWidth - 1,
					dimension.width - 2*borderWidth,
					dimension.height - 2*borderWidth);
		}
		
		if (selected) {
			g2.setStroke(GraphConstants.SELECTION_STROKE);
			g.setColor(getHighlightColor());
			g.drawOval(borderWidth/2, borderWidth/2,
					dimension.width - borderWidth - 1,
					dimension.height - borderWidth - 1);
		}
	}
}
