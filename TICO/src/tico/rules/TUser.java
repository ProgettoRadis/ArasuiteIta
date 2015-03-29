/*
 * File: TUser.java
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
 * The <code>TUser</code> object stores all features of an user, its name, and 
 * a list of attributes that represents its limitations.
 *
 * @author Beatriz Mateo
 * @version 1.0 Sep 27, 2007
 */

public class TUser {
	
	// The user name
	private String name = null;
	// The list of its limitations
	private ArrayList attributeList;
	private Hashtable attributeTable;
	
	/**
	 * Creates a new empty <code>TUser</code> with no initial <code>name</code>.
	 */
	public TUser() {
		attributeList = new ArrayList();
		attributeTable = new Hashtable();
	}
	
	/**
	 * Creates a new empty <code>TUser</code> with the specified initial
	 * <code>name</code>.
	 * 
	 * @param name The specified initial <code>name</code>
	 */
	public TUser(String name) {
		this.name = name;
		attributeList = new ArrayList();
		attributeTable = new Hashtable();
	}
	
	
	/**
	 * Returns the user <code>name</code>.
	 * 
	 * @return The user <code>name</code>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the user <code>name</code>.
	 * 
	 * @param name The user <code>name</code> to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Add an <code>attribute</code> to the user.
	 * 
	 * @param attribute The <code>attribute</code> to add
	 */
	public void addAttribute(TAttribute attribute) {
		attributeList.add(attribute);
		attributeTable.put(attribute.getName(), attribute);
	}
	
	/**
	 * Removes the specified <code>attribute</code> from the user.
	 * 
	 * @param attribute The <code>attribute</code> to delete
	 */
	public void removeAttribute(TAttribute attribute) {
		attributeTable.remove(attribute.getName());
		attributeList.remove(attribute);
	}
	
	/**
	 * Returs the user attribute with the specified <code>name</code>.
	 * 
	 * @param name The specified attribute <code>name</code>
	 * @return The user attribute with the specified <code>name</code>. If the
	 * user does not contain any attribute with that name, returns null
	 */
	public TAttribute getAttribute(String name) {
		return (TAttribute)attributeTable.get(name);
	}
	
	/**
	 * Returs the user attribute in the specified <code>index</code>.
	 * 
	 * @param index The specified attribute <code>index</code>
	 * @return The user attribute in the specified <code>index</code>
	 */
	public TAttribute getAttribute(int index) {
		return (TAttribute)attributeList.get(index);
	}
	
	/**
	 * Returns the number of attributes contained by the user
	 * 
	 * @return The user number of attributes
	 */
	public int getAttributeCount() {
		return attributeList.size();
	}
	
	/**
	 * Returs the user attribute list.
	 * 
	 * @return The user attribute list. If the user does not contain 
	 * any attribute, returns null
	 */
	public List getAttributeList() {
		return attributeList;
	}
}