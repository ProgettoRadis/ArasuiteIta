/*
 * File: TInterpreterFullscreenAction.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 *
 * Authors: Rodrigo Perez Fulloni
 *
 * Date:	Mar, 2012
 *
 * Company: Fundacion Teleton, Montevideo, Uruguay
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

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;

import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

/**
 * 
 * @author Rodrigo Perez Fulloni
 *
 */
public class TInterpreterFullscreenAction extends TInterpreterAbstractAction {

    private mouseMotionListener mml;
    private Toolkit t;
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

    public TInterpreterFullscreenAction(TInterpreter interpreter) {
        super(interpreter, TLanguage.getString("TInterpreterFullscreenAction.NAME"));    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        interpreter = getInterpreter();

        switchFullScreen();
    }
    
    JMenuBar menu;
    JSplitPane sp;
    int dividerLocation;
    int dividerSize;
    
    private void switchFullScreen() {   
        
        //hide the frame to change its propieties
        interpreter.setVisible(false);
        interpreter.dispose();
        
        sp = TInterpreter.splitPane;
        menu = interpreter.getJMenuBar();

        if (TInterpreter.fullscreen) {
            interpreter.setExtendedState(JFrame.NORMAL);
            interpreter.setUndecorated(false);
            interpreter.setAlwaysOnTop(false);

            sp.setDividerLocation(dividerLocation);
            sp.setDividerSize(dividerSize);

            removeMouseListener();
        } else {
            dividerLocation = sp.getDividerLocation();
            dividerSize = sp.getDividerSize();
            //activate the fullscreen mode
            interpreter.setExtendedState(JFrame.MAXIMIZED_BOTH);
            interpreter.setUndecorated(true);
            interpreter.setAlwaysOnTop(true);

            //add the auto hide menu
            createMouseListener();
        }

        //show the frame again
        interpreter.setVisible(true);

        TInterpreter.fullscreen = !TInterpreter.fullscreen;

        interpreter.repaintCurrentBoard(true);
    }

    //Creates the awt mouse listener
    private void createMouseListener() {
        mml = new mouseMotionListener();
        long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK;
        t = Toolkit.getDefaultToolkit();
//        t.beep();
        t.addAWTEventListener(mml, eventMask);
    }

    class mouseMotionListener implements AWTEventListener {

        public void mouseMoved(MouseEvent e) {
            //check if there is an open menu        
            boolean hasSelectedMenu = false;
            for (int i = 0; i < menu.getMenuCount() && !hasSelectedMenu; ++i) {
                if (menu.getMenu(i).isSelected()) {
                    hasSelectedMenu = true;
                }
            }

            Point pos = MouseInfo.getPointerInfo().getLocation();
            if (!hasSelectedMenu) {
                if (menu.isVisible()) {
                    menu.setVisible(pos.y < menu.getHeight());
                } else {
                    menu.setVisible(pos.y < 5);
                }
            }

            if (pos.y > screenSize.height - 15) {
                sp.setDividerLocation(screenSize.height - 100);
                sp.setDividerSize(5);
            } else if (pos.y < screenSize.height - 100) {
                sp.setDividerLocation(screenSize.height - 5);
                sp.setDividerSize(0);
            }
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            mouseMoved((MouseEvent) event);
        }
    }

    private void removeMouseListener() {
        t.removeAWTEventListener(mml);
    }
}
