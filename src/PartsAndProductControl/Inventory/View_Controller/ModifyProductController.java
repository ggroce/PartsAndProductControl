package PartsAndProductControl.Inventory.View_Controller;

import PartsAndProductControl.Inventory.Model.*;
import PartsAndProductControl.Inventory.Model.Inventory;
import PartsAndProductControl.Inventory.Model.Part;
import PartsAndProductControl.Inventory.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {
    @FXML private TextField textfieldSearchPart;
    @FXML private TextField textfieldId;
    @FXML private TextField textfieldName;
    @FXML private TextField textfieldStock;
    @FXML private TextField textfieldPrice;
    @FXML private TextField textfieldMax;
    @FXML private TextField textfieldMin;
    @FXML private TableView<Part> tableviewParts;
    @FXML private TableView<Part> tableviewProduct;
    @FXML private TableColumn<Part, Integer> columnPartId;
    @FXML private TableColumn<Part, String> columnPartName;
    @FXML private TableColumn<Part, Integer> columnPartStock;
    @FXML private TableColumn<Part, Double> columnPartPrice;
    @FXML private TableColumn<Part, Integer> columnChosenPartId;
    @FXML private TableColumn<Part, String> columnChosenPartName;
    @FXML private TableColumn<Part, Integer> columnChosenPartStock;
    @FXML private TableColumn<Part, Double> columnChosenPartPrice;

    private Inventory inventory;
    private ObservableList<Part> partsList = FXCollections.observableArrayList();
    private int selectedProductIndex;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnPartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        columnPartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        columnPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        columnChosenPartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        columnChosenPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        columnChosenPartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        columnChosenPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
    }

    public void initializeData(Inventory inventory, int selectedProductIndex) {
        this.inventory = inventory;
        this.selectedProductIndex = selectedProductIndex;
        Product selectedProduct = this.inventory.getAllProducts().get(this.selectedProductIndex);

        textfieldId.setText(Integer.toString(selectedProduct.getId()));
        textfieldName.setText(selectedProduct.getName());
        textfieldPrice.setText(Double.toString(selectedProduct.getPrice()));
        textfieldStock.setText(Integer.toString(selectedProduct.getStock()));
        textfieldMax.setText(Integer.toString(selectedProduct.getMax()));
        textfieldMin.setText(Integer.toString(selectedProduct.getMin()));

        partsList.addAll(selectedProduct.getAllAssociatedParts());

        tableviewParts.setItems(this.inventory.getAllParts());
        tableviewProduct.setItems(partsList);
    }

    @FXML
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainScreen(event);
    }

    @FXML
    public void saveButtonPushed(ActionEvent event) throws IOException{
        String id = textfieldId.getText();
        String name = textfieldName.getText();
        String price = textfieldPrice.getText();
        String stock = textfieldStock.getText();
        String max = textfieldMax.getText();
        String min = textfieldMin.getText();
        double totalPartsPrice = 0.0;

        try {
            for (Part part : partsList) {               //add selected parts together to see total
                totalPartsPrice += part.getPrice();
            }
            if (name.isEmpty()) {
                showErrorDialog("All fields must contain a value.");
            } else
            if (Integer.parseInt(max) < Integer.parseInt(min)) {
                showErrorDialog("Maximum inventory must be greater than or equal to minimum.");
            } else
            if (Double.parseDouble(price) < totalPartsPrice || totalPartsPrice == 0.0) {      //Show error dialog if product cost less than total cost of all parts.
                showErrorDialog("The Product cost must be greater than or equal to the sum of its constituent parts, and at least one part must be included.");
            } else {
                Product editedProduct = new Product(Integer.parseInt(id), name, Double.parseDouble(price),
                        Integer.parseInt(stock), Integer.parseInt(min), Integer.parseInt(max));
                for (Part part : partsList) {                     //Add Parts to Product before adding to Inventory
                    editedProduct.addAssociatedPart(part);
                }
                this.inventory.updateProduct(selectedProductIndex, editedProduct);
                returnToMainScreen(event);
            }
        } catch (NumberFormatException exception) {
            showErrorDialog("Price, Stock, Minimum, and Maximum fields must contain a number");
        }
    }

    private void showErrorDialog(String errorText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Error on Input");
        alert.setContentText(errorText);
        alert.showAndWait();
    }

    @FXML
    public void addPartButtonPushed() {
        Part addPart = tableviewParts.getSelectionModel().getSelectedItem();
        if (addPart != null) {
            partsList.add(addPart);
        }
    }

    @FXML
    public void deletePartButtonPushed() {
        Part selectedPart = tableviewProduct.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            partsList.remove(selectedPart);
        }
    }

    @FXML
    public void searchPartsButtonPushed() {
        String searchString = textfieldSearchPart.getText();
        if (searchString.equals("")) {
            tableviewParts.setItems(this.inventory.getAllParts());      //Reset screen if no text input.
        } else {
            tableviewParts.setItems(this.inventory.lookupPart(searchString));
            textfieldSearchPart.setText("");
        }
    }

    private void returnToMainScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.initializeData(this.inventory);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Garrett.Groce Inventory System");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
