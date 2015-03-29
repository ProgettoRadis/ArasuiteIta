// A mouse listener implementation. Has the control over selection of elements.

package araword.listeners;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import araword.G;
import araword.classes.AWElement;
import araword.classes.AWTextField;

public class MouseListenerImpl implements MouseListener {

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}


	public void mousePressed(MouseEvent e) {
//		System.out.println("mPressed");
    	try {
    		switch (G.selectionState) {
    			case 0: {
					G.selectionState = 1;
					if (e.getSource() instanceof araword.classes.AWElement)
						G.indexSelectionFrom = ((AWElement)e.getSource()).getPosition(); 
					else if (e.getSource() instanceof araword.classes.AWTextField)
						G.indexSelectionFrom = ((AWElement)((AWTextField)e.getSource()).getParent()).getPosition();
					else System.out.println("MousePressed error: wrong mouse event source");
    				break;
    			}
    			case 1: {
    				break;
    			}
    			case 2: {
					G.selectionState = 1;
					for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) {
						G.elementList.get(i).setBackground(Color.WHITE);
					}
					if (e.getSource() instanceof araword.classes.AWElement)
						G.indexSelectionFrom = ((AWElement)e.getSource()).getPosition(); 
					else if (e.getSource() instanceof araword.classes.AWTextField)
						G.indexSelectionFrom = ((AWElement)((AWTextField)e.getSource()).getParent()).getPosition();
					else System.out.println("MousePressed error: wrong mouse event source");
    				break;
    			}
       			default: System.out.println("MousePressed error: wrong state on selection state machine");
    		}
//    		TextUtils.printModel();
    	}	
    	catch (Exception exc) {System.out.println(exc);}
	}


	public void mouseReleased(MouseEvent e) {
//		System.out.println("mReleased");
    	try {
    		switch (G.selectionState) {
    			case 0: {
    				break;
    			}
    			case 1: {
    				if (G.wereDrag) {
						// Let's see if we ended on another component, using lastPointDragged...
						Component[] myComps = G.textZone.getComponents();
						// In fact, an array of ComponentView.Invalidator, a subclass of Container.
						for (Component comp: myComps) {
							Point p = new Point();
			    			Dimension d = new Dimension();
			    			p = comp.getLocation();
			            	d = comp.getSize();
			            	if ((p.getX()<=G.lastPointDragged.getX()) &&
			            		(p.getX()+d.getWidth()>=G.lastPointDragged.getX()) &&
			            		(p.getY()<=G.lastPointDragged.getY()) &&
			            		(p.getY()+d.getHeight()>=G.lastPointDragged.getY())) {
			            		// Point is inside that component.
			            		Component realComp = ((Container)comp).getComponent(0); 
//			            		System.out.println("Drag to element: "+((AWElement)realComp).getPosition());
			            		G.indexSelectionTo = ((AWElement)realComp).getPosition();
								// Add "reverse selection" feature.
								if (G.indexSelectionTo<G.indexSelectionFrom) {
									// Swap indexes.
									int tmp = G.indexSelectionFrom;
									G.indexSelectionFrom = G.indexSelectionTo;
									G.indexSelectionTo = tmp;
								}
//								System.out.println("Selection from "+G.indexSelectionFrom+" to "+G.indexSelectionTo);
								for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) {
									G.elementList.get(i).setBackground(Color.BLUE);
								}
								G.wereDrag = false;
								G.selectionState = 2;
//								TextUtils.printModel();
			            		return;
			            	}
				        }
    				}
    				break;
    			}
    			case 2: {
    				break;
    			}
       			default: System.out.println("MouseReleased error: wrong state on selection state machine");
    		}
    		G.wereDrag = false;
    		G.selectionState = 0; // Not found right place to "end drag", or not applicable, return to nothing selected.
//    		TextUtils.printModel();
    	}	
    	catch (Exception exc) {System.out.println(exc);}
	}
}
