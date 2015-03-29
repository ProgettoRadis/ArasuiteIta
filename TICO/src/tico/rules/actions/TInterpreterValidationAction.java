/* File: TnterpreterValidatonAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 16, 2007
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

package tico.rules.actions;

import java.awt.event.ActionEvent;

import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import tico.interpreter.actions.TInterpreterAbstractAction;
import tico.rules.dialogs.TValidationDialog;

/**
 * Action which opens the board validation dialog.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Oct 16, 2007
 */
public class TInterpreterValidationAction extends TInterpreterAbstractAction {
	/**
	 * Constructor for TInterpreterValidationAction.
	 * 
	 * @param interpreter The boards' interpreter
	 */
	public TInterpreterValidationAction(TInterpreter interpreter) {
		super(interpreter, TLanguage.getString("TInterpreterValidationAction.NAME"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		new TValidationDialog(interpreter);
	}
}
