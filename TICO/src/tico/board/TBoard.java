/*
 * File: TBoard.java
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

package tico.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.AttributeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tico.board.encoding.InvalidFormatException;
import tico.board.events.BoardChangeEvent;
import tico.board.events.BoardChangeListener;

/**
 * A control that displays a communication board.
 * 
 * A <code>TBoard</code> object doesn't actually contain your data; it simply
 * provides a view of the data. Like any non-trivial <code>Swing</code>
 * component, the board gets data by querying its data model.
 *
 * <code>TBoard</code> displays its data by drawing individual elements. Each
 * element displayed by the board contains exactly one item of data, which is
 * called a component.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoard extends JGraph {
	// Number of boards created during execution
	private static int boardCount = 0;
	
	// Name of the board
	private String name = null;
	
	/**
	 * Board border color which is used to mark the orginialSize border
	 */
	public final static Color DEFAULT_BORDER_COLOR = Color.BLACK;

	/**
	 * Creates a new empty <code>TBoard</code>.
	 */
	public TBoard() {
		this(new TBoardModel());
	}

	/**
	 * Creates a new empty <code>TBoard</code> with the specified initial
	 * <code>name</code>.
	 * 
	 * @param name The specified initial <code>name</code>
	 */
	public TBoard(String name) {
		this(new TBoardModel());
		
		this.name = name;
	}
	
	/**
	 * Creates a new empty <code>TBoard</code> with the specified initial
	 * <code>attributes</code>.
	 * 
	 * @param attributes The specified initial <code>attributes</code>

	 */
	public TBoard(AttributeMap attributes) {
		this(new TBoardModel(attributes));
	}
	
	/**
	 * Creates a new empty <code>TBoard</code> with the specified initial
	 * <code>attributes</code> and <code>name</code>.
	 * 
	 * @param attributes The specified initial <code>attributes</code>
	 * @param name The specified initial <code>name</code>
	 */
	public TBoard(AttributeMap attributes, String name) {
		this(attributes);
		
		this.name = name;
	}

	/**
	 * Creates a new <code>TBoard</code> with the specified initial
	 * <code>model</code> and <code>name</code>.
	 * 
	 * @param model The specified initial <code>model</code>
	 * @param name The specified initial <code>name</code>
	 */
	public TBoard(TBoardModel model, String name) {
		this(model);
		
		this.name = name;
	}
	
	/**
	 * Creates a new <code>TBoard</code> with the specified initial
	 * <code>model</code>.
	 * 
	 * @param model The specified initial <code>model</code>
	 */
	public TBoard(TBoardModel model) {
		super();
		
		// Set the Tolerance to 2 Pixel
		setTolerance(2);
		// Allows resize
		setSizeable(true);
		// Allows control-drag
		setCloneable(false);
		// Does not allow connections
		setConnectable(false);
		setDisconnectable(false);
		setBendable(false);
		// Does not allow in-place editing
		setEditable(false);
		// Set anti-aliased
		setAntiAliased(true);
		// Set layout cache
		setGraphLayoutCache(new TBoardLayoutCache());
		// Set model
		model.addGraphModelListener(new GraphModelListener() {
			public void graphChanged(GraphModelEvent e) {
				// Check if model size has been modified and update the board
				// minimum size
				Map attributes = (Map)e.getChange().getAttributes();
				if (attributes != null)
					attributes = (Map)attributes.get(getModel());
				if (attributes != null)
					if (attributes.containsKey(TBoardConstants.SIZE))
						setMinimumSize(TBoardConstants.getSize(attributes));
				
				// Fire change
				fireBoardChange();
			}
		});
		setModel(model);
		// Set minimum size
		setMinimumSize(TBoardConstants.getSize(getAttributes(null)));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getBoardName();
	}
	
	/**
	 * Returns the board <code>name</code>.
	 * 
	 * @return The board <code>name</code>
	 */
	public String getBoardName() {
		return name;
	}

	/**
	 * Sets the board <code>name</code>.
	 * 
	 * @param name The board <code>name</code> to set
	 */
	public void setBoardName(String name) {
		this.name = name;
	}
		
	/**
	 * Creates a new board <code>name</code> different from any other board
	 * <code>name</code> created with this function.
	 * 
	 * @return The new board <code>name</code>
	 */
	public static String newBoardName() {
		return "board_" + boardCount++;
	}
	
    /* (non-Javadoc)
     * @see javax.swing.JComponent#updateUI()
     */
    public void updateUI() {
        setUI(new TBoardUI());
        invalidate();
    }

	// Sends the BoardChangeEvent to all its BoardChangeListeners
	private void fireBoardChange() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == BoardChangeListener.class) {
				((BoardChangeListener)listeners[i + 1])
						.boardChanged(new BoardChangeEvent(this));
			}
		}
	}   

	/**
	 * Adds an <code>BoardChangeListener</code>.
	 * 
	 * The <code>BoardChangeListener</code>. will receive a
	 * <code>BoardChangeEvent</code> when any board change has been made.
	 * 
	 * @param listener The <code>BoardChangeListener</code> that is to be notified
	 */	
	public void addBoardChangeListener(BoardChangeListener listener) {
		// Add to the listeners list
		listenerList.add(BoardChangeListener.class, listener);
	}

	/**
	 * Removes a <code>BoardChangeListener</code>.
	 * 
	 * @param listener The <code>BoardChangeListener</code> to remove
	 */	
	public void removeBoardChangeListener(BoardChangeListener listener) {
		// Remove from the listeners list
		listenerList.remove(BoardChangeListener.class, listener);
	}

	/**
	 * Returns a <code>BufferedImage</code> that represents the board.
	 * 
	 * @return The <code>BufferedImage</code> that represents the board
	 */
	public BufferedImage getImage() {
		Dimension bounds = TBoardConstants.getSize(getAttributes(getModel()));
		if (bounds != null) {
			BufferedImage img = new BufferedImage(bounds.width,
					bounds.height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			print(graphics);
			
			return img;
		}
		return null;
	}

	/**
	 * Generates an XML <code>Element</code> that contains the board information.
	 * 
	 * @param doc The <code>Document</code> that represents the
	 * entire XML document
	 * @return The XML <code>Element</code> generated
	 */
	public Element XMLEncode(Document doc) {
		// Create board node
		Element boardElement = doc.createElement("board");
		boardElement.setAttribute("name",getBoardName());

		// Append model node
		boardElement.appendChild(((TBoardModel)getModel()).XMLEncode(doc));
		
		// Append board node
		return boardElement;
		
	}

	/**
	 * Returns a <code>TBoard</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the board data
	 * @return The generated <code>TBoard</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static TBoard XMLDecode(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("board")) {
			TBoardModel model = null;
			// Get child nodes
			NodeList childNodes = element.getChildNodes();
			// For each node in child nodes
			for (int i = 0; i < childNodes.getLength(); i++) {
				String tagName = childNodes.item(i).getNodeName();
				// If is attributes, decode attribute node
				if (tagName.equals("model"))
					model = TBoardModel.XMLDecode((Element)childNodes.item(i));
			}
			// Create the new board with the model
			TBoard board = new TBoard(model);
			// Get the project name
			if (element.hasAttribute("name"))
				board.setBoardName(element.getAttribute("name"));
			else throw new InvalidFormatException();
			// Return the board
			return board;
		}
		throw new InvalidFormatException();
	}
}
