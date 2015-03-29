/*
 * File: TEditorAbstractAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jan 30, 2006
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

package tico.editor.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import tico.editor.TEditor;

/**
 * An abstract TEditor action. The base class for all TEditor actions.
 *
 *
 * @author bleras
 * @version 0.1 Jul 11, 2006
 */
public abstract class TEditorAbstractAction extends AbstractAction {
	/**
	 * A reference back to the TEditor. If an action was performed the Actions
	 * applies the changes to the current TBoardContainer at the TEditor.
	 */
	protected TEditor editor;

	/**
	 * Constructor for TEditorAbstractAction. The Abstract action uses the class
	 * name without package prefix as action name.
	 * 
	 * @see Action#NAME
	 */
	public TEditorAbstractAction() {
		this((TEditor)null);
	}

	/**
	 * Constructor for TEditorAbstractAction. The Abstract action uses the class
	 * name without package prefix as action name.
	 * 
	 * @param editor The reference to the TEditor for this action
	 * @see Action#NAME
	 * 
	 */
	public TEditorAbstractAction(TEditor editor) {
		this.editor = editor;
	}

	/**
	 * Constructor for TEditorAbstractAction.
	 * 
	 * @param name Key for the name of this action
	 */
	public TEditorAbstractAction(String name) {
		super(name);
	}

	/**
	 * Constructor for TEditorAbstractAction.
	 * 
	 * @param editor The reference to the TEditor for this action
	 * @param name Key for the name of this action
	 */
	public TEditorAbstractAction(TEditor editor, String name) {
		super(name);
		this.editor = editor;
	}

	/**
	 * Constructor for TEditorAbstractAction.
	 * 
	 * @param editor The reference to the TEditor for this action
	 * @param name Key for the name of the action
	 * @param icon The icon for this action
	 */
	public TEditorAbstractAction(TEditor editor, String name, Icon icon) {
		super(name, icon);
		this.editor = editor;
	}
	
	/** 
	 * Sets the TEditor.
	 * 
	 * @param editor The TEditor to set
	 */
	public void setEditor(TEditor editor) {
		this.editor = editor;
	}

	/**
	 * Returns the TEditor.
	 * 
	 * @return TEditor
	 */
	public TEditor getEditor() {
		return editor;
	}
}
