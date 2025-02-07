/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.sun.mail.iap.ConnectionException;
import com.tartanga.grupo4.businesslogic.AdminClientFactory;
import com.tartanga.grupo4.businesslogic.CustomerClientFactory;
import com.tartanga.grupo4.models.Admin;
import com.tartanga.grupo4.models.CreditCardBean;
import com.tartanga.grupo4.models.Customer;
import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.xml.ws.http.HTTPException;
import org.apache.http.conn.HttpHostConnectException;

/**
 *
 * @author Iñi
 */
public class RovoBankMainController {

    private static Logger logger = Logger.getLogger(RovoBankMainController.class.getName());

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @FXML
    private MenuItem menuItemCredit;
    @FXML
    private MenuItem menuItemProfile;

    @FXML
    private MenuItem menuItemCustomers;

    @FXML
    private TableView<Customer> customersTable;

    @FXML
    private TableColumn<Customer, String> columnDni;
    @FXML
    private TableColumn<Customer, String> columnName;
    @FXML
    private TableColumn<Customer, String> columnSurname;
    @FXML
    private TableColumn<Customer, String> columnUsername;
    @FXML
    private TableColumn<Customer, String> columnCity;
    @FXML
    private TableColumn<Customer, String> columnStreet;
    @FXML
    private TableColumn<Customer, String> columnZip;
    @FXML
    private TableColumn<Customer, String> columnTelephone;

    @FXML
    private MenuItem menuitemAbout;

    @FXML
    private void initialize() {

    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        
        try {
            List<Customer> customerList = CustomerClientFactory.customerLogic().findAllCustomers(new GenericType<List<Customer>>() {
            });
            ObservableList dataList = FXCollections.observableList(customerList);
            columnDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
            columnUsername.setCellValueFactory(new PropertyValueFactory<>("logIn"));
            columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
            columnStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
            columnZip.setCellValueFactory(new PropertyValueFactory<>("zip"));
            columnTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
            customersTable.setItems(dataList);
        } catch (ProcessingException e) {
             logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
             Alert alert = new Alert(AlertType.ERROR);
             ButtonType CloseButton = new ButtonType("Close");
             alert.getButtonTypes().setAll(CloseButton);
             alert.setHeaderText("Server Error.");
             alert.setContentText("An error Ocurred. Contact you system administrator");
             alert.showAndWait();
        }

        stage.setTitle("Main");
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    private void handleCreditCard(ActionEvent event) {
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankCreditCardView.fxml"));

            Parent root = (Parent) FXMLLoader.load();

            RovoBankCreditCardController controller = (RovoBankCreditCardController) FXMLLoader.getController();
            controller.setStage(stage);
            controller.initStage(root);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Something went wrong when loading the window.", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewMyProfile(ActionEvent event) {
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankMyProfileView.fxml"));

            Parent root = (Parent) FXMLLoader.load();

            RovoBankMyProfileController controller = (RovoBankMyProfileController) FXMLLoader.getController();
            controller.setStage(stage);
           // controller.setAdmin(admin);
            controller.initStage(root);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Something went wrong when loading the window.", e.getMessage());
        }
    }

    @FXML
    private void handleViewCustomersCreation(ActionEvent event) {
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankCustomersView.fxml"));

            Parent root = (Parent) FXMLLoader.load();

            RovoBankCustomersController controller = (RovoBankCustomersController) FXMLLoader.getController();
            controller.setStage(stage);
            controller.initStage(root);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Something went wrong when loading the window.", e.getMessage());
        }
    }

    @FXML
    private void handleViewAbout(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        ButtonType CloseButton = new ButtonType("Close");
        alert.getButtonTypes().setAll(CloseButton);
        alert.setHeaderText("About");
        alert.setContentText("Authors: Aitor Barrio Pinos, Alin Marian Dadacu, Aratz Eguren Zarraga, Inigo Rodriguez Gonzalo\n\n\n"
                + "                           Workplace: Tartanga, Erandio");
        alert.showAndWait();
    }

}
