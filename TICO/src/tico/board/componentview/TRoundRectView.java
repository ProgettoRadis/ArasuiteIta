/*
 * File: TRoundRectView.java
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

import org.jgraph.graph.CellViewRenderer;

import tico.board.componentredenerer.TRoundRectRenderer;

/**
 * Implementation of a <code>TCell</code> view.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TRoundRectView extends TComponentView {
	/**
	 * <code>TRoundRectRendered</code> that displays the <code>TRoundRect</code> of this view.
	 */
	public static transient TRoundRectRenderer renderer = new TRoundRectRenderer();
	
	/**
	 * Creates a new <code>TRoundRectView</code>.
	 */
	public TRoundRectView() {
		super();
	}

	/**
	 * Creates a new <code>TRoundRectView</code> for the specified <code>roundRect</code>.
	 * 
	 * @param roundRect The specified <code>roundRect</code>
	 */
	public TRoundRectView(Object roundRect) {
		super(roundRect);
	}

	/**
	 * Calculates an appropriate arc for the corners of the rectangle
	 * for boundary size cases of width and height.
	 * 
	 * @return The calculated arc size
	 */
	public static int getArcSize(int width, int height) {
		int arcSize;

		// The arc width of a activity rectangle is 1/5th of the larger
		// of the two of the dimensions passed in, but at most 1/2
		// of the smaller of the two. 1/5 because it looks nice and 1/2
		// so the arc can complete in the given dimension

		if (width <= height) {
			arcSize = height / 5;
			if (arcSize > (width / 2)) {
				arcSize = width / 2;
			}
		} else {
			arcSize = width / 5;
			if (arcSize > (height / 2)) {
				arcSize = height / 2;
			}
		}
			
		return arcSize;
	}

	/* (non-Javadoc)
	 * @see org.jgraph.graph.AbstractCellView#getRenderer()
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
}