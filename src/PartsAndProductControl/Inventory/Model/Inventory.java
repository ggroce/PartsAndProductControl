package PartsAndProductControl.Inventory.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private int partIdTracker = 10;             //Started at 10 to accommodate test data
    private int productIdTracker = 10;

    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    public static boolean deletePart(Part selectedPart) {
        if (allParts.remove(selectedPart)){
            return true;
        };
        return false;
    }

    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.remove(selectedProduct)){
            return true;
        };
        return false;
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static ObservableList<Part> lookupPart(String searchString) {
        ObservableList<Part> searchPartResults = FXCollections.observableArrayList();
        for(Part searchedPart : getAllParts()) {
            if (searchedPart.getName().toLowerCase().contains(searchString.toLowerCase())) {
                searchPartResults.add(searchedPart);
            }
            if (searchString.equals(Integer.toString(searchedPart.getId()))) {
                    searchPartResults.add(lookupPart(searchedPart.getId()));        //Could have just added part, but exhibiting lookupPart method
            }
        }
        return searchPartResults;
    }

    public static ObservableList<Product> lookupProduct(String searchString) {
        ObservableList<Product> searchProductResults = FXCollections.observableArrayList();
        for(Product searchedProduct : getAllProducts()) {
            if (searchedProduct.getName().toLowerCase().contains(searchString.toLowerCase())) {
                searchProductResults.add(searchedProduct);
            }
            if (searchString.equals(Integer.toString(searchedProduct.getId()))) {
                searchProductResults.add(lookupProduct(searchedProduct.getId()));
            }
        }
        return searchProductResults;
    }

    public static Part lookupPart(int partId) {
        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }
        throw new RuntimeException("No PartID found by that part number.");         //Shouldn't get here by it's use, for implementation
    }

    public static Product lookupProduct(int productId) {
        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        throw new RuntimeException("No ProductID found by that product number.");     //Shouldn't get here by it's use, for implementation
    }

    public int assignPartId() {
        return ++partIdTracker;
    }

    public int assignProductId() {
        return ++productIdTracker;
    }
}
