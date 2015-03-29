/*
 * File: TInterpreterShowAbout.java
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

import java.awt.event.ActionEvent;
import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import tico.interpreter.dialogs.TAboutDialog;


/**
 * Action which exists from the interpreter application.
 * 
 * @author Antonio Rodríguez
 */

public class TInterpreterShowAbout  extends  TInterpreterAbstractAction {

	public TInterpreterShowAbout(TInterpreter interpreter) {
		super(interpreter, TLanguage.getString("TAboutDialog.TITLE"));
	}

	public void actionPerformed(ActionEvent e) {
		new TAboutDialog(getInterpreter());
	}

}
