/*
 * JGenfitApp.java
 */

package jgenfit;

import java.awt.Image;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class JGenfitApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
     
        
        
        
    
        JGenfitView jgenfitView = new JGenfitView(this);
        
        show(jgenfitView);
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
     * @return the instance of JGenfitApp
     */
    public static JGenfitApp getApplication() {
        
        return Application.getInstance(JGenfitApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) throws IOException {
        launch(JGenfitApp.class, args);
       
        
    }
}
