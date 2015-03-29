/*
 * File: TBoardListener.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Isabel Gonz�lez y Carolina Palacio
 * 
 * Date: Nov, 2009
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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;
import tico.interpreter.components.TInterpreterCell;


public class TBoardListener implements MouseListener {
TInterpreter interprete;
	public TBoardListener(TInterpreter interpreter) {
		interprete=interpreter;
		if (TInterpreterConstants.currentBoard.getOrderedCellListNames().size()!=0){
			TInterpreterConstants.boardOrderedCells = TInterpreterConstants.currentBoard.getOrderedCellListNames();
			int posInicioBarrido = interpreter.getProject().getPositionCellToReturn();
			TInterpreterConstants.countRun = posInicioBarrido;
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
	}

	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getButton()==MouseEvent.BUTTON3){
			if (TInterpreter.returnMouseMode().equals(TInterpreterConstants.MANUAL_SCANNING_MODE)){
				click();
			}	
		}
	}
	
	public void click() {
		if (TInterpreterConstants.countRun < TInterpreterConstants.currentBoard.getOrderedCellListNames().size()){
			TInterpreterCell cell = TInterpreterConstants.currentBoard.getCellByName(TInterpreterConstants.boardOrderedCells.get(TInterpreterConstants.countRun));
			Point point = cell.getLocation();
			SwingUtilities.convertPointToScreen(point, TInterpreter.interpretArea);
    		cell.setCenter(new Point(point.x+cell.getWidth()/2,point.y+cell.getHeight()/2));
    		TInterpreterConstants.x = cell.getCenter().x -1;
    		TInterpreterConstants.y = cell.getCenter().y -1;
    		interprete.interpreterRobot.mouseMove(cell.getCenter().x,cell.getCenter().y);
    		
    		TInterpreterConstants.countRun++;
			if (TInterpreterConstants.countRun==TInterpreterConstants.currentBoard.getOrderedCellListNames().size())
				TInterpreterConstants.countRun=0;
		}
			
		}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {

	}
	
}

    
