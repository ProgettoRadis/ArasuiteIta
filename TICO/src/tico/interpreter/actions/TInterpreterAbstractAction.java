/*
 * File: TInterpreterAbstractAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez
 * 
 * Date:	May-2006 
 *  
 * Based on: @(#)AbstractActionDefault.java 1.0 12-MAY-2004
 *           Copyright (c) 2001-2005, Gaudenz Alder
 *           From JGraphPad 5.7.3.1.1
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

package tico.interpreter.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import tico.interpreter.TInterpreter;

/**
 * An abstract TInterpreter action. The base class for all TInterpreter actions.
 * 
 * @author Antonio Rodriguez
 */
public abstract class TInterpreterAbstractAction extends AbstractAction {
	/**
	 * A reference back to the TInterpreter. If an action was performed the Actions
	 * applies the changes to the current TBoardContainer at the TInterpreter.
	 */
	protected TInterpreter interpreter;

	/**
	 * Constructor for TInterpreterAbstractAction. The Abstract action uses the class
	 * name without package prefix as action name.
	 * 
	 * @see Action#NAME
	 */
	public TInterpreterAbstractAction() {
		this((TInterpreter)null);
	}

	/**
	 * Constructor for TInterpreterAbstractAction. The Abstract action uses the class
	 * name without package prefix as action name.
	 * 
	 * @param interpreter The reference to the TInterpreter for this action
	 * @see Action#NAME
	 * 
	 */
	public TInterpreterAbstractAction(TInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	/**
	 * Constructor for TInterpreterAbstractAction.
	 * 
	 * @param name Key for the name of this action
	 */
	public TInterpreterAbstractAction(String name) {
		super(name);
	}

	/**
	 * Constructor for TInterpreterAbstractAction.
	 * 
	 * @param interpreter The reference to the TInterpreter for this action
	 * @param name Key for the name of this action
	 */
	public TInterpreterAbstractAction(TInterpreter interpreter, String name) {
		super(name);
		this.interpreter = interpreter;
	}

	/**
	 * Constructor for TInterpreterAbstractAction.
	 * 
	 * @param interpreter The reference to the TInterpreter for this action
	 * @param name Key for the name of the action
	 * @param icon The icon for this action
	 */
	public TInterpreterAbstractAction(TInterpreter interpreter, String name, Icon icon) {
		super(name, icon);
		this.interpreter = interpreter;
	}
	
	/** 
	 * Sets the TInterpreter.
	 * 
	 * @param interpreter The TInterpreter to set
	 */
	public void setInterpreter(TInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	/**
	 * Returns the TInterpreter.
	 * 
	 * @return TInterpreter
	 */
	public TInterpreter getInterpreter() {
		return interpreter;
	}
}
