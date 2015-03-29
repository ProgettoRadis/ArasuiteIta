/*
 * File: TAdjustVerticalGapAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
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

package tico.editor.actions;

import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.board.components.TGrid;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich distributes vertically all the editor's current selected
 * components.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TAdjustVerticalGapAction extends TEditorAbstractAction {
	/**
	 * Constructor of the TAdjustVerticalGapAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TAdjustVerticalGapAction(TEditor editor) {
		super(editor, TLanguage.getString("TAdjustVerticalGapAction.NAME"),
				TResourceManager.getImageIcon("align-vertical-gap-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Map nested = new Hashtable();
		Map attributeMap;
		
		int selectedCellsCount;
		
		ArrayList unorderedCells, orderedCells;
		
		double verticalGapSize, lastCellPlace;
		
		TComponent currentCell;
		Rectangle2D currentBounds, newBounds;

		// Remove TGrids from the selected components
		Object[] selectedCells = getEditor().getCurrentBoard().getSelectionCells();
		for (int i = 0; i < selectedCells.length; i++)
			if (selectedCells[i] instanceof TGrid)
				getEditor().getCurrentBoard().removeSelectionCell(selectedCells[i]);
		
		selectedCellsCount = getEditor().getCurrentBoard().getSelectionCount();
		
		// I don't have to modify any component
		if (selectedCellsCount < 3) return;

		// Order using its X position
		unorderedCells = new ArrayList();
		orderedCells = new ArrayList();
		for (int i = 0; i < selectedCellsCount; i++)
			unorderedCells.add(getEditor().getCurrentBoard().getSelectionCells()[i]);
		
		for (int i = 0; i < selectedCellsCount; i++) {
			TComponent minCell = (TComponent)unorderedCells.get(0);
			currentBounds = TBoardConstants.getBounds(minCell.getAttributes());
			double minPosition = currentBounds.getY();
						
			for (int j = 1; j < unorderedCells.size(); j++) {
				currentCell = (TComponent)unorderedCells.get(j);
				currentBounds = TBoardConstants.getBounds(currentCell.getAttributes());
				if (currentBounds.getY() < minPosition) {
					minPosition = currentBounds.getY();
					minCell = currentCell;
				}	
			}
			
			orderedCells.add(minCell);
			unorderedCells.remove(minCell);			
		}
		
		// Get the horizontal gap size
		verticalGapSize =
			TBoardConstants.getBounds(((TComponent)orderedCells.get(orderedCells.size()-1)).getAttributes()).getY() -
			TBoardConstants.getBounds(((TComponent)orderedCells.get(0)).getAttributes()).getY();
		for (int i = 0; i < selectedCellsCount - 1; i++)
			verticalGapSize -= TBoardConstants.getBounds(((TComponent)orderedCells.get(i)).getAttributes()).getHeight();
		verticalGapSize = verticalGapSize / (float)(orderedCells.size() - 1);
		
		// Replace all the components
		lastCellPlace = TBoardConstants.getBounds(((TComponent)orderedCells.get(0)).getAttributes()).getY() +
		TBoardConstants.getBounds(((TComponent)orderedCells.get(0)).getAttributes()).getHeight();
		
		for (int i = 1; i < selectedCellsCount - 1; i++) {
			attributeMap = new AttributeMap();
			
			currentCell = (TComponent)orderedCells.get(i);
			currentBounds = (Rectangle2D)TBoardConstants.getBounds(currentCell.getAttributes());
			newBounds = new Rectangle2D.Double(currentBounds.getX(),
					lastCellPlace + verticalGapSize,currentBounds.getWidth(),currentBounds.getHeight());
			
			TBoardConstants.setBounds(attributeMap,newBounds);
			nested.put(currentCell,attributeMap);
			
			lastCellPlace += verticalGapSize + currentBounds.getHeight();
		}
					
		getEditor().getCurrentBoard().getGraphLayoutCache().edit(nested,null,null,null);
	}
}
