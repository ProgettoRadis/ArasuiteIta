/*
 * File: TInterpreterActionSet.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez
 * 
 * Date: May 23, 2007
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

package tico.interpreter;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.Action;

import tico.interpreter.actions.TInterpreterEditOptions;
import tico.interpreter.actions.TInterpreterExitAction;
import tico.interpreter.actions.TInterpreterFullscreenAction;
import tico.interpreter.actions.TInterpreterLanguajes;
import tico.interpreter.actions.TInterpreterProjectOpenAction;
//import tico.interpreter.actions.TInterpreterProjectPrint;
import tico.interpreter.actions.TInterpreterReadAction;
import tico.interpreter.actions.TInterpreterRun;
import tico.interpreter.actions.TInterpreterShowAbout;
import tico.interpreter.actions.TInterpreterStopAction;
import tico.interpreter.actions.TInterpreterUndoAction;
import tico.interpreter.actions.TInterpreterUndoAllAction;
import tico.rules.actions.TInterpreterValidationAction;

/**
 * Map of actions that can be done to an interpreter.
 * 
 * @author Antonio Rodríguez
 */

public class TInterpreterActionSet {
	/**
	 * The <code>TInterpreterExitAction</code> id
	 */
	public final static String INTERPRETER_EXIT_ACTION = "interpreterExitAction";

	/**
	 * The <code>TProjectOpenAction</code> id
	 */
	public final static String PROJECT_OPEN_ACTION = "proyectOpenAction";

	public final static String INTERPRETER_ABOUT = "interpreterShowAbout";

	public final static String INTERPRETER_RUN = "interpreterRun";

	public final static String INTERPRETER_STOP = "interpreterStop";

	public final static String INTERPRETER_LANGUAJES = "interpreterLanguajes";

	public final static String INTERPRETER_OPTIONS = "interpreterOptions";

	public final static String INTERPRETER_READ = "interpreterRead";

	public final static String INTERPRETER_PRINT_ACTION = "InterpreterPrint";

	public final static String CONTROLLER_BUTTON_ACTION = "ControllerAction";

	public final static String INTERPRETER_UNDO = "interpreterUndo";
	
	public final static String INTERPRETER_UNDO_ALL = "interpreterUndoAll";
	
	public final static String INTERPRETER_VALIDATION = "interpreterValidation";

        public final static String INTERPRETER_FULLSCREEN = "interpreterFullscreen";

	// Map which contains the actionName-action pairs

	private Map actionSet;

	/**
	 * Creates a new <code>TInterpreterActionSet</code> for the specified
	 * <code>interpreter</code> with all the possible actions.
	 * 
	 * @param interpreter
	 *            The specified <code>interpreter</code>
	 */
	public TInterpreterActionSet(TInterpreter interpreter) {

		actionSet = new Hashtable();

		actionSet.put(INTERPRETER_EXIT_ACTION, new TInterpreterExitAction(interpreter));

		actionSet.put(PROJECT_OPEN_ACTION, new TInterpreterProjectOpenAction(interpreter));

		actionSet.put(INTERPRETER_ABOUT, new TInterpreterShowAbout(interpreter));

		actionSet.put(INTERPRETER_RUN, new TInterpreterRun(interpreter));

		actionSet.put(INTERPRETER_STOP, new TInterpreterStopAction(interpreter));

		actionSet.put(INTERPRETER_LANGUAJES, new TInterpreterLanguajes(interpreter));

		actionSet.put(INTERPRETER_OPTIONS, new TInterpreterEditOptions(interpreter));

		actionSet.put(INTERPRETER_READ, new TInterpreterReadAction(interpreter));

		/*actionSet.put(INTERPRETER_PRINT_ACTION, new TInterpreterProjectPrint(
				interpreter));*/

		actionSet.put(INTERPRETER_UNDO, new TInterpreterUndoAction(interpreter));
		
		actionSet.put(INTERPRETER_UNDO_ALL, new TInterpreterUndoAllAction(interpreter));
		
		actionSet.put(INTERPRETER_VALIDATION, new TInterpreterValidationAction(interpreter));

                actionSet.put(INTERPRETER_FULLSCREEN, new TInterpreterFullscreenAction(interpreter));

	}

	/**
	 * Returns the <code>action</code> with the specified
	 * <code>actionId</code>.
	 * 
	 * @param actionId
	 *            The specified <code>actionId</code>
	 * @return The <code>action</code> with the specified
	 *         <code>actionId</code>
	 */
	public Action getAction(String actionId) {
		return (Action) actionSet.get(actionId);
	}

}
