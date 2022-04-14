package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Modify Part Controller
 * @author Edward Schroeder
 */

public class Modify_Part_Controller implements Initializable {
    @FXML public Button saveButton;
    @FXML public TextField tf_partid;
    @FXML public TextField tf_inv;
    @FXML public TextField tf_price;
    @FXML public TextField tf_machineid;
    @FXML public TextField tf_max;
    @FXML public TextField tf_min;
    public ToggleGroup Toggle;
    @FXML private TextField tf_name;
    private Part selectedPart;
    @FXML public RadioButton InHouseButton;
    @FXML public RadioButton OutsourcedButton;
    @FXML public Label toggleLabel;

    /**
     *
     * @param event - Cancel Event
     */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        Inventory.mainMenu(event);
    }

    /**
     *
     * @param event - to save Modify Part
     */
    @FXML
    private void saveButton(ActionEvent event) throws IOException {
        try {
            Double.parseDouble(tf_price.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Price is invalid.");
            return;
        }
        try {
            Integer.parseInt(tf_inv.getText());
        }
        catch(NumberFormatException e) {
            Inventory.alertError("Stock is invalid.");
            return;
        }
        try {
            Integer.parseInt(tf_min.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Minimum is invalid.");
            return;
        }
        try {
            Integer.parseInt(tf_max.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Maximum is invalid.");
            return;
        }
        String newName = tf_name.getText();

        double p = Double.parseDouble(tf_price.getText());
        DecimalFormat formatPrice = new DecimalFormat("0.00");
        double price = Double.parseDouble(formatPrice.format(p));
        int newStock = Integer.parseInt(tf_inv.getText());
        int newMin = Integer.parseInt(tf_min.getText());
        int newMax = Integer.parseInt(tf_max.getText());
        int stock = Integer.parseInt(tf_inv.getText());
        int index = Inventory.getAllParts().indexOf(selectedPart);

        if(InHouseButton.isSelected()) {
            int newMID = Integer.parseInt(tf_machineid.getText());

            if (tf_name.getText().matches("[0-9]")){
                Inventory.alertError("Name is invalid.");
            }
            if(tf_machineid.getText().matches("[a-zA-Z]")){
                Inventory.alertError("Machine ID is invalid.");
            }
            else if(stock > newMax){
                Inventory.alertError("Inventory is greater than the maximum.");
                return;
            }
            else if(newMin > newMax){
                Inventory.alertError("Minimum is greater than the maximum.");
                return;
            }
            else if(stock < newMin) {
                Inventory.alertError("Inventory is less than the minimum.");
                return;
            }
            InHouse newPart = new InHouse(index+1,newName,price,newStock,newMin,newMax,newMID);
            Inventory.updatePart(index, newPart);
            Inventory.mainMenu(event);
        }
        else if (OutsourcedButton.isSelected()){
            String company = (tf_machineid.getText());

            if (tf_name.getText().matches("[0-9]")) {
                Inventory.alertError("Name is invalid.");
            }
            else if(stock > newMax){
                Inventory.alertError("Inventory is greater than the maximum.");
                return;
            }
            else if(newMin > newMax){
                Inventory.alertError("Minimum is greater than the maximum.");
                return;
            }
            else if(stock < newMin) {
                Inventory.alertError("Inventory is less than the minimum.");
                return;
            }
            Outsourced newPart = new Outsourced(index+1,newName,price,newStock,newMin,newMax,company);
            Inventory.updatePart(index, newPart);
            Inventory.mainMenu(event);
        }
    }

    /**
     *
     * @param event - toggle label between InHouse Machine ID and Outsourced Company Name
     */
    @FXML
    public void InHouseButton(ActionEvent event) {
        toggleLabel.setText("Machine ID");
    }
    @FXML
    public void OutsourcedButton(ActionEvent event) {
        toggleLabel.setText("Company Name");
    }

    @Override
    public void initialize(URL url, ResourceBundle resources){
        selectedPart = Main_Form_Controller.getSelectedPart();

        if (selectedPart instanceof InHouse){
            tf_machineid.setText(String.valueOf(((InHouse) selectedPart).getMachineID()));
            InHouseButton.setSelected(true);
            toggleLabel.setText("Machine ID");
        }
        else if (selectedPart instanceof Outsourced){
            OutsourcedButton.setSelected(true);
            toggleLabel.setText("Company Name");
            tf_machineid.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
        }
        tf_name.setText(selectedPart.getName());
        tf_inv.setText(String.valueOf(selectedPart.getStock()));
        tf_max.setText(String.valueOf(selectedPart.getMax()));
        tf_min.setText(String.valueOf(selectedPart.getMin()));
        tf_price.setText(String.valueOf(selectedPart.getPrice()));
    }
}