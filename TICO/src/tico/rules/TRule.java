/*
 * File: TRule.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 12, 2007
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
 * The <code>TRule</code> is an object that stores all the information of a rule
 * in the XML file.
 *
 * @author Beatriz Mateo
 * @version 1.0 Oct 12, 2007
 */

public class TRule {
	
	private String name = null;
	private String nameXML = null;
	private TAttribute attribute = null;
	private TAttribute attribute2 = null;
	private TAttribute parameter = null;
	private String function = null;
	private String functionXML = null;
	private String message = null;
	private String messageXML = null;
	private String type = null;
	
	/**
	 * Creates a new empty <code>TRule</code> with no initial attributes.
	 */
	public TRule() {
	}
	
	/**
	 * Creates a new empty <code>TRule</code> with the specified initial
	 * <code>name</code> and <code>nameXML</code>.
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param nameXML The specified initial <code>nameXML</code>
	 */
	public TRule(String name, String nameXML) {
		this.name = name;
		this.nameXML = nameXML;
	}
	
	
	/**
	 * Creates a new empty <code>TRule</code> with the specified initial
	 * <code>name</code>, <code>nameXML</code>, <code>attribute</code>, 
	 * <code>attribute2</code>, <code>parameter</code>, <code>function</code>, 
	 * <code>functionXML</code> and <code>messageXML</code>.
	 * 
	 * @param name The specified initial <code>name</code>
	 * @param nameXML The specified initial <code>nameXML</code>
	 * @param attribute The specified initial <code>attribute</code>
	 * @param attribute2 The specified initial <code>attribute2</code>
	 * @param parameter The specified initial <code>parameter</code>
	 * @param function The specified initial <code>function</code>
	 * @param functionXML The specified initial <code>functionXML</code>
	 * @param message The specified initial <code>message</code>
	 * @param messageXML The specified initial <code>messageXML</code>
	 */
	public TRule(String name, String nameXML, TAttribute attribute, TAttribute attribute2,
			TAttribute parameter, String function, String functionXML, String message,
			String messageXML) {
		this.name = name;
		this.nameXML = name;
		this.attribute = attribute;
		this.attribute2 = attribute2;
		this.parameter = parameter;
		this.function = function;
		this.functionXML = function;
		this.message = message;
		this.messageXML = messageXML;
	}
	
	/**
	 * Returns the rule <code>name</code>.
	 * 
	 * @return The rule <code>name</code>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the rule <code>name</code>.
	 * 
	 * @param name The rule <code>name</code> to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the rule <code>nameXML</code>.
	 * 
	 * @return The rule <code>nameXML</code>
	 */
	public String getNameXML() {
		return nameXML;
	}
	
	/**
	 * Sets the rule <code>nameXML</code>.
	 * 
	 * @param nameXML The rule <code>nameXML</code> to set
	 */
	public void setNameXML(String nameXML) {
		this.nameXML = nameXML;
	}
	
	/**
	 * Returns the rule <code>attribute</code>.
	 * 
	 * @return The rule <code>attribute</code>
	 */
	public TAttribute getAttribute() {
		return attribute;
	}
	
	/**
	 * Sets the rule <code>attribute</code>.
	 * 
	 * @param attribute The rule <code>attribute</code> to set
	 */
	public void setAttribute(TAttribute attribute) {
		this.attribute = attribute;
	}
	
	
	/**
	 * Returns the rule <code>attribute2</code>.
	 * 
	 * @return The rule <code>attribute2</code>
	 */
	public TAttribute getAttribute2() {
		return attribute2;
	}
	
	/**
	 * Sets the rule <code>attribute2</code>.
	 * 
	 * @param attribute2 The rule <code>attribute2</code> to set
	 */
	public void setAttribute2(TAttribute attribute2) {
		this.attribute2 = attribute2;
	}
	

	/**
	 * Returns the rule <code>paramter</code>.
	 * 
	 * @return The rule <code>parameter</code>
	 */
	public TAttribute getParameter() {
		return parameter;
	}
	
	/**
	 * Sets the rule <code>parameter</code>.
	 * 
	 * @param parameter The rule <code>parameter</code> to set
	 */
	public void setParameter(TAttribute parameter) {
		this.parameter = parameter;
	}
	
	
	/**
	 * Returns the rule <code>function</code>.
	 * 
	 * @return The rule <code>function</code>
	 */
	public String getFunction() {
		return function;
	}
	
	/**
	 * Sets the rule <code>function</code>.
	 * 
	 * @param function The rule <code>function</code> to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	
	/**
	 * Returns the rule <code>functionXML</code>.
	 * 
	 * @return The rule <code>functionXML</code>
	 */
	public String getFunctionXML() {
		return functionXML;
	}
	
	/**
	 * Sets the rule <code>functionXML</code>.
	 * 
	 * @param functionXML The rule <code>functionXML</code> to set
	 */
	public void setFunctionXML(String functionXML) {
		this.functionXML = functionXML;
	}
	
	/**
	 * Returns the rule <code>message</code>.
	 * 
	 * @return The rule <code>message</code>
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the rule <code>message</code>.
	 * 
	 * @param message The rule <code>message</code> to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns the rule <code>messageXML</code>.
	 * 
	 * @return The rule <code>messageXML</code>
	 */
	public String getMessageXML() {
		return messageXML;
	}
	
	/**
	 * Sets the rule <code>messageXML</code>.
	 * 
	 * @param messageXML The rule <code>messageXML</code> to set
	 */
	public void setMessageXML(String messageXML) {
		this.messageXML = messageXML;
	}
	
	
	/**
	 * Returns the rule <code>type</code>.
	 * 
	 * @return The rule <code>type</code>
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the rule <code>type</code>.
	 * 
	 * @param type The rule <code>type</code> to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}