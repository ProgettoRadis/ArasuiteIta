/*
 * File: TGridCell.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Apr 17, 2006
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

package tico.board.components;

import org.jgraph.graph.AttributeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import tico.board.TBoardConstants;
import tico.board.encoding.InvalidFormatException;
import tico.board.encoding.TAttributeEncoder;

/**
 * Defines a <code>TGridCell</code> and its attributes. A <code>TGridCell</code>
 * is allways part of a <code>TGrid</code>.
 *
 * @author Pablo Muñoz
 * @version 1.1 May 09, 2009
 */
public class TGridCell extends TComponent {
	/**
	 * The <code>TGridCell</code> type used as attribute in XML encoding and decoding.
	 */
//	public static String TYPE = "gridCell";
	public static String TYPE = "cell";
	
//	private static int gridCellCount = 0;
	
	/**
	 * Creates a new <code>TGridCell</code>.
	 */
	public TGridCell() {
		super();
		/*Eliminamos el código al hacerle heredar de TCell*/
		setAllowsChildren(false);

		TBoardConstants.setTextSender(getAttributes(), true);
//		TBoardConstants.setBrowseable(getAttributes(), false);
		TBoardConstants.setBrowseable(getAttributes(), true);//mio
		/**/
		
	}
	
	/**
	 * Creates a new <code>TGridCell</code> with the specified initial
	 * <code>row</code> and <code>column</code> attributes.
	 * 
	 * @param row The specified initial <code>row</code>
	 * @param column The specified initial <code>column</code>
	 */
	public TGridCell(int row,int column) {
		super();
//		setAllowsChildren(false);
		
		TBoardConstants.setColumn(getAttributes(),column);
		TBoardConstants.setRow(getAttributes(),row);
		TBoardConstants.setTextSender(getAttributes(), true);
//		TBoardConstants.setBrowseable(getAttributes(), false);
		TBoardConstants.setBrowseable(getAttributes(), true);//mio
		TBoardConstants.setId(getAttributes(), newId());//nuevo mio
		
	}
	
	/* (non-Javadoc)
	 * @see tico.board.components.TComponent#newId()
	 */
	/*//eliminamos al hacerlo heredar de TCell*/
	public String newId() {
    	//return "grid_cell_" + new Integer(gridCellCount++);
		return "cell_" + new Integer(TCell.cellCount++);
	}/**/
	
	/* (non-Javadoc)
	 * @see tico.board.components.TComponent#getId()
	 */
	public String getId() {
/*		return TBoardConstants.getId(((TGrid)getParent()).getAttributes()) +
				"(" + TBoardConstants.getRow(getAttributes()) + "," +
				TBoardConstants.getColumn(getAttributes()) + ")";
*/
		return TBoardConstants.getId(getAttributes());
	}
	
	/**
	 * Returns the grid cell parent <code>grid</code>.
	 * 
	 * @return The grid cell parent <code>grid</code>
	 */
	public TGrid getGrid() {		
		return (TGrid)getParent();
	}

	/* (non-Javadoc)
	 * @see tico.board.components.TComponent#XMLEncode(org.w3c.dom.Document)
	 */
	public Node XMLEncode(Document doc) {
		// Create component node
		Element componentElement = doc.createElement("component");
		componentElement.setAttribute("type", TYPE);

		// Delete default attributes
		AttributeMap map = (AttributeMap)getAttributes().clone();
		map.remove(TBoardConstants.TEXT_SENDER);
		// Append component attributes
		componentElement.appendChild(TAttributeEncoder.XMLEncode(map, doc));
		
		// Return the component node
		return componentElement;
	}
	
	/**
	 * Returns a <code>TGridCell</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the cell data
	 * @return The generated <code>TGridCell</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static TGridCell XMLDecode(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("component")
				&& element.getAttribute("type").equals(TYPE)) {
			// Create the empty gridCell
			TGridCell gridCell = new TGridCell();
			// Get the attributes
			Node attributesNode = element.getElementsByTagName("attributes")
					.item(0);
			// Apply attributes to the gridCell
			gridCell.getAttributes().applyMap(TAttributeEncoder
					.XMLDecode((Element)attributesNode));
			// Return it
			return gridCell;
		}
		throw new InvalidFormatException();
	}
}
