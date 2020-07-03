package PartsAndProductControl.Inventory.View_Controller;

import PartsAndProductControl.Inventory.Model.Inventory;
import PartsAndProductControl.Inventory.Model.Part;
import PartsAndProductControl.Inventory.Model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TextField textfieldSearchPart;
    @FXML private TextField textfieldSearchProduct;
    @FXML private TableView<Part> tableviewParts;
    @FXML private TableView<Product> tableviewProducts;
    @FXML private TableColumn<Part, Integer> columnPartId;
    @FXML private TableColumn<Part, String> columnPartName;
    @FXML private TableColumn<Part, Integer> columnPartStock;
    @FXML private TableColumn<Part, Double> columnPartPrice;
    @FXML private TableColumn<Product, Integer> columnProductId;
    @FXML private TableColumn<Product, String> columnProductName;
    @FXML private TableColumn<Product, Integer> columnProductStock;
    @FXML private TableColumn<Product, Double> columnProductPrice;

    private Inventory inventory;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnPartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        columnPartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        columnPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        columnProductId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnProductStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        columnProductPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
    }

    public void initializeData(Inventory inventory) {
        this.inventory = inventory;
        tableviewParts.setItems(this.inventory.getAllParts());
        tableviewProducts.setItems(this.inventory.getAllProducts());
    }

    @FXML
    public void openAddPartScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPartScreen.fxml"));
        Parent root = loader.load();
        AddPartController addPartController = loader.getController();
        addPartController.initializeData(this.inventory);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Add Part");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void openModifyPartScreen(ActionEvent event) throws IOException {
        Part selectedPart = tableviewParts.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {                                                      //Did user select a part to modify?
            int selectedPartIndex = this.inventory.getAllParts().indexOf(selectedPart);  //Get index of selected Part
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPartScreen.fxml"));
            Parent root = loader.load();
            ModifyPartController modifyPartController = loader.getController();
            modifyPartController.initializeData(this.inventory, selectedPartIndex);         //Send selected data to new screen
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    public void openAddProductScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProductScreen.fxml"));
        Parent root = loader.load();
        AddProductController addProductController = loader.getController();
        addProductController.initializeData(this.inventory);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Add Product");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void openModifyProductScreen(ActionEvent event) throws IOException {
        Product selectedProduct = tableviewProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {                                                                //Did user select a product to modify?
            int selectedProductIndex = this.inventory.getAllProducts().indexOf(selectedProduct);      //Get index of selected Product
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProductScreen.fxml"));
            Parent root = loader.load();
            ModifyProductController modifyProductController = loader.getController();
            modifyProductController.initializeData(this.inventory, selectedProductIndex);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    public void searchPartsButtonPushed() {
        String searchString = textfieldSearchPart.getText();
        if (searchString.equals("")) {
            tableviewParts.setItems(this.inventory.getAllParts());      //Reset screen if button clicked and no text input
        } else {
            tableviewParts.setItems(this.inventory.lookupPart(searchString));
            textfieldSearchPart.setText("");
        }
    }

    @FXML
    public void searchProductsButtonPushed() {
        String searchString = textfieldSearchProduct.getText();
        if (searchString.equals("")) {
            tableviewProducts.setItems(this.inventory.getAllProducts());        //Reset screen if button clicked and no text input
        } else {
            tableviewProducts.setItems(this.inventory.lookupProduct(searchString));
            textfieldSearchProduct.setText("");
        }
    }

    @FXML
    public void deletePartButtonPushed() {
        Part selectedPart = tableviewParts.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            this.inventory.deletePart(selectedPart);
        }
    }

    @FXML
    public void deleteProductButtonPushed() {
        Product selectedProduct = tableviewProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            this.inventory.deleteProduct(selectedProduct);
        }
    }

    @FXML
    public void exitApplicationButtonPushed() {
        System.exit(0);
    }
}
