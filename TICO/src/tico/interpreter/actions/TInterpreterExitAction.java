/*
 * File: TInterpreterExitAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez
 * 
 * Date:	May, 2007
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
import tico.configuration.TSetup;
import tico.editor.TFileHandler;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;
import tico.interpreter.threads.TInterpreterTTS;


/**
 * Action which exists from the interpreter application.
 * 
 * @author Antonio Rodríguez
 * @version 1.0 May, 2007
 */

public class TInterpreterExitAction extends TInterpreterAbstractAction {
	/**
	 * Constructor for TInterpreterExitAction.
	 * 
	 * @param interpreter The interpreter' interpreter
	 */
	public TInterpreterExitAction(TInterpreter interpreter) {
		super(interpreter, TLanguage.getString("TInterpreterExitAction.NAME"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Delete the project
		getInterpreter().deleteProject();
		TFileHandler.deleteCurrentDirectory();
		try {
			TSetup.save();
		} catch (Exception ex) {
		}
		getInterpreter().dispose();
		
		if (interpreter.interpretationThread!=null)
		{
			if (interpreter.interpretationThread.isAlive())
			{		
			interpreter.interpretationThread.stop();
			
			};
		}
		
		
		if (interpreter.interpreterRobot!=null)
			interpreter.interpreterRobot=null;
		
		//
		//Stop Current Sound
		
		if (TInterpreterConstants.audio != null)
		{
			TInterpreterConstants.audio.stop();
		}
		if(TInterpreterConstants.audioMp3!=null)
		{
			TInterpreterConstants.audioMp3.TStop();
		}
		System.gc();
                
                TInterpreterTTS tts = TInterpreterTTS.getInstance();
                tts.unload();
}
	
}
