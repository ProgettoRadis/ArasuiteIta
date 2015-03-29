/*
 * File: TComponent.java
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

import org.jgraph.graph.DefaultGraphCell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import tico.board.TBoardConstants;
import tico.board.encoding.InvalidFormatException;

/**
 * Defines a <code>TComponent</code> and its attributes. Only subclasses
 * of <code>TComponent</code> can be part of a <code>TBoard</code>.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
abstract public class TComponent extends DefaultGraphCell {
	/**
	 * Generates a new id for this component.
	 * 
	 * @return Returns the new id.
	 */
	public abstract String newId();

	/**
	 * Creates a new <code>TComponent</code>.
	 */
	public TComponent() {
		super();
		
	}

	/**
	 * Creates a new <code>TComponent</code> with the specified initial
	 * <code>id</code>.
	 * 
	 * @param id The specified initial <code>id</code>
	 */
	public TComponent(String id) {
		super();
		TBoardConstants.setId(getAttributes(), id);
	}

	/**
	 * Creates a new <code>TComponent</code> with the specified initial
	 * <code>childs</code>.
	 * 
	 * @param childs The specified initial <code>childs</code>
	 */
	public TComponent(TComponent[] childs) {
		super(null, null, childs);
	}

	/**
	 * Creates a new <code>TComponent</code> with the specified initial
	 * <code>id</code> and <code>childs</code>.
	 * 
	 * @param id The specified initial <code>id</code>
	 * @param childs The specified initial <code>childs</code>
	 */
	public TComponent(String id, TComponent[] childs) {
		super(null, null, childs);
		TBoardConstants.setId(getAttributes(), id);
	}

	/**
	 * Returns the component <code>id</code>.
	 * 
	 * @return The component <code>id</code>
	 */
	public String getId() {
		return TBoardConstants.getId(getAttributes());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getId();
	}

	/**
	 * Generates an XML <code>Element</code> that contains the component information.
	 * 
	 * @param doc The <code>Document</code> represents that represents the
	 * entire XML document
	 * @return The XML <code>Element</code> generated
	 */
	abstract public Node XMLEncode(Document doc);
	
	/**
	 * Returns a <code>TComponent</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the component data
	 * @return The generated <code>TComponent</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static TComponent XMLDecodeType(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("component")) {
			// Get the attribute type
			String type = element.getAttribute("type");
			// Depending on the type, you have to return a different component
			if (type.equals(TCell.TYPE)) {
				return TCell.XMLDecode(element);
			} else if (type.equals(TControllerCell.TYPE)) {
				return TControllerCell.XMLDecode(element);
			} else if (type.equals(TGrid.TYPE)) {
				return TGrid.XMLDecode(element);
			} else if (type.equals(TLabel.TYPE)) {
				return TLabel.XMLDecode(element);
			} else if (type.equals(TLine.TYPE)) {
				return TLine.XMLDecode(element);
			} else if (type.equals(TOval.TYPE)) {
				return TOval.XMLDecode(element);
			} else if (type.equals(TRectangle.TYPE)) {
				return TRectangle.XMLDecode(element);
			} else if (type.equals(TRoundRect.TYPE)) {
				return TRoundRect.XMLDecode(element);
			} else if (type.equals(TTextArea.TYPE)) {
				return TTextArea.XMLDecode(element);
			} 
 		}
		return null;
	}
}