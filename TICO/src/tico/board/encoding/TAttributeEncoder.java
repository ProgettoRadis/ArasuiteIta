/*
 * File: TAttributeEncoder.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Oct 6, 2006
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

package tico.board.encoding;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.AttributeMap.SerializableRectangle2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tico.components.resources.TFileUtils;
import tico.editor.TFileHandler;
import tico.editor.TProjectHandler;

/**
 * Static class which implements functions to encode and decode attibute maps
 * to XML.
 * 
 * @author Pablo Mu�oz
 * @version 1.0 Nov 20, 2006
 */ 
public class TAttributeEncoder {
	// Attribute names
	private final static String TYPE_ATTRIBUTE = "type";
	private final static String KEY_ATTRIBUTE = "key";

	// Type names
	private final static String COLOR_TYPE = "color";
	private final static String FONT_TYPE = "font";
	private final static String RECTANGLE_TYPE = "rectangle";
	private final static String SIZE_TYPE = "size";
	private final static String LIST_TYPE = "list";
	private final static String STRING_TYPE = "string";
	private final static String FLOAT_TYPE = "float";
	private final static String INTEGER_TYPE = "integer";
	private final static String BOOLEAN_TYPE = "boolean";
	private final static String ICON_TYPE = "icon";
	private final static String FILE_TYPE = "file";

	/**
	 * Generates an XML <code>Element</code> that contains the
	 * <code>attributeMap</code> data.
	 * 
	 * @param map The <code>attributeMap</code> to encode
	 * @param doc The <code>Document</code> that represents the
	 * entire XML document
	 * @return The XML <code>Element</code> generated
	 */
	public static Node XMLEncode(AttributeMap map, Document doc) {
		// Create attributes node
		Element attributesElement = doc.createElement("attributes");

		// Append each attribute map element
		Iterator attributeIterator = map.entrySet().iterator();
		while (attributeIterator.hasNext()) {
			attributesElement.appendChild(writeAttribute(
					(Map.Entry)attributeIterator.next(), doc));
		}

		// Return the attributes node
		return attributesElement;
	}

