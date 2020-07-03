package PartsAndProductControl.Inventory.View_Controller;

import PartsAndProductControl.Inventory.Model.InHouse;
import PartsAndProductControl.Inventory.Model.Inventory;
import PartsAndProductControl.Inventory.Model.Outsourced;
import PartsAndProductControl.Inventory.Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class AddPartController {
    @FXML private TextField textfieldName;
    @FXML private TextField textfieldStock;
    @FXML private TextField textfieldPrice;
    @FXML private TextField textfieldMax;
    @FXML private TextField textfieldMin;
    @FXML private TextField textfieldSource;
    @FXML private Label labelSourceSelection;
    private boolean inHouse = true;

    private Inventory inventory;

    public void initializeData(Inventory inventory) {
        this.inventory = inventory;
    }

    @FXML
    public void radioInHouseSelected() {
        inHouse = true;
        labelSourceSelection.setText("Machine ID");
        textfieldSource.setPromptText("Mach ID");
    }

    @FXML
    public void radioOutsourcedSelected() {
        inHouse = false;
        labelSourceSelection.setText("Company Name");
        textfieldSource.setPromptText("Comp Nm");
    }

    @FXML
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainScreen(event);
    }

    @FXML public void addPartButtonPushed(ActionEvent event) throws IOException{
        String name = textfieldName.getText();
        String price = textfieldPrice.getText();
        String stock = textfieldStock.getText();
        String max = textfieldMax.getText();
        String min = textfieldMin.getText();
        String source = textfieldSource.getText();

        if (Integer.parseInt(max) < Integer.parseInt(min)) {
            showErrorDialog("Maximum inventory must be greater than or equal to minimum.");
        } else
        if (inHouse) {
            try {
                if (name.isEmpty()) {
                    showErrorDialog("All fields must contain a value.");
                } else {
                    Part newPart = new InHouse(inventory.assignPartId(), name, Double.parseDouble(price),
                            Integer.parseInt(stock), Integer.parseInt(min),
                            Integer.parseInt(max), Integer.parseInt(source));
                    this.inventory.addPart(newPart);
                    returnToMainScreen(event);
                }
            } catch (NumberFormatException exception) {
                showErrorDialog("Price, Stock, Machine ID, Minimum, and Maximum fields must contain a number");
            }
        } else {        //if Outsourced part:
            try {
                if (name.isEmpty() || source.isEmpty()) {
                    showErrorDialog("All fields must contain a value.");
                } else {
                    Part newPart = new Outsourced(inventory.assignPartId(), name, Double.parseDouble(price),
                            Integer.parseInt(stock), Integer.parseInt(min),
                            Integer.parseInt(max), source);
                    this.inventory.addPart(newPart);
                    returnToMainScreen(event);
                }
            } catch (NumberFormatException exception) {
                showErrorDialog("Price, Stock, Machine ID, Minimum, and Maximum fields must contain a number");
            }
        }
    }

    private void showErrorDialog(String errorText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Error on Input");
        alert.setContentText(errorText);
        alert.showAndWait();
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
