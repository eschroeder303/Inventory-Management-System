package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * @author Edward Schroeder #9478943
 * C482 Software 1 - QKM2 Task 1
 * IMS: Inventory Management System
 *
 * "FUTURE ENHANCEMENT" to improve the IMS may include:
 * implementation of a linked database system to help manage and archive large amounts of data over time,
 * a function to undo an action or restore the system's state in the event of human error or an abrupt loss of data,
 * inclusion of user accounts and associated privileges with a user login screen for system security measures.
 *
 * .\FinalProjectIMS\Javadoc
 *
 * IMS Main Class
 */
public class Main extends Application {

    /**
     * ID for Parts and Products
     * "RUNTIME ERROR" this code will start each ID at 1 then increment so that if one is deleted, the next will take the old ID.
     */
    private static int partUID = 1;
    private static int prodUID = 1;

    /**
     *
     * @param MainStage - Main Menu
     */
    @Override
    public void start(Stage MainStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
        MainStage.setScene(new Scene(root));
        MainStage.setTitle("IMS: Inventory Management System");
        MainStage.show();
    }

    /**
     * Incremental ID for Parts and Products
     * "RUNTIME ERROR" this code will count each new part and assign a new ID which will solve repeating ID's after a modification or deletion.
     */
    public static int genPartID() { return partUID++; }
    public static int genProductID() { return prodUID++; }

    /**
     *
     * @param args - Main Method
     */
    public static void main(String[] args) { launch(args); }

}