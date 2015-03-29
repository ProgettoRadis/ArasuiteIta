/*
 * File: TViewFactory.java
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

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

import tico.board.components.TCell;
import tico.board.components.TControllerCell;
import tico.board.components.TGridCell;
import tico.board.components.TLabel;
import tico.board.components.TLine;
import tico.board.components.TOval;
import tico.board.components.TRectangle;
import tico.board.components.TRoundRect;
import tico.board.components.TTextArea;

/**
 * Implementation of a cell view factory that returns the default view for
 * each component type.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TViewFactory extends DefaultCellViewFactory {
	/**
	 * Constructs a <code>TComponentView</code> view for the specified
	 * <code>component</code>.
	 * 
	 * @param component The specified <code>component</code>
	 * @return The constructed <code>TComponentView</code>
	 */
	protected VertexView createVertexView(Object component) {
		if (component instanceof TCell)
			return new TCellView(component);
		if (component instanceof TControllerCell)
			return new TControllerCellView(component);
		if (component instanceof TGridCell)
			return new TGridCellView(component);
		if (component instanceof TOval)
			return new TOvalView(component);
		if (component instanceof TRectangle)
			return new TRectangleView(component);
		if (component instanceof TRoundRect)
			return new TRoundRectView(component);
		if (component instanceof TTextArea)
			return new TTextAreaView(component);
		if (component instanceof TLine)
			return new TLineView(component);
		if (component instanceof TLabel)
			return new TLabelView(component);
		return super.createVertexView(component);
	}
}
