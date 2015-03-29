/*
 * File: TBoardModel
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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.undo.UndoableEdit;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.ParentMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tico.board.components.TComponent;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.board.components.TLabel;
import tico.board.encoding.InvalidFormatException;
import tico.editor.TEditor;
import tico.board.encoding.TAttributeEncoder;
import tico.components.resources.TFileUtils;
import tico.editor.TFileHandler;

/**
 * Custom model than does not allow <code>Connections</code> between nodes. This
 * model do not use the <code>Graph</code> structure.
 * 
 * @author Pablo Muñoz
 * @version 1.1 May 09, 2009
 */
public class TBoardModel extends DefaultGraphModel {

	private final static int HORIZONTAL_FIT_TEXT_MARGIN = 6;

	private final static int VERTICAL_FIT_TEXT_MARGIN = 4;
	
	/**
	 * Creates a new empty <code>TBoardModel</code> and with
	 * <code>defaultAttributes</code>.
	 */
	public TBoardModel() {
		this((List)null, defaultAttributes());
	}

	/**
	 * Creates a new <code>TBoardModel</code> with the specified initial
	 * <code>roots</code> and <code>defaultAttributes</code>.
	 * 
	 * @param roots The specified initial <code>roots</code>
	 */
	public TBoardModel(List roots) {
		this(roots, defaultAttributes());
	}

	/**
	 * Creates a new empty <code>TBoardModel</code> with the specified initial
	 * <code>attributes</code>.
	 * 
	 * @param attributes The specified initial <code>attributes</code>
	 */
	public TBoardModel(AttributeMap attributes) {
		this(null, attributes);
	}

	/**
	 * Creates a new <code>TBoardModel</code> with the specified initial
	 * <code>roots</code> and <code>attributes</code>.
	 * 
	 * @param roots The specified initial <code>roots</code>
	 * @param attributes The specified initial <code>attributes</code>
	 */
	public TBoardModel(List roots, AttributeMap attributes) {
		super(roots, attributes);
	}

	/**
	 * Creates the default model <code>attributes</code>.
	 * 
	 * @return The default model <code>attributes</code>
	 */
	private static AttributeMap defaultAttributes() {
		AttributeMap defaultAttributes = new AttributeMap();

		//TBoardConstants.setBackground(defaultAttributes, Color.WHITE);
		TBoardConstants.setBackground(defaultAttributes, TEditor.get_board_background_color());
		if(TEditor.get_board_gradient_color()!=null)TBoardConstants.setGradientColor(defaultAttributes, TEditor.get_board_gradient_color());
		TBoardConstants.setSize(defaultAttributes, new Dimension(TEditor.get_board_width(), TEditor.get_board_height()));

		TBoardConstants
				.setUnorderedCellList(defaultAttributes, new ArrayList());
		TBoardConstants.setOrderedCellList(defaultAttributes, new ArrayList());

		return defaultAttributes;
	}

	//
	// Graph Structure
	//
	/**
	 * Edges are not used in this model.
	 * 
	 * @return null
	 */
	public Object getSource(Object edge) {
		return null;
	}

	/**
	 * Edges are not used in this model.
	 * 
	 * @return null
	 */
	public Object getTarget(Object edge) {
		return null;
	}

	/**
	 * Edges are not used in this model, therefore port are not used too.
	 * 
	 * @return false.
	 */
	public boolean acceptsSource(Object edge, Object port) {
		return false;
	}

