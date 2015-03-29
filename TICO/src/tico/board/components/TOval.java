/*
 * File: TOval.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date:	16-Nov-2005 
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

import tico.board.encoding.InvalidFormatException;
import tico.board.encoding.TAttributeEncoder;

/**
 * Defines a <code>TCell</code> and its attributes.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TOval extends TComponent {
	/**
	 * The <code>TOval</code> type used as attribute in XML encoding and decoding.
	 */
	public static String TYPE = "oval";
	
	private static int ovalCount = 0;
	
	/**
	 * Creates a new <code>TOval</code>.
	 */
	public TOval() {
		super();
		setAllowsChildren(false);
	}	
	
    /* (non-Javadoc)
     * @see tico.board.components.TComponent#newId()
     */
	public String newId() {
    	return "oval_" + new Integer(ovalCount++);
    }

	public Node XMLEncode(Document doc) {
		// Create component node
		Element componentElement = doc.createElement("component");
		componentElement.setAttribute("type", TYPE);

		// Delete default attributes
		AttributeMap map = (AttributeMap)getAttributes().clone();
		// Append component attributes
		componentElement.appendChild(TAttributeEncoder.XMLEncode(map, doc));
		
		// Return the component node
		return componentElement;
	}

	/**
	 * Returns a <code>TOval</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the oval data
	 * @return The generated <code>TOval</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static TOval XMLDecode(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("component")
				&& element.getAttribute("type").equals(TYPE)) {
			// Create the empty oval
			TOval oval = new TOval();
			// Get the attributes
			Node attributesNode = element.getElementsByTagName("attributes")
					.item(0);
			// Apply attributes to the oval
			oval.getAttributes().applyMap(TAttributeEncoder
					.XMLDecode((Element)attributesNode));
			// Return it
			return oval;
		}
		return null;
	}
}
