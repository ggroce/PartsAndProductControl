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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class ModifyPartController {
    @FXML private TextField textfieldId;
    @FXML private TextField textfieldName;
    @FXML private TextField textfieldStock;
    @FXML private TextField textfieldPrice;
    @FXML private TextField textfieldMax;
    @FXML private TextField textfieldMin;
    @FXML private TextField textfieldSource;
    @FXML private RadioButton radiobuttonInHouse;
    @FXML private RadioButton radiobuttonOutsourced;
    @FXML private Label labelSourceSelection;
    private boolean inHouse;
    private int selectedPartIndex;

    private Inventory inventory;

    public void initializeData(Inventory inventory, int selectedPartIndex) {
        this.inventory = inventory;
        this.selectedPartIndex = selectedPartIndex;
        Part selectedPart = this.inventory.getAllParts().get(this.selectedPartIndex);

        textfieldId.setText(Integer.toString(selectedPart.getId()));
        textfieldName.setText(selectedPart.getName());
        textfieldPrice.setText(Double.toString(selectedPart.getPrice()));
        textfieldStock.setText(Integer.toString(selectedPart.getStock()));
        textfieldMax.setText(Integer.toString(selectedPart.getMax()));
        textfieldMin.setText(Integer.toString(selectedPart.getMin()));

        String partSource = selectedPart.getClass().getSimpleName();    //Retrieve part source, (InHouse, Outsourced)

        if (partSource.equals("InHouse")) {
            radioInHouseSelected();
            textfieldSource.setText(Integer.toString(((InHouse)selectedPart).getMachineId()));
        }else {
            radioOutsourcedSelected();
            textfieldSource.setText(((Outsourced)selectedPart).getCompanyName());
        }
    }

    @FXML
    public void radioInHouseSelected() {
        inHouse = true;
        labelSourceSelection.setText("Machine ID");
        radiobuttonInHouse.setSelected(true);
    }

    @FXML
    public void radioOutsourcedSelected() {
        inHouse = false;
        labelSourceSelection.setText("Company Name");
        radiobuttonOutsourced.setSelected(true);
    }

    @FXML
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        returnToMainScreen(event);
    }

    @FXML
    public void saveButtonPushed(ActionEvent event) throws IOException {
        String id = textfieldId.getText();
        String name = textfieldName.getText();
        String price = textfieldPrice.getText();
        String stock = textfieldStock.getText();
        String max = textfieldMax.getText();
        String min = textfieldMin.getText();
        String source = textfieldSource.getText();

        if (Integer.parseInt(max) < Integer.parseInt(min)) {
            showErrorDialog("Maximum inventory must be greater than or equal to minimum.");
        } else
        if(inHouse) {
            try {
                if (name.isEmpty()) {
                    showErrorDialog("All fields must contain a value.");
                } else {
                    Part editedPart = new InHouse(Integer.parseInt(id), name, Double.parseDouble(price),
                            Integer.parseInt(stock), Integer.parseInt(min),
                            Integer.parseInt(max), Integer.parseInt(source));
                    this.inventory.updatePart(selectedPartIndex, editedPart);
                    returnToMainScreen(event);
                }
            } catch (NumberFormatException exception) {
                showErrorDialog("All fields must contain a value.  Price, Stock, Machine ID, Minimum, and Maximum fields must contain a number");
            }
        }else {          //if Outsourced part:
            try {
                if (name.isEmpty() || source.isEmpty()) {
                    showErrorDialog("All fields must contain a value.");
                } else {
                    Part editedPart = new Outsourced(Integer.parseInt(id), name, Double.parseDouble(price),
                            Integer.parseInt(stock), Integer.parseInt(min),
                            Integer.parseInt(max), source);
                    this.inventory.updatePart(selectedPartIndex, editedPart);
                    returnToMainScreen(event);
                }
            } catch (NumberFormatException exception) {
                showErrorDialog("All fields must contain a value.  Price, Stock, Machine ID, Minimum, and Maximum fields must contain a number");
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