	/**
	 * Edges are not used in this model, therefore port are not used too.
	 * 
	 * @return false.
	 */
	public boolean acceptsTarget(Object edge, Object port) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.graph.GraphModel#getAttributes(java.lang.Object)
	 */
	public AttributeMap getAttributes(Object node) {
		// This allows to modify the graph model attributes using the
		// common model edit methods, and suporting undo/redo functions
		if ((node == null) || (node instanceof GraphModel))
			return super.getAttributes(null);

		return super.getAttributes(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.graph.GraphModel#insert(java.lang.Object[],
	 *      java.util.Map, org.jgraph.graph.ConnectionSet,
	 *      org.jgraph.graph.ParentMap, javax.swing.undo.UndoableEdit[])
	 */
	public void insert(Object[] inserted, Map attributes, ConnectionSet cs,
			ParentMap pm, UndoableEdit[] edits) {
		if (attributes == null)
			attributes = new Hashtable();

		// For each new component
		if (inserted != null)
			for (int i = 0; i < inserted.length; i++) { 
				//bucle para evitar los repetidos de los grid cell
				boolean repeated=false;
				for(int j=0;j<i;j++){
					if(inserted[i]==inserted[j]){
						repeated=true;
					}
				}
				if(!repeated){
					TComponent newComponent = (TComponent)inserted[i];

					Map attributeMap = (Map)attributes.get(newComponent);
					if (attributeMap == null)
						attributeMap = new AttributeMap();
					else {
						attributeMap = (Map)((AttributeMap)attributeMap).clone();
						attributes.remove(newComponent);
					}

					attributes.putAll(insertUpdateAttributes(newComponent,
							attributeMap, attributes));
				}
			}

		// For each parent map modification
		
		
		if (pm != null) {
			Iterator pmIterator = pm.entries();

			while (pmIterator.hasNext()) {
				ParentMap.Entry entry = (ParentMap.Entry)pmIterator.next();

				if ((entry.getParent() instanceof TGrid)
						&& (entry.getChild() instanceof TGridCell)) {
					Map attributeMap = (Map)attributes.get(entry.getParent());
					if (attributeMap == null)
						attributeMap = new AttributeMap();
					else {
						attributeMap = (Map)((AttributeMap)attributeMap)
								.clone();
						attributes.remove(entry.getParent());
					}

					attributes
							.putAll(parentUpdateAttributes((TGridCell)entry
									.getChild(), (TGrid)entry.getParent(),
									attributeMap));
				}
			}
		}
		
		super.insert(inserted, attributes, cs, pm, edits);
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.graph.GraphModel#edit(java.util.Map,
	 *      org.jgraph.graph.ConnectionSet, org.jgraph.graph.ParentMap,
	 *      javax.swing.undo.UndoableEdit[])
	 */
	public void edit(Map attributes, ConnectionSet cs, ParentMap pm,
			UndoableEdit[] edits) {

		if (attributes == null)
			attributes = new Hashtable();

		// For each modified component
		Iterator componentIterator = attributes.entrySet().iterator();
		Map newAttributes = new Hashtable();

		while (componentIterator.hasNext()) {
			Map.Entry entry = (Map.Entry)componentIterator.next();

			if (entry.getKey() instanceof TComponent) {
				TComponent component = (TComponent)entry.getKey();

				Map attributeMap = (Map)attributes.get(component);
				if (attributeMap == null)
					attributeMap = new AttributeMap();

				newAttributes.putAll(editUpdateAttributes(component,
						attributeMap));
			}
		}

		attributes.putAll(newAttributes);

		super.edit(attributes, cs, pm, edits);
	}

	/**
	 * Removes <code>cells</code> from the model. Notifies the model- and undo
	 * listeners of the change.
	 */
	public void remove(Object[] removed) {
		// Create an empty edit component map
		Map attributes = new Hashtable();

		// For each removed component
		if (removed != null)
			for (int i = 0; i < removed.length; i++) {
				TComponent component = (TComponent)removed[i];

				Map attributeMap = (Map)attributes.get(component);
				if (attributeMap == null)
					attributeMap = new AttributeMap();
				else
					attributes.remove(component);

				attributes.putAll(removeUpdateAttributes(component, attributes));
			}
		// Create delete edit
		GraphModelEdit removeEdit = createRemoveEdit(removed);
		// If is not null execute it
		if (removeEdit != null)
			removeEdit.execute();

		edit(attributes, null, null, new UndoableEdit[] { removeEdit });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.graph.GraphModel#edit(java.util.Map,
	 *      org.jgraph.graph.ConnectionSet, org.jgraph.graph.ParentMap,
	 *      javax.swing.undo.UndoableEdit[])
	 */
	public void edit(Object[] inserted, Object[] removed, Map attributes,
			ConnectionSet cs, ParentMap pm, UndoableEdit[] edits) {

		if (attributes == null)
			attributes = new Hashtable();

		// For each new component
		if (inserted != null)
			for (int i = 0; i < inserted.length; i++) {
				TComponent newComponent = (TComponent)inserted[i];

				Map attributeMap = (Map)attributes.get(newComponent);
				if (attributeMap == null)
					attributeMap = new AttributeMap();
				else {
					attributeMap = (Map)((AttributeMap)attributeMap).clone();
					attributes.remove(newComponent);
				}

				attributes.putAll(insertUpdateAttributes(newComponent,
						attributeMap, attributes));
			}
		
		// For each modified component
		Iterator componentIterator = attributes.entrySet().iterator();
		Map newAttributes = new Hashtable();

		while (componentIterator.hasNext()) {
			Map.Entry entry = (Map.Entry)componentIterator.next();

			if (entry.getKey() instanceof TComponent) {
				TComponent component = (TComponent)entry.getKey();

				Map attributeMap = (Map)attributes.get(component);
				if (attributeMap == null)
					attributeMap = new AttributeMap();

				newAttributes.putAll(editUpdateAttributes(component,
						attributeMap));
			}
		}

		attributes.putAll(newAttributes);

		// For each parent map modification
		if (pm != null) {
			Iterator pmIterator = pm.entries();

			while (pmIterator.hasNext()) {
				ParentMap.Entry entry = (ParentMap.Entry)pmIterator.next();

				if ((entry.getParent() instanceof TGrid)
						&& (entry.getChild() instanceof TGridCell)) {
					Map attributeMap = (Map)attributes.get(entry.getParent());
					if (attributeMap == null)
						attributeMap = new AttributeMap();
					else {
						attributeMap = (Map)((AttributeMap)attributeMap)
								.clone();
						attributes.remove(entry.getParent());
					}

					attributes
							.putAll(parentUpdateAttributes((TGridCell)entry
									.getChild(), (TGrid)entry.getParent(),
									attributeMap));
				}
			}
		}
		
		// For each removed component
		if (removed != null)
			for (int i = 0; i < removed.length; i++) {
				TComponent component = (TComponent)removed[i];

				Map attributeMap = (Map)attributes.get(component);
				if (attributeMap == null)
					attributeMap = new AttributeMap();
				else
					attributes.remove(component);

				attributes.putAll(removeUpdateAttributes(component, attributes));
			}

		super.edit(inserted, removed, attributes, cs, pm, edits);
	}

	/**
	 * Applies the attributeMaps to the components specified in <code>attributes</code>
	 * and removes the components in <code>removed</code>.
	 * 
	 * @param attributes Pairs <code>component</code> - <code>attributeMap</code>
	 * that determines wich objects will receive wich attribute map modifications
	 * @param removed The <code>components</code> to remove
	 */
	public void editAndRemove(Map attributes, Object[] removed) {
		// For each removed component
		if (removed != null)
			for (int i = 0; i < removed.length; i++) {
				TComponent component = (TComponent)removed[i];

				Map attributeMap = (Map)attributes.get(component);
				if (attributeMap == null)
					attributeMap = new AttributeMap();
				else
					attributes.remove(component);

				attributes.putAll(removeUpdateAttributes(component, attributes));
			}
		// Create delete edit
		GraphModelEdit removeEdit = createRemoveEdit(removed);
		// If is not null execute it
		if (removeEdit != null)
			removeEdit.execute();

		// Execute the other undoable edits
		edit(attributes, null, null, new UndoableEdit[] { removeEdit });
	}

	/**
	 * Applies the attributeMaps to the components specified in
	 * <code>attributes</code>, removes the components in <code>removed</code>
	 * and inserts the components in <code>insert</code>.
	 * 
	 * @param insert The <code>components</code> to insert
	 * @param removed The <code>components</code> to remove
	 * @param attributes Pairs <code>component</code> - <code>attributeMap</code>
	 * that determines which objects will receive which attribute map modifications
	 */	
	public void removeInsertAndEdit(Object[] insert, Object[] removed,
			Map attributes) {
		// For each removed component
		if (removed != null)
			for (int i = 0; i < removed.length; i++) {
				TComponent component = (TComponent)removed[i];

				Map attributeMap = (Map)attributes.get(component);
				if (attributeMap == null)
					attributeMap = new AttributeMap();
				else
					attributes.remove(component);

				attributes.putAll(removeUpdateAttributes(component, attributes));
			}
		// Create delete edit
		GraphModelEdit removeEdit = createRemoveEdit(removed);
		// If is not null execute it
		if (removeEdit != null)
			removeEdit.execute();

		// Execute the other undoable edits
		edit(insert, null, attributes, null, null,
				new UndoableEdit[] { removeEdit });

	}

	//
	// Collateral modification functions
	//
	
	// Function executed when any component has been inserted
	private Map insertUpdateAttributes(TComponent component, Map attributeMap, Map attributes) {
		Map nested = new Hashtable();
		
		if (attributeMap == null)
			attributeMap = new AttributeMap();

		// Manage the ordered model browseable component lists
		if (TBoardConstants.isBrowseable(component.getAttributes())) {
			ArrayList orderedCells;
			Map currentModelMap = (Map)attributes.get(this);
			if ((currentModelMap != null) &&
				(currentModelMap.containsKey(TBoardConstants.ORDERED_CELL_LIST))) {
				orderedCells = TBoardConstants.getOrderedCellList(currentModelMap);
			} else {	
				orderedCells = TBoardConstants.getOrderedCellList(getAttributes());
			}
			
			orderedCells.add(component);		

			AttributeMap modelAttributeMap = new AttributeMap();
			TBoardConstants.setOrderedCellList(modelAttributeMap, orderedCells);

			nested.put(this, modelAttributeMap);
		}

		// Change pasted grid cell list order
/*		if (component instanceof TGrid) {
			System.out.println("es un tgrid");
			// For each cell in grid order list
			ArrayList orderedCells = TBoardConstants
					.getOrderedCellList(attributeMap);
			ArrayList newOrderedCells = new ArrayList();

			for (int i = 0; i < orderedCells.size(); i++) {
				// Get the cell and check if corresponds with the grid
				TGridCell gridCell = (TGridCell)orderedCells.get(i);
				// If it has different parent
				if (!gridCell.getParent().equals(component)) {
					// Get the orginial cell position
					int cellRow = TBoardConstants.getRow(gridCell
							.getAttributes());
					int cellColumn = TBoardConstants.getColumn(gridCell
							.getAttributes());
					// Get the corresponding one in the new grid
					TGridCell newCell = ((TGrid)component).getCell(cellRow,
							cellColumn);
					newOrderedCells.add(newCell);
				} else
					newOrderedCells.add(gridCell);
			}

			TBoardConstants.setOrderedCellList(attributeMap, newOrderedCells);
		}
*/
		// Assign an unused id to the component. This is not applicable to
		// grid cells because its Id is made with its grid id and its
		// position
//		if (!(component instanceof TGridCell)) {
			// Get the current component id or create a new one
			String newId = component.getId(); 
			//System.out.println("nuevo nombre");
			if (newId == null)
				newId = component.newId();
			// While newId exists, create a new one
			//System.out.println("el id es "+newId);
			while (existsId(newId)){
				newId = component.newId();
				//System.out.println("existe");
			}
			TBoardConstants.setId(attributeMap, newId);
//		}
		
		// Implements resizeToText component property
		if (TBoardConstants.isResizeToText(attributeMap)) {
			//modificado para android mode
			/*if(!TEditor.get_android_mode()){*/
				TBoardConstants.setBounds(attributeMap,
					computeNewBounds(TBoardConstants.getBounds(attributeMap),
							TBoardConstants.getText(attributeMap),
							TBoardConstants.getFont(attributeMap), Math.max(1,
									Math.round(TBoardConstants
											.getLineWidth(attributeMap)))));
			/*}else{//modo android
				Font font=TBoardConstants.getFont(attributeMap);
				font=new Font("Droid Sans",font.getStyle(),font.getSize());
				TBoardConstants.setBounds(attributeMap,
						computeNewBounds(TBoardConstants.getBounds(attributeMap),
								TBoardConstants.getText(attributeMap),
								font, Math.max(1,
										Math.round(TBoardConstants
												.getLineWidth(attributeMap)))));
			}*/
		}
		
		// TUNE Apply these following methods directly to needed attributes without using TBoardConstants
		// Import an icon or file when copying a component
		ImageIcon icon = (ImageIcon)TBoardConstants.getIcon(attributeMap);
		if (icon != null) {
			try {
				File imageFile = new File(TFileHandler.importFile(
						icon.getDescription()).getAbsolutePath());
				// Test if need to be loaded with JAI libraries (different
				// format than JPG, PNG and GIF)
				if (TFileUtils.isJAIRequired(imageFile)) {
					// Load it with JAI class
					RenderedOp src = JAI.create("fileload", imageFile
							.getAbsolutePath());
					BufferedImage bufferedImage = src.getAsBufferedImage();
					icon = new ImageIcon(bufferedImage, imageFile
							.getAbsolutePath());
				} else {
					// Create it as usual
					icon = new ImageIcon(imageFile.getAbsolutePath(), imageFile
							.getAbsolutePath());
				}
				TBoardConstants.setIcon(attributeMap, icon);
			} catch (Exception e) {
			}
		}

		ImageIcon alternativeIcon = (ImageIcon)TBoardConstants
				.getAlternativeIcon(attributeMap);
		if (alternativeIcon != null) {
			try {
				File imageFile = new File(TFileHandler.importFile(alternativeIcon.getDescription()).getAbsolutePath());
				// Test if need to be loaded with JAI libraries (different
				// format than JPG, PNG and GIF)
				if (TFileUtils.isJAIRequired(imageFile)) {
					// Load it with JAI class
					RenderedOp src = JAI.create("fileload", imageFile
							.getAbsolutePath());
					BufferedImage bufferedImage = src.getAsBufferedImage();
					alternativeIcon = new ImageIcon(bufferedImage, imageFile
							.getAbsolutePath());
				} else {
					// Create it as usual
					alternativeIcon = new ImageIcon(
							imageFile.getAbsolutePath(), imageFile
									.getAbsolutePath());
				}
				TBoardConstants.setAlternativeIcon(attributeMap, alternativeIcon);
			} catch (Exception e) {
			}
		}

		String soundFilePath = TBoardConstants.getSoundFile(attributeMap);
		if (soundFilePath != null)
			try {
				TBoardConstants.setSoundFile(attributeMap, TFileHandler
						.importFile(soundFilePath).getAbsolutePath());
			} catch (Exception e) {
			}
		
		String videoFilePath = TBoardConstants.getVideoFile(attributeMap);
		if (videoFilePath != null)
			try {
				TBoardConstants.setVideoFile(attributeMap, TFileHandler
						.importFile(videoFilePath).getAbsolutePath());
			} catch (Exception e) {
		}

			// Delete following board property if it refers to current board
		if (TBoardConstants.getFollowingBoardName(attributeMap) != null) {
			if (TBoardConstants.getFollowingBoardName(attributeMap).equals(TBoardConstants.currentBoard.getBoardName())){
			//if ((TBoardModel) TBoardConstants.getFollowingBoard(attributeMap).getModel() == this) {
				attributeMap.remove(TBoardConstants.FOLLOWING_BOARD);
				TBoardConstants.setRemoveAttributes(attributeMap, new Object[] {TBoardConstants.FOLLOWING_BOARD});
			}
		}

		nested.put(component, attributeMap);

		return nested;
	}
	
	// Function executed when any component has change its parent
	private Map parentUpdateAttributes(TGridCell child, TGrid parent,
			Map parentAttributeMap) { 
		Map nested = new Hashtable();
		
		if (parentAttributeMap == null)
			parentAttributeMap = new AttributeMap();
		
		// Insert the new cells to the TGridCell
		ArrayList gridCellList;
		// If is the first cell of the grid, initialized the grid
		// vector
		gridCellList = TBoardConstants.getOrderedCellList(parentAttributeMap);
		if (gridCellList.isEmpty())
			gridCellList = TBoardConstants.getOrderedCellList(parent
					.getAttributes());

		// We should not add it again, this only happens when pasting a grid
		if (!gridCellList.contains(child))
			gridCellList.add(child);

		TBoardConstants.setOrderedCellList(parentAttributeMap, gridCellList);

		nested.put(parent, parentAttributeMap);

		return nested;
	}
	
	// Function executed when any component has been removed
	private Map removeUpdateAttributes(TComponent component, Map attributes) {
		Map nested = new Hashtable();
		
		// Manage the ordered and unordered model cell lists
		if (TBoardConstants.isBrowseable(component.getAttributes())) {
			ArrayList unorderedCells, orderedCells;
			Map currentModelMap = (Map)attributes.get(this);

			if ((currentModelMap != null) &&
			    (currentModelMap.containsKey(TBoardConstants.UNORDERED_CELL_LIST))) {
				unorderedCells = TBoardConstants.getUnorderedCellList(currentModelMap);
			} else {
				unorderedCells = TBoardConstants.getUnorderedCellList(getAttributes());
			}
						
			if (!unorderedCells.isEmpty())
				unorderedCells.remove(component);

			if ((currentModelMap != null) &&
				(currentModelMap.containsKey(TBoardConstants.ORDERED_CELL_LIST))) {
				orderedCells = TBoardConstants.getOrderedCellList(currentModelMap);
			} else {
				orderedCells = TBoardConstants.getOrderedCellList(getAttributes());
			}
				
			if (!orderedCells.isEmpty())
				orderedCells.remove(component);

			AttributeMap modelAttributeMap = new AttributeMap();
			TBoardConstants.setOrderedCellList(modelAttributeMap, orderedCells);
			
			AttributeMap attributeMap = new AttributeMap();
			TBoardConstants.setUnorderedCellList(attributeMap, unorderedCells);
			TBoardConstants.setOrderedCellList(attributeMap, orderedCells);

			nested.put(this, attributeMap);
		}

		// Manage TGrid internal order lists
		
		if (component instanceof TGridCell) {
			ArrayList gridCellList;

			TGrid grid = (TGrid)component.getParent();
			gridCellList = TBoardConstants.getOrderedCellList(grid
					.getAttributes());
			gridCellList.remove(component);

			Map attributeMap = new AttributeMap();
			TBoardConstants.setOrderedCellList(attributeMap, gridCellList);

			nested.put(grid, attributeMap);
		}
		

		// If the component is a text receiver remove all the appearances of that
		// component in text sender component attributes
		if (TBoardConstants.isTextReceiver(component.getAttributes())) {
			// For each text sender
			Object[] components = getAll(this);
			for (int i = 0; i < components.length; i++) {
				if (TBoardConstants.isTextSender(((TComponent)components[i])
						.getAttributes())) {
					// Get, if exits, its send text area attribute
					TComponent textReceiver = TBoardConstants
							.getSendTextTarget(((TComponent)components[i])
									.getAttributes());
					if (textReceiver != null) {
						// If its the same component
						if (textReceiver.equals(component)) {
							AttributeMap newAttributes = new AttributeMap();
							TBoardConstants.setRemoveAttributes(newAttributes,
									new Object[] { TBoardConstants.SEND_TEXT,
											TBoardConstants.SEND_TEXT_TARGET,
											TBoardConstants.SEND_TEXT_TIMER });
							nested.put(components[i], newAttributes);
						}
					}
				}
			}
		}

		return nested;
	}
	
	// Function executed when any component has been edited
	private Map editUpdateAttributes(TComponent component, Map attributeMap) {
		// Create an empty component changes map
		Map nested = new Hashtable();
		
		// If the current edit attribute map is empty, create a new one
		if (attributeMap == null)
			attributeMap = new AttributeMap();

		// Implements resizeToText component property
		AttributeMap finalAttributeMap = (AttributeMap)((AttributeMap)component
				.getAttributes()).clone();
		finalAttributeMap.applyMap(attributeMap);

		/*antiguo, cambiado para funcionamiento correcto android mode*/
		if (TBoardConstants.isResizeToText(finalAttributeMap)) {
			TBoardConstants.setBounds(attributeMap, computeNewBounds(
					TBoardConstants.getBounds(finalAttributeMap),
					TBoardConstants.getText(finalAttributeMap), TBoardConstants
							.getFont(finalAttributeMap), Math.max(1, Math
							.round(TBoardConstants
									.getLineWidth(finalAttributeMap)))));
		}
		
		/*if (TBoardConstants.isResizeToText(finalAttributeMap)) {
			if(!TEditor.get_android_mode()){
				TBoardConstants.setBounds(attributeMap, computeNewBounds(
					TBoardConstants.getBounds(finalAttributeMap),
					TBoardConstants.getText(finalAttributeMap), TBoardConstants
							.getFont(finalAttributeMap), Math.max(1, Math
							.round(TBoardConstants
									.getLineWidth(finalAttributeMap)))));
			}
			else{//modo android
				Font font=TBoardConstants.getFont(finalAttributeMap);
				font=new Font("Droid Sans",font.getStyle(),font.getSize());
				TBoardConstants.setBounds(attributeMap, computeNewBounds(
						TBoardConstants.getBounds(finalAttributeMap),
						TBoardConstants.getText(finalAttributeMap), font, Math.max(1, Math
								.round(TBoardConstants
										.getLineWidth(finalAttributeMap)))));
			}
		}*/
		/*fin parte cambiada*/
		nested.put(component, attributeMap);

		return nested;
	}
	
	
	/**
	 * Deletes the specified <code>board</code> from all the model component's
	 * attributes. Is used when the <code>board</code> has been deleted from the
	 * boards project
	 * 
	 * @param board The <code>board</code> to be deleted from model component's
	 * attributes
	 */
	public void deleteBoardFromAttributes(TBoard board) {
		if (board == null)
			throw new NullPointerException();
		TBoard followingBoard = null;
		Object[] components = getAll(this);
		for (int i = 0; i < components.length; i++) {
			// Get, if exits, its following board attribute
			String followingBoardName = TBoardConstants
					.getFollowingBoardName(((TComponent)components[i])
							.getAttributes());			
			if (followingBoardName != null){
				followingBoard = TBoardConstants.editor.getProject().getBoard(followingBoardName);
				// If its the same text area remove the send text attributes
				if (followingBoard!= null && followingBoard.equals(board)) {
					AttributeMap attributeMap = new AttributeMap();
					TBoardConstants.setRemoveAttributes(attributeMap,
							new Object[] { TBoardConstants.FOLLOWING_BOARD });
					getAttributes(components[i]).applyMap(attributeMap);
				}
			}
		}
	}

	// Id management functions
	/**
	 * Determines if any model component has the specified <code>id</code>.
	 * 
	 * @param id The specified <code>id</code>
	 * @return <i>true</i> if any model component has the specified <code>id</code>
	 */
	public boolean existsId(String id) {
		List components = getRoots();
		
		for (int i = 0; i < components.size(); i++){
			if (((TComponent)components.get(i)).getId().equals(id))
				return true;
			if((TComponent)components.get(i) instanceof TGrid){
				//System.out.println("es un grid");
				if(existsIdInGrid(id, (TGrid)components.get(i))) return true;
			}
		}
		return false;
	}
	
	
	
	private boolean existsIdInGrid(String id,TGrid grid) {
		List components = grid.getChildren();
		
		for (int i = 0; i < components.size(); i++){
			if (((TComponent)components.get(i)).getId().equals(id))
				return true;
			
		}
		return false;
	}

	/**
	 * Determines if any model component different from <code>original</code>
	 * has the specified <code>id</code>.
	 * 
	 * @param original The <code>original</code> component
	 * @param id The specified <code>id</code>
	 * @return <i>true</i> if any model component different from
	 * <code>original</code> has the specified <code>id</code>
	 */
	public boolean isRepeatedId(TComponent original, String id) {
		List components = getRoots();

		for (int i = 0; i < components.size(); i++){
			if (!original.equals(components.get(i)))
				if (((TComponent)components.get(i)).getId().equals(id))
					return true;
			if((TComponent)components.get(i) instanceof TGrid){
				//System.out.println("es un grid");
				if(isRepeatedIdInGrid(original,id, (TGrid)components.get(i))) return true;
			}
		}

		return false;
	}
	
	
	private boolean isRepeatedIdInGrid(TComponent original,String id, TGrid grid) {
		List components = grid.getChildren();
		
		for (int i = 0; i < components.size(); i++){
			if (!original.equals(components.get(i)))
				if (((TComponent)components.get(i)).getId().equals(id))
					return true;
			
		}
		return false;
	}


	// Used to get the bounds of a resizeToText component
	public static Rectangle2D computeNewBounds(Rectangle2D bounds, String text,
			Font font, int borderWidth) {
		//modificacion para adaptar al texto en fuente android si esta en modo android
		Font fontAux;
		if(TEditor.get_android_mode()) fontAux=new Font("Droid Sans",font.getStyle(),font.getSize());
		else fontAux=font;
		
		// Create a JLabel to calculate the text size on a swing component
		JLabel sizeLabel = new JLabel();
		FontMetrics fontMetric = sizeLabel.getFontMetrics(fontAux);//antes font

		// Get text size
		if (text == null)
			text = "";

		int textWidth = fontMetric.stringWidth(text);
		int textHeight = fontMetric.getHeight();

		// Calculate new size
		int newLabelWidth, newLabelHeight;

		newLabelWidth = textWidth + 2
				* (HORIZONTAL_FIT_TEXT_MARGIN + borderWidth);
		newLabelHeight = textHeight + 2
				* (VERTICAL_FIT_TEXT_MARGIN + borderWidth);

		// Return it
		bounds.setFrame(bounds.getX(), bounds.getY(), newLabelWidth,
				newLabelHeight);

		return bounds;
	}
	
	/**
	 * Generates a XML <code>Node</code> that contains the model board
	 * information.
	 * 
	 * @param doc The <code>Document</code> that represents the
	 * entire XML document
	 * @return The XML <code>Node</code> generated
	 */
	public Node XMLEncode(Document doc) {
		// Create model node
		Element modelElement = doc.createElement("model");

		// Append model attributes
		modelElement.appendChild(TAttributeEncoder.XMLEncode(
				(AttributeMap)getAttributes(), doc));

		// Append component nodes
		for (int i = 0; i < getRootCount(); i++)
			/*nuevo mio*/
			if(((TComponent)getRootAt(i)) instanceof TGrid){
				for (int j = 0; j < ((TComponent)getRootAt(i)).getChildCount(); j++){
					modelElement.appendChild(((TComponent)((TComponent)getRootAt(i)).getChildAt(j))
							.XMLEncode(doc));
				}
			}
			else
			/*fin*/
			modelElement.appendChild(((TComponent)getRootAt(i)).XMLEncode(doc));

		// Return the model node
		return modelElement;
	}
	
	/**
	 * Returns a <code>TBoardModel</code> object from the data contained in
	 * the XML <code>Element</code>.
	 * 
	 * @param element The XML <code>Element</code> that contains the board data
	 * @return The generated <code>TBoardModel</code>
	 * @throws InvalidFormatException If <code>Element</code> has an invalid format
	 */
	public static TBoardModel XMLDecode(Element element) throws InvalidFormatException {
		if (element.getTagName().equals("model")) {
			// Define the needed data
			AttributeMap attributes = null;
			Vector componentList = new Vector();
			Hashtable componentMap = new Hashtable();
			Vector textSenderList = new Vector();
			// Get child nodes
			NodeList childNodes = element.getChildNodes();
			// For each node in child nodes
			for (int i = 0; i < childNodes.getLength(); i++) {
				String tagName = childNodes.item(i).getNodeName();
				// If is attributes, decode attribute node
				if (tagName.equals("attributes"))
					attributes = TAttributeEncoder
							.XMLDecode((Element)childNodes.item(i));
				// If component, decode component
				if (tagName.equals("component")) {
					TComponent component = TComponent
							.XMLDecodeType((Element)childNodes.item(i));
					componentMap.put(component.getId(), component);
					componentList.add(component);

					if (component.getAttributes().containsKey(
							TBoardConstants.SEND_TEXT_TARGET))
						textSenderList.add(component);
					for (int j = 0; j < component.getChildCount(); j++) {
						TComponent childComponent = (TComponent)component
								.getChildAt(j);
						if (childComponent.getAttributes().containsKey(
								TBoardConstants.SEND_TEXT_TARGET))
							textSenderList.add(childComponent);

					}
					//if anyadido
					/*if(component instanceof TLabel){
						AttributeMap labelAttributes = component.getAttributes();
						if(!TEditor.get_android_mode()){
							TBoardConstants.setBounds(labelAttributes,
								computeNewBounds(TBoardConstants.getBounds(labelAttributes),
										TBoardConstants.getText(labelAttributes),
										TBoardConstants.getFont(labelAttributes), Math.max(1,
												Math.round(TBoardConstants
														.getLineWidth(labelAttributes)))));
						}else{//modo android
							Font font=TBoardConstants.getFont(labelAttributes);
							font=new Font("Droid Sans",font.getStyle(),font.getSize());
							TBoardConstants.setBounds(labelAttributes,
									computeNewBounds(TBoardConstants.getBounds(labelAttributes),
											TBoardConstants.getText(labelAttributes),
											font, Math.max(1,
													Math.round(TBoardConstants
															.getLineWidth(labelAttributes)))));
						}
					}*/
				}
			}

			// Check the attribute
			if (attributes == null)
				throw new InvalidFormatException();
			// Get he cell lists
			ArrayList idUnorderedList = TBoardConstants
					.getUnorderedCellList(attributes);
			ArrayList idOrderedList = TBoardConstants
					.getOrderedCellList(attributes);
			// Create the new lists
			ArrayList componentUnorderedList = new ArrayList();
			ArrayList componentOrderedList = new ArrayList();
			// Fill the new lists
			for (int i = 0; i < idUnorderedList.size(); i++)
				componentUnorderedList.add(componentMap.get(idUnorderedList
						.get(i)));
			for (int i = 0; i < idOrderedList.size(); i++)
				componentOrderedList
						.add(componentMap.get(idOrderedList.get(i)));

			// Set the component lists
			TBoardConstants
					.setOrderedCellList(attributes, componentOrderedList);
			TBoardConstants.setUnorderedCellList(attributes,
					componentUnorderedList);

			// Replace components sent text target
			for (int i = 0; i < textSenderList.size(); i++) {
				TComponent component = (TComponent)textSenderList.get(i);
				String sendTextTargetId = (String)component.getAttributes()
						.get(TBoardConstants.SEND_TEXT_TARGET);
				TBoardConstants.setSendTextTarget(component.getAttributes(),
						(TComponent)componentMap.get(sendTextTargetId));
			}

			// Create the model
			TBoardModel model = new TBoardModel(componentList, attributes);
			// Return the model
			return model;
		}
		throw new InvalidFormatException();
	}
}
