// A mouse motion listener implementation. Has the control over selection of elements.

package araword.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import araword.G;
import araword.classes.AWElement;

public class MouseMotionListenerImpl implements MouseMotionListener {


	public void mouseDragged(MouseEvent e) {
//		System.out.println("mDragged");
		try {
    		switch (G.selectionState) {
    			case 0: {
    				break;
    			}
    			case 1: {
    				G.auxPointForDrag = ((AWElement)e.getSource()).getParent().getLocation();
    				G.lastPointDragged.setLocation(G.auxPointForDrag.getX()+e.getX(),G.auxPointForDrag.getY()+e.getY());
    				G.wereDrag = true;
    				break;
    			}
    			case 2: {
    				break;
    			}
       			default: System.out.println("MouseDragged error: wrong state on selection state machine");
    		}
    	}	
    	catch (Exception exc) {System.out.println(exc);}
	}


	public void mouseMoved(MouseEvent e) {
		
	}

}
