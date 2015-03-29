/*
 * File: TInterpreterUndoAllAction.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Dic, 2009
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

import java.awt.event.ActionEvent;

import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;

/**
 * 
 * @author Carolina Palacio
 * @version e1.0 Dec, 2009
 *
 */

public class TInterpreterUndoAllAction extends TInterpreterAbstractAction{

	
	public TInterpreterUndoAllAction (TInterpreter interpreter){
		super(interpreter,TLanguage.getString("TInterpreterUndoAllAction.NAME"));
	}
	public void actionPerformed(ActionEvent e) {
		
		if (TInterpreter.accumulatedCellsList.size()>0){	
			TInterpreter.accumulatedCells.removeAll();
			TInterpreter.accumulatedCellsList.removeAll(TInterpreter.accumulatedCellsList);
			TInterpreter.accumulatedCells.updateUI();
		}
	}

}
