package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Add Part Controller
 * @author Edward Schroeder
 */

public class Add_Part_Controller {
    public Label toggleLabel;
    public RadioButton InHouseButton;
    public RadioButton OutsourcedButton;
    public Button saveButton;
    public TextField nameField;
    public TextField invField;
    public TextField priceField;
    public TextField id_companyField;
    public TextField minField;
    public TextField maxField;
    public ToggleGroup Toggle;

    /**
     *
     * @param event - Cancel Event
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     *
     * @param event - toggle label between Machine ID and Company Name
     */
    @FXML
    public void InHouseButton(ActionEvent event) { toggleLabel.setText("Machine ID"); }
    @FXML
    public void OutsourcedButton(ActionEvent event) {
        toggleLabel.setText("Company Name");
    }

    /**
     *
     * @param event - to validate and Add Part
     */
    @FXML
    private void savePart(ActionEvent event) throws IOException{
        try {
            Double.parseDouble(priceField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Price is invalid.");
            return;
        }
        try {
            Integer.parseInt(invField.getText());
        }
        catch(NumberFormatException e) {
            Inventory.alertError("Inventory is invalid.");
            return;
        }
        try {
            Integer.parseInt(minField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Minimum is invalid.");
            return;
        }
        try {
            Integer.parseInt(maxField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Maximum is invalid.");
            return;
        }
        String name = nameField.getText();
        double p = Double.parseDouble(priceField.getText());
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        double price = Double.parseDouble(priceFormat.format(p));

        int stock = Integer.parseInt(invField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());

        if (InHouseButton.isSelected()) {
            int id_company = Integer.parseInt(id_companyField.getText());

            InHouse newPart = new InHouse(Inventory.genPartID(),name,price,stock,min,max,id_company);
            int id = newPart.getMachineID();

            if(stock > max){
                Inventory.alertError("Inventory is greater than the maximum.");
                return;
            }
            else if(min > max){
                Inventory.alertError("Minimum is greater than the maximum.");
                return;
            }
            else if(stock < min) {
                Inventory.alertError("Inventory is less than the minimum.");
                return;
            }
            Inventory.addPart(newPart);
        }
        else if (OutsourcedButton.isSelected()) {
            String id_company = id_companyField.getText();
            Outsourced newPart = new Outsourced(Inventory.genProductID(),name,price,stock,min,max,id_company);

            if(stock > max){
                Inventory.alertError("Inventory is greater than the maximum.");
                return;
            }
            else if(min > max){
                Inventory.alertError("Minimum is greater than the maximum.");
                return;
            }
            else if(stock < min) {
                Inventory.alertError("Inventory is less than the minimum.");
                return;
            }
            Inventory.addPart(newPart);
        }
        Inventory.mainMenu(event);
    }
}