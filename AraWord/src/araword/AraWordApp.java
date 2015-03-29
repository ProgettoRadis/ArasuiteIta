// A convenient way of launching GUI.

package araword;

import java.util.EventObject;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import araword.gui.GUI;


/**
 * The main class of the application.
 */
public class AraWordApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new GUI(this));
        
     // Create the ExitListener
        ExitListener exitListener = new ExitListener() {    
            public boolean canExit(EventObject arg0) { 
                // return statement
                return false; 
            }

            public void willExit(EventObject arg0) {}
        };
        // Add the Listener
        addExitListener(exitListener);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of PictoWordApp
     */
    public static AraWordApp getApplication() {
        return Application.getInstance(AraWordApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(AraWordApp.class, args);
    }
    
}
