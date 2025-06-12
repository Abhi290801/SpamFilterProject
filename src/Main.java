import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            logger.info("Starting the application...");
            GUI.main(args); // Launch the GUI
            logger.info("GUI launched successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while launching the GUI", e);
        }
    }
}