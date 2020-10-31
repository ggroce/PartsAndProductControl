package PartsAndProductControl.Inventory.Main;

import PartsAndProductControl.Inventory.Model.*;
import PartsAndProductControl.Inventory.Model.*;
import PartsAndProductControl.Inventory.View_Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws IOException {
        Inventory inventory = new Inventory();
        injectTestData(inventory);                  //Setup test data for display
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/PartsAndProductControl/Inventory/View_Controller/MainScreen.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();     //Get reference in order to make call and pass data
        mainController.initializeData(inventory);
        primaryStage.setTitle("Garrett.Groce Inventory System");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void injectTestData(Inventory inventory) {
        Part test01 = new InHouse(1, "Coupler", 2.22, 3, 1, 5, 44);
        Part test02 = new InHouse(3, "BlasterLop", 5.52, 2, 1, 14, 32);
        Part test03 = new InHouse(4, "DundlerTud", 8.43, 2, 1, 10, 20);
        Part test04 = new Outsourced(7, "Fixtlut", 55.40, 4, 0, 10, "Great Job");
        Part test05 = new Outsourced(9, "Blast Plate", 29.99, 8, 0, 20, "Boxt");
        inventory.addPart(test01);
        inventory.addPart(test02);
        inventory.addPart(test03);
        inventory.addPart(test04);
        inventory.addPart(test05);
        Product test06 = new Product(1, "Non Working Device", 654.34, 1, 0, 5);
        Product test07 = new Product(2, "Fluidian Drive", 2343.22, 2, 0, 8);
        Product test08 = new Product(7, "Rigged Core", 800.44, 1, 1, 6);
        Product test09 = new Product(9, "Pile of Junk", 650.00, 1, 0, 10);
        test06.addAssociatedPart(test01);
        test06.addAssociatedPart(test04);
        test06.addAssociatedPart(test05);
        test07.addAssociatedPart(test01);
        test07.addAssociatedPart(test02);
        test08.addAssociatedPart(test03);
        test09.addAssociatedPart(test01);
        test09.addAssociatedPart(test02);
        test09.addAssociatedPart(test04);
        test09.addAssociatedPart(test05);
        inventory.addProduct(test06);
        inventory.addProduct(test07);
        inventory.addProduct(test08);
        inventory.addProduct(test09);
    }
}
