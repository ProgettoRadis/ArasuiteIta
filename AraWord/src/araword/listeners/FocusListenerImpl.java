// A focus listener for textfields. Quite simple, only sets active element and scrolls accordingly the textZone.

package araword.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import araword.G;
import araword.classes.AWElement;
import araword.classes.AWTextField;

public class FocusListenerImpl implements FocusListener {
	
	public void focusGained(FocusEvent e) {
    	if (!(e.getComponent() instanceof AWTextField)) return; // Possibly optional, just for added security.
    	
    	// Set activeElement...
    	G.activeComponent = e.getComponent();
    	G.activeElement = (AWElement)G.activeComponent.getParent();
    	G.activeElementPosition = G.activeElement.getPosition();
    	// Corrects scroll problem to last element.
    	G.activeElement.scrollRectToVisible(G.activeElement.getBounds());    
    	
//    	System.out.println("Focus won: "+G.activeElement.getTextField().getText());
    }

    public void focusLost(FocusEvent e) {
//    	System.out.println("Focus lost: "+G.activeElement.getTextField().getText());
    }

}
