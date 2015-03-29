/*
 * File: TOvalView.java
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

import tico.board.componentredenerer.TOvalRenderer;

/**
 * Implementation of a <code>TOvalView</code> view.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TOvalView extends TComponentView {
	/**
	 * <code>TOvalRendered</code> that displays the <code>TOvalView</code> of this view.
	 */
	public static transient TOvalRenderer renderer = new TOvalRenderer();
	
	/**
	 * Creates a new <code>TOvalView</code>.
	 */
	public TOvalView() {
		super();
	}

	/**
	 * Creates a new <code>TOvalView</code> for the specified <code>oval</code>.
	 * 
	 * @param oval The specified <code>oval</code>
	 */
	public TOvalView(Object oval) {
		super(oval);
	}

	/* (non-Javadoc)
	 * @see org.jgraph.graph.AbstractCellView#getRenderer()
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
}