	/**
	 * Returns an <code>attributeMap</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the
	 * <code>attributeMap</code> data
	 * @return The generated <code>attributeMap</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static AttributeMap XMLDecode(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("attributes")) {
			// Create an empty attribute map
			AttributeMap map = new AttributeMap();
			// Set all attributes
			NodeList attributeNodeList = element.getChildNodes();
			for (int i = 0; i < attributeNodeList.getLength(); i++) {
				Node attributeNode = attributeNodeList.item(i);
				if (attributeNode.getNodeType() == Node.ELEMENT_NODE)
					map.putAll(readAttribute((Element)attributeNode));
			}
			// Return the map
			return map;
		}
		throw new InvalidFormatException();
	}

	/**
	 * Generates an XML <code>Node</code> that contains one attributeMap
	 * <code>attribute</code> data.
	 * 
	 * @param entry The attributeMap <code>attribute</code> to encode
	 * @param doc The <code>Document</code> that represents the
	 * entire XML document
	 * @return The XML <code>Element</code> generated
	 */
	private static Node writeAttribute(Map.Entry entry, Document doc) {
		// Create attribute node
		Element attributesElement = doc.createElement("attribute");
		// Get the attribute key and its value
		String key = (String)entry.getKey();
		Object value = entry.getValue();

		// Set attribute type
		attributesElement.setAttribute(KEY_ATTRIBUTE, key);

		if (value instanceof Color) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, COLOR_TYPE);
			attributesElement.appendChild(doc.createTextNode(Integer
					.toHexString(((Color)value).getRGB())));
		} else if (value instanceof Font) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, FONT_TYPE);

			Font font = (Font)value;
			Element familyElement = doc.createElement("family");
			familyElement
					.appendChild(doc.createTextNode("" + font.getFamily()));
			Element sizeElement = doc.createElement("size");
			sizeElement.appendChild(doc.createTextNode("" + font.getSize()));
			Element boldElement = doc.createElement("bold");
			Element italicElement = doc.createElement("italic");

			attributesElement.appendChild(familyElement);
			attributesElement.appendChild(sizeElement);
			if (font.isBold())
				attributesElement.appendChild(boldElement);
			if (font.isItalic())
				attributesElement.appendChild(italicElement);
		} else if (value instanceof SerializableRectangle2D) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, RECTANGLE_TYPE);

			Rectangle2D rectangle = (Rectangle2D)value;
			Element xElement = doc.createElement("x");
			xElement.appendChild(doc.createTextNode("" + rectangle.getX()));
			Element yElement = doc.createElement("y");
			yElement.appendChild(doc.createTextNode("" + rectangle.getY()));
			Element widthElement = doc.createElement("width");
			widthElement.appendChild(doc.createTextNode(""
					+ rectangle.getWidth()));
			Element heightElement = doc.createElement("height");
			heightElement.appendChild(doc.createTextNode(""
					+ rectangle.getHeight()));

			attributesElement.appendChild(xElement);
			attributesElement.appendChild(yElement);
			attributesElement.appendChild(widthElement);
			attributesElement.appendChild(heightElement);
		} else if (value instanceof Dimension) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, SIZE_TYPE);

			Dimension dimension = (Dimension)value;
			Element widthElement = doc.createElement("width");
			widthElement.appendChild(doc.createTextNode(Double
					.toString(dimension.getWidth())));
			Element heightElement = doc.createElement("height");
			heightElement.appendChild(doc.createTextNode(Double
					.toString(dimension.getHeight())));

			attributesElement.appendChild(widthElement);
			attributesElement.appendChild(heightElement);
		} else if (value instanceof Object[]) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, LIST_TYPE);
			// For each element in the list
			for (int i = 0; i < ((Object[])value).length; i++) {
				Node elementNode = doc.createElement("element");
				elementNode.appendChild(doc.createTextNode(((Object[])value)[i]
						.toString()));
				attributesElement.appendChild(elementNode);
			}
		} else if (value instanceof Float) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, FLOAT_TYPE);
			attributesElement.appendChild(doc.createTextNode(value.toString()));
		} else if (value instanceof Integer) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, INTEGER_TYPE);
			attributesElement.appendChild(doc.createTextNode(value.toString()));
		} else if (value instanceof Boolean) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, BOOLEAN_TYPE);
			attributesElement.appendChild(doc.createTextNode(value.toString()));
		} else if (value instanceof Icon) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, ICON_TYPE);
			String partialPath = "image"+ File.separator + ((ImageIcon)value).getDescription().substring(((ImageIcon)value).getDescription().lastIndexOf(File.separator)+1);
			attributesElement.appendChild(doc.createTextNode(partialPath));
		} else if (value instanceof File) {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, FILE_TYPE);
			String partialPath = TFileHandler.convertToPartial(((File)value)
					.getAbsolutePath());
			attributesElement.appendChild(doc.createTextNode(partialPath));
		} else {
			attributesElement.setAttribute(TYPE_ATTRIBUTE, STRING_TYPE);
			attributesElement.appendChild(doc.createTextNode(value.toString()));
		}

		// Return the attribute node
		return attributesElement;
	}

	/**
	 * Returns a <code>map</code> that contains the attributeMap
	 * <code>attribute</code> contained in the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the
	 * attributeMap <code>attribute</code>
	 * @return A <code>map</code> that contains the attributeMap <code>attribute</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	private static Map readAttribute(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("attribute")) {
			// Create an empty attribute map
			Map attribute = new AttributeMap();
			Object value = null;

			if (!element.hasAttribute(KEY_ATTRIBUTE)
					|| !element.hasAttribute(TYPE_ATTRIBUTE))
				throw new InvalidFormatException();
			// Get the type
			String type = element.getAttribute(TYPE_ATTRIBUTE);

			// Create the map depending on the type
			if (type.equals(COLOR_TYPE)) {
				// Get the color value
				String hexaValue = element.getChildNodes().item(0)
						.getNodeValue();
				value = new Color((int)Long.parseLong(hexaValue, 16));
			} else if (type.equals(FONT_TYPE)) {
				// Get the font value
				// Get family value
				NodeList familyElementList = element
						.getElementsByTagName("family");
				if (familyElementList.getLength() != 1)
					throw new InvalidFormatException();
				String family = familyElementList.item(0).getChildNodes().item(
						0).getNodeValue();
				// Get style value
				int style = 0;
				if (element.getElementsByTagName("bold").getLength() == 1)
					style |= Font.BOLD;
				if (element.getElementsByTagName("italic").getLength() == 1)
					style |= Font.ITALIC;
				// Get size value
				NodeList sizeElementList = element.getElementsByTagName("size");
				if (sizeElementList.getLength() != 1)
					throw new InvalidFormatException();
				String sizeValue = sizeElementList.item(0).getChildNodes()
						.item(0).getNodeValue();
				int size = Integer.parseInt(sizeValue);
				// Create the font
				value = new Font(family, style, size);
			} else if (type.equals(RECTANGLE_TYPE)) {
				// Get the rectangle values
				// Get x value
				NodeList xElementList = element.getElementsByTagName("x");
				if (xElementList.getLength() != 1)
					throw new InvalidFormatException();
				String xValue = xElementList.item(0).getChildNodes().item(0)
						.getNodeValue();
				double x = Double.parseDouble(xValue);
				// Get y value
				NodeList yElementList = element.getElementsByTagName("y");
				if (yElementList.getLength() != 1)
					throw new InvalidFormatException();
				String yValue = yElementList.item(0).getChildNodes().item(0)
						.getNodeValue();
				double y = Double.parseDouble(yValue);
				// Get width value
				NodeList widthElementList = element
						.getElementsByTagName("width");
				if (widthElementList.getLength() != 1)
					throw new InvalidFormatException();
				String widthValue = widthElementList.item(0).getChildNodes()
						.item(0).getNodeValue();
				double width = Double.parseDouble(widthValue);
				// Get height value
				NodeList heightElementList = element
						.getElementsByTagName("height");
				if (heightElementList.getLength() != 1)
					throw new InvalidFormatException();
				String heightValue = heightElementList.item(0).getChildNodes()
						.item(0).getNodeValue();
				double height = Double.parseDouble(heightValue);
				// Create the rectangle
				value = new Rectangle2D.Double(x, y, width, height);
			} else if (type.equals(SIZE_TYPE)) {
				// Get the dimension values
				// Get width value
				NodeList widthElementList = element
						.getElementsByTagName("width");
				if (widthElementList.getLength() != 1)
					throw new InvalidFormatException();
				String widthValue = widthElementList.item(0).getChildNodes()
						.item(0).getNodeValue();
				double width = Double.parseDouble(widthValue);
				// Get height value
				NodeList heightElementList = element
						.getElementsByTagName("height");
				if (heightElementList.getLength() != 1)
					throw new InvalidFormatException();
				String heightValue = heightElementList.item(0).getChildNodes()
						.item(0).getNodeValue();
				double height = Double.parseDouble(heightValue);
				// Create the dimension
				Dimension dimension = new Dimension();
				dimension.setSize(width, height);
				value = dimension;
			} else if (type.equals(LIST_TYPE)) {
				NodeList elementNodeList = element
						.getElementsByTagName("element");
				Object[] objectList = new Object[elementNodeList.getLength()];
				for (int i = 0; i < objectList.length; i++)
					objectList[i] = elementNodeList.item(i).getChildNodes()
							.item(0).getNodeValue();
				value = objectList;
			} else if (type.equals(FLOAT_TYPE)) {
				value = new Float(Float.parseFloat(element.getChildNodes()
						.item(0).getNodeValue()));
			} else if (type.equals(INTEGER_TYPE)) {
				value = new Integer(Integer.parseInt(element.getChildNodes()
						.item(0).getNodeValue()));
			} else if (type.equals(STRING_TYPE)) {
				if (element.getChildNodes().getLength() == 0)
					value = "";
				else
					value = element.getChildNodes().item(0).getNodeValue();
			} else if (type.equals(BOOLEAN_TYPE)) {
				value = Boolean.valueOf(element.getChildNodes().item(0)
						.getNodeValue());
			} else if (type.equals(ICON_TYPE)) {
				String partialPath = element.getChildNodes().item(0)
						.getNodeValue();
				partialPath = partialPath.replace('\\','/');
				File imageFile;
				try {
					imageFile = TFileHandler.importFile(new File(TProjectHandler
							.getTempDirectory(), partialPath));
					
				} catch (IOException e) {
					throw new InvalidFormatException();
				}
				// Test if need to be loaded with JAI libraries (different
				// format than JPG, PNG and GIF)
				if (TFileUtils.isJAIRequired(imageFile)) {
					// Load it with JAI class
					RenderedOp src = JAI.create("fileload", imageFile
							.getAbsolutePath());
					BufferedImage bufferedImage = src.getAsBufferedImage();
					value = new ImageIcon(bufferedImage, imageFile
							.getAbsolutePath());
				} else {
					// Create it as usual
					value = new ImageIcon(imageFile.getAbsolutePath(),
							imageFile.getAbsolutePath());
				}
			} else if (type.equals(FILE_TYPE)) {
				String partialPath = element.getChildNodes().item(0)
						.getNodeValue();
				partialPath = partialPath.replace('\\','/');
				try {
					value = TFileHandler.importFile(new File(TProjectHandler
							.getTempDirectory(), partialPath));
				} catch (Exception e) {
					throw new InvalidFormatException();
				}
			}

			if (value != null)
				attribute.put(element.getAttribute(KEY_ATTRIBUTE), value);
			else
				throw new InvalidFormatException();

			// Return the attribute map
			return attribute;
		}
		return null;
	}
	
	/**
	 * Returns all the file paths that appears in a XML document.
	 * 
	 * @param doc The XML document
	 * @return A vector with all the file paths that apear in <code>doc</code>
	 */
	public static String[] getFilePaths(Document doc) {
		Vector filePaths = new Vector();

		// Get all attributes element in the document
		NodeList attributesNodeList = doc.getElementsByTagName("attributes");
		for (int i = 0; i < attributesNodeList.getLength(); i++) {
			// Get all attributes in the attribute element
			NodeList attributeNodeList = attributesNodeList.item(i)
					.getChildNodes();
			for (int j = 0; j < attributeNodeList.getLength(); j++) {
				if (attributeNodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element attributeNode = (Element)attributeNodeList.item(j);
					if (attributeNode.getTagName().equals("attribute")
							&& attributeNode.hasAttribute(KEY_ATTRIBUTE)
							&& attributeNode.hasAttribute(TYPE_ATTRIBUTE)) {

						// Get the type
					
						String type = attributeNode
								.getAttribute(TYPE_ATTRIBUTE);
						// Depending on the type
						
						if (type.equals(ICON_TYPE)) {
									
							String partialPath = attributeNode.getChildNodes()
									.item(0).getNodeValue(); 
							filePaths.add(partialPath);
						}
							else
								if (type.equals(FILE_TYPE)) {
									
									
							String partialPath = attributeNode.getChildNodes()
									.item(0).getNodeValue();
							filePaths.add(partialPath);
						}
					}
				}
			}
		}
	
		String[] paths = new String[filePaths.size()];
		for (int i = 0; i < filePaths.size(); i++)
			paths[i] = (String)filePaths.get(i);
		
		return paths;
	}
}
