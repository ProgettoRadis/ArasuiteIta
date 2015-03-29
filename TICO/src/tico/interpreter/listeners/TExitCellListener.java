/*
 * File: TExitCellListener.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date:	Dec, 2009 
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

package tico.interpreter.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import tico.components.resources.TFileUtils;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;
import tico.interpreter.actions.TInterpreterExitAction;
import tico.interpreter.components.TInterpreterCell;
import tico.interpreter.threads.TInterpreterMidSound;
import tico.interpreter.threads.TInterpreterMp3Sound;
import tico.interpreter.threads.TInterpreterWavSound;

/**
 * Implements the actions to be performed by an exit control cell
 * 
 * @author Carolina Palacio
 * @version 1.0 Dec, 2009
 */

public class TExitCellListener implements MouseListener {
	
	TInterpreter interpreter;

	public TExitCellListener() {
		interpreter = TInterpreterConstants.interpreter;
	}

	public void mousePressed(MouseEvent arg0) {
		if ((interpreter.run==1)){
			if(arg0.getButton()==MouseEvent.BUTTON3){ //Right button
				TInterpreter.boardListener.mouseClicked(arg0);
			}else{	
				interpreter.run = 0;
				TInterpreterExitAction exit = new TInterpreterExitAction(interpreter);
				exit.actionPerformed(null);
			}
		}			
	}

	public void mouseEntered(MouseEvent arg0) {
		if ((interpreter.run==1)){
			TInterpreterCell controllerCellButton = (TInterpreterCell) arg0.getSource();
			Border thickBorder = new LineBorder(TInterpreterConstants.SELECTED_BORDER_COLOR, 
					TInterpreterConstants.SELECTED_BORDER_SIZE);
			controllerCellButton.setBorder(thickBorder);
			
			
			//if anyadido sonido alternativo suena en modos de barrido
			if (controllerCellButton.getAlternativeSoundPath()!= null && !TInterpreter.returnMouseMode().equals(TInterpreterConstants.DIRECT_SELECTION_MODE)){
				String extension= TFileUtils.getExtension(controllerCellButton.getAlternativeSoundPath());
				if (extension.equals("mp3")){
					TInterpreterConstants.audioMp3 = new TInterpreterMp3Sound(controllerCellButton.getAlternativeSoundPath());
					TInterpreterConstants.audioMp3.TPlay();			 
					//TInterpreterConstants.audioMp3.TJoin();
				}
				
				else if(extension.equals("mid") || extension.equals("midi")) {
					TInterpreterConstants.audioMid=new TInterpreterMidSound(controllerCellButton.getAlternativeSoundPath());	
					TInterpreterConstants.audioMid.start();
					
				}
				
				else {
					TInterpreterConstants.audio=new TInterpreterWavSound(controllerCellButton.getAlternativeSoundPath());	
					TInterpreterConstants.audio.start();
					/*try {
						TInterpreterConstants.audio.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("Error al reproducir el sonido de la celda");
					}*/
				}
			}
		}
	}

	public void mouseExited(MouseEvent arg0) {		
		if ((interpreter.run==1)){
			TInterpreterCell controllerCellButton = (TInterpreterCell) arg0.getSource();
			Border thickBorder = new LineBorder(TInterpreterConstants.BORDER_COLOR, 
					TInterpreterConstants.BORDER_SIZE);
			controllerCellButton.setBorder(thickBorder);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {

	}

	
}

    
