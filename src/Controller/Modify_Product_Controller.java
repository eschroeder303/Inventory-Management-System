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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Modify Product Controller
 * @author Edward Schroeder
 */

public class Modify_Product_Controller implements Initializable {
    public TextField tf_name;
    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, String> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, String> partInvColumn;
    @FXML private TableColumn<Part, String> partPriceColumn;
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, String> associatedPartIDColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, String> associatedPartInvColumn;
    @FXML private TableColumn<Part, String> associatedPartPriceColumn;
    @FXML public TextField stockField;
    @FXML public TextField priceField;
    @FXML public TextField maxField;
    @FXML public TextField minField;
    @FXML private TextField nameField;
    @FXML private TextField searchParts;

    private Part selectedPart;
    private Product selectedProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

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
     * @param event - Search Event
     */
    @FXML
    private void partSearch(ActionEvent event){

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
     * @param event - Save Event
     */
    @FXML
    private void saveProduct(ActionEvent event) throws IOException {
        String name = nameField.getText();
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
            Inventory.alertError("Max is invalid.");
            return;
        }
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());
        int index = Inventory.getAllProducts().indexOf(selectedProduct);

        Product newProduct = new Product(index+1,name,price,stock,min,max);
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
        Inventory.updateProduct(index, newProduct);
        if (associatedPartsTable != null){
            for (Part newPart : associatedParts){
                newProduct.addAssociatedPart(newPart);
            }
        }
        Inventory.mainMenu(event);
    }

    @FXML
    public void removeAssociatedPart(){
        Part selected = associatedPartsTable.getSelectionModel().getSelectedItem();
        if(associatedPartsTable.getSelectionModel().getSelectedItem() != null){
            Optional<ButtonType> result = Inventory.alertDelete();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selected);
            }
            associatedPartsTable.getSelectionModel().clearSelection();
        }
        else{
            Inventory.alertError("Select part to remove.");
        }
    }

    @FXML
    public void addAssociatedPart(){
        selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (partTableView.getSelectionModel().getSelectedItem() != null){
            associatedParts.add(selectedPart);
        }
        else {
            Inventory.alertError("Select part to add.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        selectedProduct = Main_Form_Controller.getSelectedProduct();
        associatedParts = selectedProduct.getAllAssociatedParts();
        partTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        if(selectedProduct != null){
            associatedPartsTable.setItems(associatedParts);
            nameField.setText(selectedProduct.getName());
            stockField.setText(String.valueOf(selectedProduct.getStock()));
            maxField.setText(String.valueOf(selectedProduct.getMax()));
            minField.setText(String.valueOf(selectedProduct.getMin()));
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
        }
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    }
}