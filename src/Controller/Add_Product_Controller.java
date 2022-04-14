package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Add Product Controller
 * @author Edward Schroeder
 */

public class Add_Product_Controller implements Initializable {
    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInvColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TextField nameField;
    @FXML private TextField stockField;
    @FXML private TextField priceField;
    @FXML private TextField minField;
    @FXML private TextField maxField;
    @FXML private TextField searchParts;
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, String> associatedPartIDColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, String> associatedPartInvColumn;
    @FXML private TableColumn<Part, String> associatedPartPriceColumn;
    /**
     * List of Associated Parts
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private static Part selectedPart;

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
     * @param event - Remove selected Part from the Associated Parts table
     */
    @FXML
    public void removePart(ActionEvent event){
        Part selected = associatedPartsTable.getSelectionModel().getSelectedItem();

        if(associatedPartsTable.getSelectionModel().getSelectedItem() != null){
            Optional<ButtonType> result = Inventory.alertDelete();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selected);
            }

            associatedPartsTable.getSelectionModel().clearSelection();
        }

        else{
            Inventory.alertError("Part not selected.");
        }
    }

    /**
     *
     * @param event - Search Event
     */
    @FXML
    public void partSearch(ActionEvent event){
        String search = searchParts.getText();
        ObservableList<Part> parts = Inventory.searchPart(search);

        if(parts.size() == 0){
            try {
                int partID = Integer.parseInt(search);
                Part part = Inventory.searchPart(partID);
                if(part != null){
                    parts.add(part);
                }
            }
            catch (NumberFormatException ignored){
                partTableView.setItems(parts);
            }
        }
        partTableView.setItems(parts);
    }

    /**
     *
     * @param event - to Add Product and Associated Parts
     */
    @FXML
    private void saveProduct(ActionEvent event) throws IOException {
        selectedPart = Main_Form_Controller.getSelectedPart();
        try {
            Double.parseDouble(priceField.getText());
        }
        catch(NumberFormatException e){
            Inventory.alertError("Price is invalid.");
            return;
        }
        try {
            Integer.parseInt(stockField.getText());
        }
        catch(NumberFormatException e) {
            Inventory.alertError("Stock is invalid.");
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
        double p = Double.parseDouble(priceField.getText());
        String name = nameField.getText();
        int stock = Integer.parseInt(stockField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double price = Double.parseDouble(decimalFormat.format(p));

        Product newProduct = new Product(Inventory.genProductID(),name,price,stock,min,max);
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
        else if(priceField.getText().matches("[a-z]") ||  stockField.getText().matches("[a-zA-Z]") || minField.getText().matches("[a-zA-Z]") || maxField.getText().matches(("[a-zA-Z]"))){
            Inventory.alertError("Price, Stock, Min, or Max contains letters.");
        }

        Inventory.addProduct(newProduct);
        if (associatedPartsTable != null){
            for (Part newPart : associatedParts){
                newProduct.addAssociatedPart(newPart);
            }
        }
        Inventory.mainMenu(event);
    }

    @FXML
    private void addButton(ActionEvent event) throws IOException {

        if(partTableView.getSelectionModel().getSelectedItem() != null){
            selectedPart = partTableView.getSelectionModel().getSelectedItem();
            associatedParts.add(selectedPart);
            associatedPartsTable.setItems(associatedParts);
            associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            associatedPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        }
        else{
            Inventory.alertError("Select part to add.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        partTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
}