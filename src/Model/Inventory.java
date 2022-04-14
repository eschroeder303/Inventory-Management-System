package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import Main.Main;

/**
 * Inventory Class
 * @author Edward Schroeder
 */

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart - Add new Part or Product
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     *
     * @param partID - Part being searched for
     * @return part - with matched ID
     */
    public static Part searchPart(int partID) {
        ObservableList<Part> allParts = getAllParts();
        for (Part part : allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    /**
     *
     * @param productID - Product being searched for
     * @return product - with matched ID
     */
    public static Product searchProduct(int productID) {
        ObservableList<Product> allProducts = getAllProducts();
        for (Product product : allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }

    /**
     *
     * @return - size of Parts or Products List + 1
     */
    public static int genPartID() {
        return allParts.size() + 1;
    }
    public static int genProductID() {
        return allProducts.size() + 1;
    }

    /**
     *
     * @param partName - Part being searched for
     * @return parts - associated with matched Name
     */
    public static ObservableList<Part> searchPart(String partName){
        ObservableList<Part> searchedPart = FXCollections.observableArrayList();
        ObservableList<Part> allParts = getAllParts();
        for(Part part:allParts){
            if (part.getName().contains(partName)){
                searchedPart.add(part);
            }
        }
        return searchedPart;
    }

    /**
     *
     * @param productName - Product being searched for
     * @return products - associated with matched Name
     */
    public static ObservableList<Product> searchProduct(String productName) {
        ObservableList<Product> searchedProduct = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = getAllProducts();
        for(Product product:allProducts){
            if (product.getName().contains(productName)){
                searchedProduct.add(product);
            }
        }
        return searchedProduct;
    }

    /**
     *
     * @param index - index of the Part
     * @param selectedPart - Part being selected
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index,selectedPart);
    }

    /**
     *
     * @param index - index of the Product
     * @param newProduct - Product being modified
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     *
     * @param selectedPart - Part being selected
     * @return true - Delete selected Part
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     *
     * @param selectedProduct - Product being selected
     * @return true - Delete selected Product
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     *
     * @return - Parts or Products List
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Sample data
     * "RUNTIME ERROR" this code will stop the sample data from being repeatedly added each time Main_Form_Controller is called.
     */
    private static boolean first = true;
    public static void addData(){
        if (!first){
            return;
        }
        first = false;

        InHouse Brakes = new InHouse(Main.genPartID(),"Brakes",15.00, 19, 1, 20, 5681);
        Inventory.addPart(Brakes);

        InHouse Wheel = new InHouse(Main.genPartID(),"Wheel", 25.00,3, 1, 20, 5972);
        Inventory.addPart(Wheel);

        Outsourced Seat = new Outsourced(Main.genPartID(),"Seat", 20.00,8, 1, 20, "Edward Bike Co");
        Inventory.addPart(Seat);

        Product GiantBike = new Product(Main.genProductID(), "Giant Bike",200.00, 3, 1, 3);
        GiantBike.addAssociatedPart(Brakes);
        Inventory.addProduct(GiantBike);

        Product Tricycle = new Product(Main.genProductID(), "Tricycle", 150.00, 2, 1, 5);
        Tricycle.addAssociatedPart(Wheel);
        Inventory.addProduct(Tricycle);

    }

    /**
     *
     * @return - Confirmation Alert
     */
    public static Optional<ButtonType> alertDelete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setContentText("Confirm?");
        return alert.showAndWait();
    }

    /**
     *
     * @param message - Error Alert
     */
    public static void alertError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     *
     * @param event - Return to Main Menu
     */
    public static void mainMenu(Event event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/View/Main_Form.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}