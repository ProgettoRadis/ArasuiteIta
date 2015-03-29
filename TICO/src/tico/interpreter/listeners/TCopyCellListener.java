/*
 * File: TCopyCellListener.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Rodrigo Perez
 * 
 * Date:	Sep, 2012
 *  
 * Company: Fundacion Teleton Uruguay
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
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;
import tico.interpreter.actions.TInterpreterCopyAction;
import tico.interpreter.components.TInterpreterCell;

/**
 *
 * @author Rodrigo
 */
public class TCopyCellListener implements MouseListener {

    TInterpreter interpreter;

    public TCopyCellListener() {
        interpreter = TInterpreterConstants.interpreter;
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        if ((interpreter.run == 1)) {
            if (arg0.getButton() == MouseEvent.BUTTON3) { //Right button
                TInterpreter.boardListener.mouseClicked(arg0);
            } else {
                TInterpreterCopyAction copy = new TInterpreterCopyAction(interpreter);
                copy.actionPerformed(null);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        if ((interpreter.run == 1)) {
            TInterpreterCell controllerCellButton = (TInterpreterCell) arg0.getSource();
            Border thickBorder = new LineBorder(TInterpreterConstants.SELECTED_BORDER_COLOR, TInterpreterConstants.SELECTED_BORDER_SIZE);
            controllerCellButton.setBorder(thickBorder);
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        if ((interpreter.run == 1)) {
            TInterpreterCell controllerCellButton = (TInterpreterCell) arg0.getSource();
            Border thickBorder = new LineBorder(TInterpreterConstants.BORDER_COLOR, TInterpreterConstants.BORDER_SIZE);
            controllerCellButton.setBorder(thickBorder);
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
