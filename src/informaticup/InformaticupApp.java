package informaticup;

import informaticup.Parser.ParserException;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * Diese Klasse enthält die Main-Funktion, die beim Programmstart aufgerufen wird.
 */
public class InformaticupApp extends SingleFrameApplication {

    public static String[] p_args;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new InformaticupView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of InformaticupApp
     */
    public static InformaticupApp getApplication() {
        return Application.getInstance(InformaticupApp.class);
    }

    /**
     * Diese Funktion wird aufgerufen, wenn das Programm gestartet wird.
     * @param args Parameter
     */
    public static void main(String[] args) throws ParserException, Exception {
        p_args = args;

        // take the menu bar off the jframe
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // set the name of the application menu item
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ATM Spreader");

        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");

        launch(InformaticupApp.class, args);
    }
}
