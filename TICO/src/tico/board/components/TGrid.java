/*
 * File: TGrid.java
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.jgraph.graph.AttributeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tico.board.TBoardConstants;
import tico.board.encoding.InvalidFormatException;
import tico.board.encoding.TAttributeEncoder;

/**
 * Defines a <code>TGrid</code>, its attributes and its children grid cells.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TGrid extends TComponent {
	/**
	 * The <code>TGrid</code> type used as attribute in XML encoding and decoding.
	 */
	public static String TYPE = "grid";
	
	private static int gridCount = 0;

	/**
	 * Sequential grid order value.
	 */
	public final static int SEQUENTIAL = 0;
	
	/**
	 * Rows grid order value.
	 */
	public final static int ROWS = 1;
	
	/**
	 * Columns grid order value.
	 */
	public final static int COLUMNS = 2;
	
	/**
	 * Custom grid order value.
	 */
	public final static int CUSTOM = 3;
	
	private final static int DEFAULT_ORDER = SEQUENTIAL;
		
	/**
	 * Default horizontal distance between cells
	 */
	public final static double DEFAULT_HORIZONTAL_GAP = 10.0;
	
	/**
	 * Default vertical distance between cells
	 */
	public final static double DEFAULT_VERTICAL_GAP = 10.0;

	/**
	 * Creates a new <code>TGrid</code> with the specified <code>childs</code>.
	 * 
	 * @param childs The specified <code>childs</code>
	 */
	public TGrid(TGridCell[] childs) {
		super(childs);
		TBoardConstants.setGroupOpaque(this.getAttributes(),false);
//		TBoardConstants.setOrder(this.getAttributes(),DEFAULT_ORDER);
//		TBoardConstants.setBrowseable(getAttributes(), true);
		TBoardConstants.setBrowseable(getAttributes(), false);//mio
		
	}

	/* (non-Javadoc)
	 * @see tico.board.components.TComponent#newId()
	 */
	public String newId() {
    	return "grid_" + new Integer(gridCount++);
	}

	/**
	 * Returns the <code>gridCell</code> in the specified position.
	 * 
	 * @param row The specified <code>row</code>
	 * @param column The specified <code>column</code>
	 * @return The <code>TGridCell</code> in the specified <code>row</code> and
	 * <code>column</code>
	 */
	public TGridCell getCell(int row, int column) {
		int currentColumn, currentRow;
		
		for (int i = 0; i < children.size(); i++) {
			currentColumn = TBoardConstants.getColumn(
					((TGridCell)children.get(i)).getAttributes());
			currentRow = TBoardConstants.getRow(
					((TGridCell)children.get(i)).getAttributes());
			if ((currentColumn == column) && (currentRow == row))
				return (TGridCell)children.get(i);
		}
		return null;
	}
	
	/**
	 * Gets the positions all its grid cells and gets the maximum column
	 * value. This value is useful when the order of the grid is ROWS
	 * otherwise the value is not porperly the number of columns.
	 * 
	 * @return Number of columns
	 */
	public int getColumnsCount() {
		int currentColumn, lastColumn = 0;
		
		for (int i = 0; i < children.size(); i++) {
			currentColumn = TBoardConstants.getColumn(
					((TGridCell)children.get(i)).getAttributes());
			if (currentColumn > lastColumn) lastColumn = currentColumn;
		}
		return lastColumn;
	}
	
	/**
	 * Gets the positions all its grid cells and gets the maximum row
	 * value. This value is useful when the order of the grid is COLUMNS
	 * otherwise the value is not porperly the number of rows.
	 * 
	 * @return Number of rows
	 */
	public int getRowsCount() {
		int currentRow, lastRow = 0;
		
		for (int i = 0; i < children.size(); i++) {
			currentRow = TBoardConstants.getRow(
					((TGridCell)children.get(i)).getAttributes());
			if (currentRow > lastRow) lastRow = currentRow;
		}
		return lastRow;
	}

	/**
	 * Returns the last <code>column</code> in the specified <code>row</code>.
	 * 
	 * @param row The specified <code>row</code>
	 * @return The last <code>column</code> in the specified <code>row</code>
	 */
	public int getLastColumnRow(int row) {
		int currentColumn, lastColumn = 0;
		
		for (int i = 0; i < children.size(); i++) {
			currentColumn = TBoardConstants.getColumn(
					((TGridCell)children.get(i)).getAttributes());
			if ((row == TBoardConstants.getRow(((TGridCell)children.get(i))
					.getAttributes())) && (currentColumn > lastColumn))
				lastColumn = currentColumn;
		}
		return lastColumn;
	}
	
	/**
	 * Returns the last <code>row</code> in the specified <code>column</code>.
	 * 
	 * @param column The specified <code>column</code>
	 * @return The last <code>row</code> in the specified <code>column</code>
	 */
	public int getLastRowColumn(int column) {
		int currentRow, lastRow = 0;
		
		for (int i = 0; i < children.size(); i++) {
			currentRow = TBoardConstants.getRow(
					((TGridCell)children.get(i)).getAttributes());
			if ((column == TBoardConstants.getColumn(((TGridCell)children.get(i))
					.getAttributes())) && (currentRow > lastRow))
				lastRow = currentRow;
		}
		return lastRow;
	}
	
	/**
	 * Returns the number of gridCells in the specified <code>column</code>.
	 * 
	 * @param column The specified <code>column</code>
	 * @return The number of cells in the specified <code>column</code>
	 */
	public int getColumnDimension(int column) {
		int dimension = 0;
		
		for (int i = 0; i < children.size(); i++)
			if (column == TBoardConstants.getColumn(((TGridCell)children.get(i)).getAttributes()))
				dimension++;
		
		return dimension;
	}
	
	/**
	 * Returns the number of gridCells in the specified <code>row</code>.
	 * 
	 * @param row The specified <code>row</code>
	 * @return The number of cells in the specified <code>row</code>
	 */
	public int getRowDimension(int row) {
		int dimension = 0;
		
		for (int i = 0; i < children.size(); i++)
			if (row == TBoardConstants.getRow(((TGridCell)children.get(i)).getAttributes()))
				dimension++;
		
		return dimension;
	} 
	
	/**
	 * Returns the following <code>gridCell</code> of the specified
	 * <code>position</code> in the specified <code>row</code>.
	 * 
	 * @param row The specified <code>row</code>
	 * @param position The specified <code>position</code>
	 * @return The following <code>gridCell</code> of the specified
	 * <code>position</code> in the specified <code>row</code>
	 */
	public TGridCell getFollowingRowCell(int row, int position) {
		int currentColumn, followingColumn;
		TGridCell followingCell;

		followingColumn = getLastColumnRow(row);

		// Check if there is any following cell after position
		if (followingColumn >= position)
			followingCell = getCell(row,followingColumn);
		else return null;
		
		// Search the closest one
		for (int i = 0; i < children.size(); i++) {
			TGridCell currentCell = (TGridCell)children.get(i);
			currentColumn = TBoardConstants.getColumn(currentCell.getAttributes());
			if ((row == TBoardConstants.getRow(currentCell.getAttributes())) &&
				(currentColumn < followingColumn) && (currentColumn >= position)) {
				followingColumn = currentColumn;
				followingCell = currentCell;
			}
		}
		return followingCell;
	}
		
	/**
	 * Returns the following <code>gridCell</code> of the specified
	 * <code>position</code> in the specified <code>column</code>.
	 * 
	 * @param column The specified <code>column</code>
	 * @param position The specified <code>position</code>
	 * @return The following <code>gridCell</code> of the specified
	 * <code>position</code> in the specified <code>column</code>
	 */
	public TGridCell getFollowingColumnCell(int column, int position) {
		int currentRow, followingRow;
		TGridCell followingCell;
		
		followingRow = getLastRowColumn(column);
				
		// Check if there is any following cell after position
		if (followingRow >= position)
			followingCell = getCell(followingRow,column);
		else return null;
		
		// Search the closest one
		for (int i = 0; i < children.size(); i++) {
			TGridCell currentCell = (TGridCell)children.get(i);
			currentRow = TBoardConstants.getRow(currentCell.getAttributes());
			if ((column == TBoardConstants.getColumn(currentCell.getAttributes())) &&
				(currentRow < followingRow) && (currentRow >= position)) {
				followingRow = currentRow;
				followingCell = currentCell;
			}
		}
		return followingCell;
	}
	
	/**
	 * Returns a <code>map</code> with the pairs <code>gridCell</code> -
	 * <code>attributeMap</code> to be aplied that must be used when you want
	 * to change the order of the <code>grid</code> to <i>ROWS</i>.
	 * 
	 * @return The <code>map</code> to apply when the <code>grid</code> order
	 * changed to <i>ROWS</i>
	 */
	public Map reorderRows() {
		Map nested = new Hashtable();

		// For each row get the number of columns of the row and move them to
		// the first positions of the row	
		int rows = getRowsCount();
		for (int currentRow = 1; currentRow <= rows; currentRow++) {
			int currentColumn = 1, lastColumn = 1;
			TGridCell currentCell;
			while ((currentCell = getFollowingRowCell(currentRow,lastColumn)) != null) {
				lastColumn = TBoardConstants.getColumn(currentCell.getAttributes());
				if (lastColumn != currentColumn) {
					Map attributeMap = new AttributeMap();
					TBoardConstants.setColumn(attributeMap,currentColumn);
					nested.put(currentCell,attributeMap);
				}
				lastColumn++;
				currentColumn++;
			}
		}
		
		return nested;
	}
	
	
	/**
	 * Returns a <code>map</code> with the pairs <code>gridCell</code> -
	 * <code>attributeMap</code> to be aplied that must be used when you want
	 * to change the order of the <code>grid</code> to <i>COLUMNS</i>.
	 * 
	 * @return The <code>map</code> to apply when the <code>grid</code> order
	 * changed to <i>COLUMNS</i>
	 */
	public Map reorderColumns() {
		Map nested = new Hashtable();

		// For each column get the number of rows of the column and move them to
		// the first positions of the column	
		int columns = getColumnsCount();		
		for (int currentColumn = 1; currentColumn <= columns; currentColumn++) {
			// Initialize the currento row ante the last row of the column
			int currentRow = 1, lastRow = 1;
			TGridCell currentCell;
			// I replace each element on its suposed finar position
			while ((currentCell = getFollowingColumnCell(currentColumn,lastRow)) != null) {
				lastRow = TBoardConstants.getRow(currentCell.getAttributes());
				if (lastRow != currentRow) {
					Map attributeMap = new AttributeMap();
					TBoardConstants.setRow(attributeMap,currentRow);
					nested.put(currentCell,attributeMap);
				}
				lastRow++;
				currentRow++;
			}
		}

		return nested;
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
		map.remove(TBoardConstants.BROWSEABLE);
		map.remove(TBoardConstants.GROUPOPAQUE);
		// Append component attributes
		componentElement.appendChild(TAttributeEncoder.XMLEncode(map, doc));

		// Append component nodes
		for (int i = 0; i < getChildCount(); i++)

			componentElement.appendChild(((TComponent)getChildAt(i))
					.XMLEncode(doc));
		// Return the component node
		return componentElement;
		
	}
	
	/**
	 * Returns a <code>TGrid</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the grid data
	 * @return The generated <code>TGrid</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static TGrid XMLDecode(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("component")
				&& element.getAttribute("type").equals(TYPE)) {
			
			// Create grid cell id -> gridCell map
			Hashtable gridCellMap = new Hashtable();
			// Get grids children
			NodeList componentNodeList = element.getElementsByTagName("component");
			TGridCell[] childrenList = new TGridCell[componentNodeList.getLength()];
			for (int i = 0; i < componentNodeList.getLength(); i++) {
				Node boardNode = componentNodeList.item(i);
				if (boardNode.getNodeType() == Node.ELEMENT_NODE) {
					TGridCell gridCell= TGridCell.XMLDecode((Element)boardNode);
					childrenList[i] = gridCell;
				}
			}
			// Create the grid with the children
			TGrid grid = new TGrid(childrenList);
			// Get the attributes
			Node attributesNode = element.getElementsByTagName("attributes")
					.item(0);
			AttributeMap attributes = TAttributeEncoder
					.XMLDecode((Element)attributesNode);
			// Apply attributes to the grid
			grid.getAttributes().applyMap(attributes);
			
			// Check the attribute
			if (attributes == null)
				throw new InvalidFormatException();

			for (int i = 0; i < grid.getChildCount(); i++) {
				TGridCell gridCell = (TGridCell)grid.getChildAt(i);
				gridCellMap.put(gridCell.getId(), gridCell);
			}
			// Get his grid cell lists and create the new one
			ArrayList idOrderedList = TBoardConstants
					.getOrderedCellList(attributes);
			ArrayList componentOrderedList = new ArrayList();
			// Fill the new lists
			for (int i = 0; i < idOrderedList.size(); i++)
				componentOrderedList
						.add(gridCellMap.get(idOrderedList.get(i)));
			// Set the grid cell lists
			TBoardConstants
					.setOrderedCellList(attributes, componentOrderedList);
			
			// Apply attributes to the grid
			grid.getAttributes().applyMap(attributes);
			// Return it
			return grid;
		}
		return null;
	}
}
