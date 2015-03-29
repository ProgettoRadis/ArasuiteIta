/*
 * File: TControllerCell.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date:	Dec, 2009
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
 * Defines a <code>TControllerCell</code> and its attributes.
 *
 * @author Carolina Palacio
 * @version e1.0 Dec, 2009
 */
public class TControllerCell extends TComponent {
	/**
	 * The <code>TCell</code> type used as attribute in XML encoding and decoding.
	 */
	public static String TYPE = "controllerCell";

	private static int cellCount = 0;

	/**
	 * Creates a new <code>TCell</code>.
	 */
	public TControllerCell() {
		super();
		setAllowsChildren(false);
		TBoardConstants.setTextSender(getAttributes(), true);
		TBoardConstants.setBrowseable(getAttributes(), true);
	}

	/* (non-Javadoc)
	 * @see tico.board.components.TComponent#newId()
	 */
	public String newId() {
		return "controller_cell_" + new Integer(cellCount++);
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
		map.remove(TBoardConstants.BROWSEABLE);
		// Append component attributes
		componentElement.appendChild(TAttributeEncoder.XMLEncode(map, doc));
		
		// Return the component node
		return componentElement;
	}

	/**
	 * Returns a <code>TCell</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the cell data
	 * @return The generated <code>TCell</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static TControllerCell XMLDecode(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("component")
				&& element.getAttribute("type").equals(TYPE)) {
			// Create the empty cell
			TControllerCell cell = new TControllerCell();
			// Get the attributes
			Node attributesNode = element.getElementsByTagName("attributes")
					.item(0);
			// Apply attributes to the cell
			cell.getAttributes().applyMap(TAttributeEncoder
					.XMLDecode((Element)attributesNode));
			// Return it
			return cell;
		}
		return null;
	}
}