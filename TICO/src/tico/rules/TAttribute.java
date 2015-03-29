/*
 * File: TAttribute.java
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

/**
 * The <code>TAttribute</code> object that contains a name, a nameXML and a value.
 *
 * @author Beatriz Mateo
 * @version 1.0 Sep 27, 2007
 */

public class TAttribute {
	
	// The name represented in the language file
	private String name = null;
	// The name in the XML file
	private String nameXML = null;
	// The value
	private Object value = null;
	// The limitation type
	private String limitType = "";
	
	/**
	 * Creates a new empty <code>TAttribute</code> with no initial <code>name</code>.
	 */
	public TAttribute() {
	}
	
	/**
	 * Creates a new empty <code>TAttribute</code> with the specified initial
	 * <code>name</code> and <code>nameXML</code>.
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param nameXML The specified initial <code>nameXML</code>
	 */
	public TAttribute(String name, String nameXML) {
		this.name = name;
		this.nameXML = nameXML;
	}
	
	/**
	 * Creates a new empty <code>TAttribute</code> with the specified initial
	 * <code>value</code>
	 * 
	 * @param value The specified initial <code>value</code>
	 */
	public TAttribute(int value) {
		this.value = value;
	}
	
	/**
	 * Creates a new empty <code>TAttribute</code> with the specified initial
	 * <code>name</code>, <code>nameXML</code> and <code>value</code>
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param nameXML The specified initial <code>nameXML</code>
	 * @param value The specified initial <code>value</code>
	 */
	public TAttribute(String name, String nameXML, Object value) {
		this.name = name;
		this.nameXML = nameXML;
		this.value = value;
	}
	
	/**
	 * Returns the attribute <code>name</code>.
	 * 
	 * @return The attribute <code>name</code>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the attribute <code>name</code>.
	 * 
	 * @param name The attribute <code>name</code> to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the attribute <code>nameXML</code>.
	 * 
	 * @return The attribute <code>nameXML</code>
	 */
	public String getNameXML() {
		return nameXML;
	}
	
	/**
	 * Sets the attribute <code>nameXML</code>.
	 * 
	 * @param nameXML The attribute <code>nameXML</code> to set
	 */
	public void setNameXML(String nameXML) {
		this.nameXML = nameXML;
	}
	
	/**
	 * Returns the attribute <code>value</code>.
	 * 
	 * @return The attribute <code>value</code>
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Sets the attribute <code>value</code>.
	 * 
	 * @param value The attribute <code>value</code> to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns the attribute <code>limitation type</code>.
	 * 
	 * @return The attribute <code>limitation type</code>
	 */
	public String getLimitType() {
		return limitType;
	}

	/**
	 * Sets the attribute <code>limitation type</code>.
	 * 
	 * @param limitType The attribute <code>limitation type</code> to set
	 */
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}
}