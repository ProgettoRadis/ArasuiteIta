/*
 * File: TBoardLayoutCache.java
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.ParentMap;

import tico.board.components.TGrid;
import tico.board.componentview.TViewFactory;

/**
 * An object that defines the view of a <code>TBoardModel</code>. This object
 * maps between model components and views and provides a set of methods to
 * change these views.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardLayoutCache extends GraphLayoutCache {
	/**
	 * Creates a new <code>TBoardLayoutCache</code>.
	 */
	public TBoardLayoutCache() {
		super(null, new TViewFactory());
	}

	/**
	 * Applies the specified <code>map</code> to all the roots of the
	 * <code>components</code>.
	 * 
	 * @param components The <code>components</code> whose roots will be modified
	 * @param map The <code>map</code> to apply
	 */
	public void editRoots(Object[] components, AttributeMap map) {
		Vector newComponents = new Vector();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof TGrid)
				newComponents.addAll(((TGrid)components[i]).getChildren());
			else
				newComponents.add(components[i]);
		}

		edit(newComponents.toArray(), map);
	}

	/**
	 * Applies the attributeMaps to the components specified in <code>attributes</code>
	 * and removes the components in <code>remove</code>.
	 * 
	 * @param attributes Pairs <code>component</code> - <code>attributeMap</code>
	 * that determines wich objects will receive wich attribute map modifications
	 * @param remove The <code>components</code> to remove
	 */
	public void editAndRemove(Map attributes, Object[] remove) {
		((TBoardModel)graphModel).editAndRemove(attributes, remove);
	}

	/**
	 * Removes the components in <code>remove</code> and insert <code>children</code>
	 * in the specified <code>group</code>.
	 * 
	 * @param remove The <code>components</code> to remove
	 * @param group The specified target <code>group</code>
	 * @param children The <code>components</code> to add to <code>group</code>
	 */
	public void removeAndInsertGroup(Object[] remove, Object group,
			Object[] children) {
		// List to store all children that are not in the model
		List newCells = new ArrayList(children.length + 1);
		// If does not exists, add it in the first position
		if (!graphModel.contains(group)) {
			newCells.add(group);
		}
		// Create a parent map for the group and the children, and
		// store the children's attributes in the nested map.
		ParentMap pm = new ParentMap();
		Map nested = new Hashtable();
		for (int i = 0; i < children.length; i++) {
			pm.addEntry(children[i], group);
			if (!graphModel.contains(children[i])) {
				newCells.add(children[i]);
				AttributeMap attrs = graphModel.getAttributes(children[i]);
				if (attrs != null)
					nested.put(children[i], attrs);
			}
		}
		// Edit
		((TBoardModel)graphModel).edit(newCells.toArray(),remove, nested, null, pm, null);
	}
	
	/**
	 * Applies the attributeMaps to the components specified in
	 * <code>attributes</code>, removes the components in <code>remove</code>
	 * and inserts the components in <code>insert</code>.
	 * 
	 * @param insert The <code>components</code> to insert
	 * @param remove The <code>components</code> to remove
	 * @param attributes Pairs <code>component</code> - <code>attributeMap</code>
	 * that determines wich objects will receive wich attribute map modifications
	 */	
	public void removeInsertAndEdit(Object[] insert, Object[] remove, Map attributes) {	
		for (int i = 0; i < insert.length; i++) {
			if (!graphModel.contains(insert[i])) {
				AttributeMap attrs = graphModel.getAttributes(insert[i]);
				if (attrs != null)
					attributes.put(insert[i], attrs);
			}
		}
		((TBoardModel)graphModel).removeInsertAndEdit(insert, remove, attributes);
	}
}
