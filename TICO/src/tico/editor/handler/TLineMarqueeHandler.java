/*
 * File: TLineMarqueeHandler.java
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

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import org.jgraph.JGraph;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.board.components.TLine;
import tico.editor.TBoardContainer;

/**
 * Marquee handler which allows to insert, in a <code>boardContainer</code>
 * board, a new <code>line</code> of the selected dimension.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TLineMarqueeHandler extends TComponentMarqueeHandler {
	/**
	 * Creates a new <code>TLineMarqueeHandler</code> for the specified
	 * <code>boardContainer</code>.
	 * 
	 * @param boardContainer The specified <code>boardContainer</code>
	 */
	public TLineMarqueeHandler(TBoardContainer boardContainer) {
		super(boardContainer);
	}

	protected TComponent createDefaultComponent(Rectangle2D bounds) {
		TComponent line = new TLine();

		line.getAttributes().applyMap(
				getBoardContainer().getEditor().getCurrentAttributes());

		TBoardConstants.setBounds(line.getAttributes(), bounds);
		TBoardConstants.setStartCorner(line.getAttributes(),
				computeStartCorner());

		return line;
	}

	public void overlay(JGraph graph, Graphics g, boolean clear) {
		if ((startPoint != null) && (currentPoint != null))
			g.drawLine((int)startPoint.getX(), (int)startPoint.getY(),
					(int)currentPoint.getX(), +(int)currentPoint.getY());
	}

	protected int computeStartCorner() {
		if (startPoint.getX() < currentPoint.getX()) {
			if (startPoint.getY() < currentPoint.getY())
				return TBoardConstants.TOP_LEFT_CORNER;
			else
				return TBoardConstants.BOTTOM_LEFT_CORNER;
		} else {
			if (startPoint.getY() < currentPoint.getY())
				return TBoardConstants.TOP_RIGHT_CORNER;
			else
				return TBoardConstants.BOTTOM_RIGHT_CORNER;
		}
	}
}
