/*
 * File: TInterpreterRun.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date:	Feb, 2010
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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;
import tico.interpreter.TInterpreterProject;
import tico.interpreter.components.TInterpreterCell;
import tico.interpreter.listeners.TBoardListener;
import tico.interpreter.threads.TInterpreterBoardSound;
import tico.interpreter.threads.TThreads;

/**
 * 
 * @author Carolina Palacio
 * @version e1.0 Feb, 2010
 *
 */

public class TInterpreterRun extends TInterpreterAbstractAction implements ActionListener, Runnable {
 		
	public TInterpreterRun (TInterpreter interpreter) {
		super(interpreter,TLanguage.getString("TInterpreterRunAction.NAME"),TResourceManager.getImageIcon("run.gif"));
	}	

	public void actionPerformed(ActionEvent e) {
		
		interpreter=getInterpreter();
			
		try {
			TInterpreterConstants.semaforo.releaseWhenStop();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		TInterpreter.interpretationThread = new TThreads(this);
		TInterpreter.interpretationThread.start();
		
	}
	
	public void run() {
		
		String mouseMode = TInterpreter.returnMouseMode();
		
		TInterpreter.run = 1;
		
		interpreter.TIntepreterChangeCursor();
	
	    TInterpreterProject project = interpreter.getProject();
	    TInterpreterConstants.currentBoard = project.getBoard(project.getBoardToGo());
	    project.setBoardChanged(false);
	    
	    //If the first board has a sound, play it when browsing is activated
	    if (TInterpreterConstants.currentBoard.getSoundFile()!=null){
			TInterpreterBoardSound boardSound = new TInterpreterBoardSound(TInterpreterConstants.currentBoard.getSoundFile());
			boardSound.run();
		}
	    
	    if (mouseMode.equals(TInterpreterConstants.MANUAL_SCANNING_MODE)){
	    	interpreter.interpreterRobot.setAutoDelay(0);
	    	TInterpreter.boardListener = new TBoardListener(interpreter);
	  	    TInterpreter.interpretAreaBackground.addMouseListener(TInterpreter.boardListener);
	  	    TInterpreter.accumulatedCells.addMouseListener(TInterpreter.boardListener);
	    }
	    
	    //Browsing mode
		if (mouseMode.equals(TInterpreterConstants.AUTOMATIC_SCANNING_MODE)){
			
			interpreter.interpreterRobot.setAutoDelay(TInterpreterConstants.interpreterDelay);
			
			if (TInterpreterConstants.currentBoard.getOrderedCellListNames().size()!=0){
				TInterpreterConstants.boardOrderedCells = TInterpreterConstants.currentBoard.getOrderedCellListNames();
				int posInicioBarrido = interpreter.getProject().getPositionCellToReturn();
				TInterpreterConstants.countRun = posInicioBarrido;
				
				while (TInterpreterConstants.countRun < TInterpreterConstants.currentBoard.getOrderedCellListNames().size() && TInterpreter.run==1){		
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {						
						TInterpreterConstants.semaforo.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					//Si he cambiado de tablero y no tengo celdas paro el barrido
					if (TInterpreterConstants.currentBoard.getOrderedCellListNames().size()==0){
						TInterpreter.run = 0;
					}else{
					
						TInterpreterCell cell = TInterpreterConstants.currentBoard.getCellByName(TInterpreterConstants.boardOrderedCells.get(TInterpreterConstants.countRun));
						Point point = cell.getLocation();
						SwingUtilities.convertPointToScreen(point, TInterpreter.interpretArea);
	            		cell.setCenter(new Point(point.x+cell.getWidth()/2,point.y+cell.getHeight()/2));
	            		
	            		TInterpreterConstants.x = cell.getCenter().x -1;
	            		TInterpreterConstants.y = cell.getCenter().y -1;
	            		interpreter.interpreterRobot.mouseMove(cell.getCenter().x,cell.getCenter().y);
	
	            		TInterpreterConstants.countRun++;
						if (TInterpreterConstants.countRun==TInterpreterConstants.currentBoard.getOrderedCellListNames().size())
							TInterpreterConstants.countRun=0;
					}
					
					try {
						TInterpreterConstants.semaforo.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}								
			}
		} 
	}
	
 }

