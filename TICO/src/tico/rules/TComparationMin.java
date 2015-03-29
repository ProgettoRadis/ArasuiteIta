/*
 * File: TComparationMin.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 1, 2007
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
 * The <code>TComparationMax</code> is a structure to comunicate Java Beans with Jess
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 1, 2007
 */

public class TComparationMin {
	
	// The name of rule
	private String name = null;
	// The element name
	private String of = null;
	// An attribute
	private Object attribute = null;
	// A parameter
	private Object parameter = null;
	// The message to display
	private String message = null;
	
	/**
	 * Creates a new empty <code>TComparationMin</code> with no initial attributes.
	 */
	public TComparationMin() {
	}
	
	
	/**
	 * Creates a new empty <code>TComparationMin</code> with the specified initial
	 * <code>name</code>, <code>of</code>, <code>attribute</code>, <code>parameter</code>,
	 * and <code>message</code>
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param of The specified initial <code>of</code>
	 * @param atr The specified initial <code>attribute</code>
	 * @param par The specified initial <code>parameter</code>
	 * @param message The specified initial <code>message</code>
	 */
	public TComparationMin(String name, String of, Object atr, Object par, String message) {
		this.name = name;
		this.of = of;
		this.attribute = atr;
		this.parameter = par;
		this.message = message;
	}
	
	/**
	 * Returns the TComparationMin <code>name</code>.
	 * 
	 * @return The TComparationMin <code>name</code>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the TComparationMin <code>name</code>.
	 * 
	 * @param name The TComparationMin <code>name</code> to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the TComparationMin <code>of</code>.
	 * 
	 * @return The TComparationMin <code>of</code>
	 */
	public String getOf() {
		return of;
	}
	
	/**
	 * Sets the TComparationMin <code>of</code>.
	 * 
	 * @param of The TComparationMin <code>of</code> to set
	 */
	public void setOf(String of) {
		this.of = of;
	}
	
	
	/**
	 * Returns the TComparationMin <code>attribute</code>.
	 * 
	 * @return The TComparationMin <code>attribute</code>
	 */
	public Object getAttribute() {
		return attribute;
	}
	
	/**
	 * Sets the TComparationMin <code>attribute</code>.
	 * 
	 * @param atr The TComparationMin <code>attribute</code> to set
	 */
	public void setAttribute(Object atr) {
		this.attribute = atr;
	}
	
	/**
	 * Returns the TComparationMin <code>parameter</code>.
	 * 
	 * @return The TComparationMin <code>parameter</code>
	 */
	public Object getParameter() {
		return parameter;
	}
	
	/**
	 * Sets the TComparationMin <code>parameter</code>.
	 * 
	 * @param par The TComparationMin <code>parameter</code> to set
	 */
	public void setParameter(Object par) {
		this.parameter = par;
	}
	
	/**
	 * Returns the TComparationMin <code>message</code>.
	 * 
	 * @return The TComparationMin <code>message</code>
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the TComparationMin <code>message</code>.
	 * 
	 * @param message The TComparationMin <code>message</code> to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}