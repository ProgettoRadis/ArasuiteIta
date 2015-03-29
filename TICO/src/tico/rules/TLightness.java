/*
 * File: TLightness.java
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
 * The <code>TLightness</code> is a structure to comunicate Java Beans with Jess
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 1, 2007
 */

public class TLightness {
	
	// The name of rule
	private String name = null;
	// The element name
	private String of = null;
	// An attribute
	private Object attribute1 = null;
	// Other attribute
	private Object attribute2 = null;
	// A parameter
	private Object parameter = null;
	// The message to display
	private String message = null;
	
	/**
	 * Creates a new empty <code>TLightness</code> with no initial <code>name</code>.
	 */
	public TLightness() {
	}
	
	
	/**
	 * Creates a new empty <code>TLightness</code> with the specified initial
	 * <code>name</code>, <code>of</code>, <code>atr1</code>, <code>atr2</code>, 
	 * <code>par</code> and <code>message</code>
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param of The specified initial <code>of</code>
	 * @param atr1 The specified initial <code>attribute1</code>
	 * @param atr2 The specified initial <code>attribute2</code>
	 * @param par The specified initial <code>parameter</code>
	 * @param message The specified initial <code>message</code>
	 */
	public TLightness(String name, String of, Object atr1, Object atr2, 
			Object par, String message) {
		this.name = name;
		this.of = of;
		this.attribute1 = atr1;
		this.attribute2 = atr2;
		this.parameter = par;
		this.message = message;
	}
	
	/**
	 * Returns the TLightness <code>name</code>.
	 * 
	 * @return The TLightness <code>name</code>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the TLightness <code>name</code>.
	 * 
	 * @param name The TLightness <code>name</code> to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the TLightness <code>of</code>.
	 * 
	 * @return The TLightness <code>of</code>
	 */
	public String getOf() {
		return of;
	}
	
	/**
	 * Sets the TLightness <code>of</code>.
	 * 
	 * @param of The TLightness <code>of</code> to set
	 */
	public void setOf(String of) {
		this.of = of;
	}
	
	/**
	 * Returns the TLightness <code>attribute1</code>.
	 * 
	 * @return The TLightness <code>attribute1</code>
	 */
	public Object getAttribute1() {
		return attribute1;
	}
	
	/**
	 * Sets the TLightness <code>attribute1</code>.
	 * 
	 * @param atr1 The TLightness <code>attribute1</code> to set
	 */
	public void setAttribute1(Object atr1) {
		this.attribute1 = atr1;
	}
	
	
	/**
	 * Returns the TLightness <code>attribute2</code>.
	 * 
	 * @return The TLightness <code>attribute2</code>
	 */
	public Object getAttribute2() {
		return attribute2;
	}
	
	/**
	 * Sets the TLightness <code>attribute2</code>.
	 * 
	 * @param atr2 The TLightness <code>attribute2</code> to set
	 */
	public void setAttribute2(Object atr2) {
		this.attribute2 = atr2;
	}
	
	/**
	 * Returns the TLightness <code>parameter</code>.
	 * 
	 * @return The TLightness <code>parameter</code>
	 */
	public Object getParameter() {
		return parameter;
	}
	
	/**
	 * Sets the TLightness <code>parameter</code>.
	 * 
	 * @param par The TLightness <code>parameter</code> to set
	 */
	public void setParameter(Object par) {
		this.parameter = par;
	}
	
	/**
	 * Returns the TLightness <code>message</code>.
	 * 
	 * @return The TLightness <code>message</code>
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the TLightness <code>message</code>.
	 * 
	 * @param message The TLightness <code>message</code> to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}