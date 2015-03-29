/*
 * File: TLimitation.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Sep 27, 2007
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

package tico.rules;

import java.util.*;

/**
 * The <code>TLimitation</code> object contains a name, a nameXML a value and
 * a list of attributes.
 *
 * @author Beatriz Mateo
 * @version 1.0 Sep 27, 2007
 */

public class TLimitation {
	
	// The name
	private String name = null;
	// The name represented in the XML file
	private String nameXML = null;
	// The value
	private int value = 0;
	// The list of attributes
	private ArrayList attributeList;
	private Hashtable attributeTable;
	
	/**
	 * Creates a new empty <code>TLimitation</code> with no initial <code>name</code>
	 * or <code>value</code>.
	 */
	public TLimitation() {
		attributeList = new ArrayList();
		attributeTable = new Hashtable();
	}
	
	/**
	 * Creates a new empty <code>TLimitation</code> with the specified initial
	 * <code>name</code> and <code>nameXML</code>.
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param nameXML The specified initial <code>nameXML</code>
	 */
	public TLimitation(String name, String nameXML) {
		this.name = name;
		this.nameXML = nameXML;
		attributeList = new ArrayList();
		attributeTable = new Hashtable();
	}
	
	
	/**
	 * Creates a new empty <code>TLimitation</code> with the specified initial
	 * <code>value</code>.
	 * 
	 * @param value The specified initial <code>value</code>
	 */
	public TLimitation(int value) {
		this.value = value;
		attributeList = new ArrayList();
		attributeTable = new Hashtable();
	}
	
	/**
	 * Creates a new empty <code>TLimitation</code> with the specified initial
	 * <code>name</code>, <code>nameXML</code> and <code>value</code>.
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param nameXML The specified initial <code>nameXML</code>
	 * @param value The specified initial <code>value</code>
	 */
	public TLimitation(String name, String nameXML, int value) {
		this.name = name;
		this.value = value;
		this.nameXML = nameXML;
		attributeList = new ArrayList();
		attributeTable = new Hashtable();
	}
	
	/**
	 * Returns the limitation <code>name</code>.
	 * 
	 * @return The limitation <code>name</code>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the limitation <code>name</code>.
	 * 
	 * @param name The limitation <code>name</code> to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the limitation <code>nameXML</code>.
	 * 
	 * @return The limitation <code>nameXML</code>
	 */
	public String getNameXML() {
		return nameXML;
	}
	
	/**
	 * Sets the limitation <code>nameXML</code>.
	 * 
	 * @param nameXML The limitation <code>nameXML</code> to set
	 */
	public void setNameXML(String nameXML) {
		this.nameXML = nameXML;
	}
	
	/**
	 * Returns the limitation <code>value</code>.
	 * 
	 * @return The limitation <code>value</code>
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Sets the limitation <code>value</code>.
	 * 
	 * @param value The limitation <code>value</code> to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Add an <code>attribute</code> to the limitation.
	 * 
	 * @param attribute The <code>attribute</code> to add
	 */
	public void addAttribute(TAttribute attribute) {
		attributeList.add(attribute);
		attributeTable.put(attribute.getName(), attribute);
	}
	
	/**
	 * Removes the specified <code>attribute</code> from the limitation.
	 * 
	 * @param attribute The <code>attribute</code> to delete
	 */
	public void removeAttribute(TAttribute attribute) {
		attributeTable.remove(attribute.getName());
		attributeList.remove(attribute);
	}
	
	/**
	 * Returs the limitation attribute with the specified <code>name</code>.
	 * 
	 * @param name The specified attribute <code>name</code>
	 * @return The limitation attribute with the specified <code>name</code>. If the
	 * limitation does not contain any attribute with that name, returns null
	 */
	public TAttribute getAttribute(String name) {
		return (TAttribute)attributeTable.get(name);
	}
	
	/**
	 * Returs the limitation attribute in the specified <code>index</code>.
	 * 
	 * @param index The specified attribute <code>index</code>
	 * @return The limitation attribute in the specified <code>index</code>
	 */
	public TAttribute getAttribute(int index) {
		return (TAttribute)attributeList.get(index);
	}
	
	/**
	 * Returns the number of attributes contained by the limitation
	 * 
	 * @return The number of attributes
	 */
	public int getAttributeCount() {
		return attributeList.size();
	}
	
	/**
	 * Returs the list of attributes
	 * 
	 * @return The list of attributes. If the limitation does not contain 
	 * any attribute, returns null
	 */
	public List getAttributeList() {
		return attributeList;
	}
}