package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import Main.Main;

/**
 * Main Form Controller
 * @author Edward Schroeder
 */

public class Main_Form_Controller implements Initializable {

    /**
     * to Modify and Delete
     */
    public Button modifyButton;
    public Button delPart;
    public Button delProd;
    /**
     * table of Parts
     */
    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInvColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    /**
     * table of Products
     */
    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInvColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    /**
     * to search Parts and Products tables
     */
    @FXML private TextField searchParts;
    @FXML private TextField searchProducts;
    /**
     * to search selected Part or Product
     */
    private static Part selectedPart;
    private static Product selectedProduct;
    /**
     * to Exit
     */
    @FXML
    private void exitButton() {
        Platform.exit();
    }

    /**
     *
     * @param event - Add Part form
     */
    @FXML
    private void addPartMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/View/Add_Part_Form.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event - Modify Part form
     */
    @FXML
    private void modifyPartMenu(ActionEvent event) throws IOException {
        if(partTableView.getSelectionModel().getSelectedItem() != null){
            selectedPart = partTableView.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/View/Modify_Part_Form.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Inventory.alertError("Select part to modify.");
        }
    }

    /**
     *
     * @return - if a Part or Product is selected
     */
    public static Part getSelectedPart(){
        return selectedPart;
    }
    public static Product getSelectedProduct(){
        return selectedProduct;
    }
    /**
     *
     * @param event - to Add Product
     */
    @FXML
    private void addProductMenu(ActionEvent event) throws IOException {
        selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/View/Add_Product_Form.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     *
     * @param event - to Modify Product
     */
    @FXML
    private void modifyProductMenu(ActionEvent event) throws IOException {
        if(productTableView.getSelectionModel().getSelectedItem() != null){
            selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/View/Modify_Product_Form.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Inventory.alertError("Select part to modify.");
        }
    }

    /**
     *
     * @param event - Delete selected Part
     */
    @FXML
    private void deletePart(ActionEvent event) {
        Part selected = partTableView.getSelectionModel().getSelectedItem();
        if(partTableView.getSelectionModel().getSelectedItem() != null){
            Optional<ButtonType> result = Inventory.alertDelete();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selected);
            }
            else if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                partTableView.getSelectionModel().clearSelection();
                return;
            }
            partTableView.getSelectionModel().clearSelection();
            ObservableList<Part> allParts = Inventory.getAllParts();
            partTableView.setItems(allParts);
        }
        else{
            Inventory.alertError("Select part to delete.");
        }
    }

    /**
     *
     * @param event - Delete selected Product
     */
    @FXML
    private void deleteProduct(ActionEvent event) {
        selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null){
            Optional<ButtonType> result = Inventory.alertDelete();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                if(selectedProduct.getAllAssociatedParts().size() == 0){
                    Inventory.deleteProduct(selectedProduct);
                }
                else{
                    Inventory.alertError("Remove all associated parts before product deletion.");
                }
            }
            else if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                productTableView.getSelectionModel().clearSelection();
                return;
            }
            productTableView.getSelectionModel().clearSelection();
            ObservableList<Product> allProducts = Inventory.getAllProducts();
            productTableView.setItems(allProducts);
        }
        else{
            Inventory.alertError("Select part to delete.");
        }
    }

    /**
     *
     * @param event - search for Part by Name or ID
     */
    @FXML
    public void partSearch(ActionEvent event){
        String search = searchParts.getText();
        ObservableList<Part> parts = Inventory.searchPart(search);
        ObservableList<Part> allParts = Inventory.getAllParts();
        if(parts.size() == 0){
            try {
                int partID = Integer.parseInt(search);
                Part part = Inventory.searchPart(partID);
                if(part != null){

                    partTableView.getSelectionModel().select(part);
                    return;
                }
                Inventory.alertError("ID not found");
                return;
            }
            catch (NumberFormatException ignored){
                partTableView.getSelectionModel().clearSelection();
                partTableView.setItems(parts);
            }
        }
        partTableView.setItems(parts);
        partTableView.getSelectionModel().clearSelection();
    }

    /**
     *
     * @param event - search for Product by Name or ID
     */
    @FXML
    public void prodSearch(ActionEvent event){
        String search = searchProducts.getText();
        ObservableList<Product> products = Inventory.searchProduct(search);
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        if(products.size() == 0) {
            try {
                int prodID = Integer.parseInt(search);
                Product product = Inventory.searchProduct(prodID);

                if (product != null) {
                    products.setAll(allProducts);
                    productTableView.getSelectionModel().select(product);
                    return;
                }
                Inventory.alertError("ID not found");
                return;
            }
            catch (NumberFormatException ignored) {
                productTableView.getSelectionModel().clearSelection();
                productTableView.setItems(products);
            }
        }
        productTableView.setItems(products);
        productTableView.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        Inventory.addData();
        partTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }
}