/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.CustomerClientFactory;
import com.tartanga.grupo4.businesslogic.ProductClientFactory;
import com.tartanga.grupo4.models.Customer;
import com.tartanga.grupo4.models.Product;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public class RovoBankCustomersController {

    private static Logger logger = Logger.getLogger(RovoBankCustomersController.class.getName());

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    private Button btn_Enroll;

    private Customer customer;

    @FXML
    private TextField fld_Email;

    @FXML
    private TextField fld_Name;

    @FXML
    private TextField fld_Surname;

    @FXML
    private TextField fld_City;

    @FXML
    private TextField fld_Street;

    @FXML
    private TextField fld_Zip;

    @FXML
    private TextField fld_Dni;

    @FXML
    private TextField fld_Telephone;

    @FXML
    private Label lbl_error_Email;

    @FXML
    private Label lbl_error_Street;

    @FXML
    private Label lbl_error_Name;

    @FXML
    private Label lbl_error_Surname;

    @FXML
    private Label lbl_error_City;

    @FXML
    private Label lbl_error_Zip;

    @FXML
    private Label lbl_error_Dni;

    @FXML
    private Label lbl_error_Telephone;

    @FXML
    private void initialize() {
        btn_Enroll.setOnAction(this::handleCreateCustomer);
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("Customers creation");
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    private void handleCreateCustomer(ActionEvent event) {

        String email = fld_Email.getText();
        String name = fld_Name.getText();
        String surname = fld_Surname.getText();
        String city = fld_City.getText();
        String street = fld_Street.getText();
        String zip = fld_Zip.getText();
        String dni = fld_Dni.getText();
        String telephone = fld_Telephone.getText();
        boolean hasError = false;

        //Validate Email
        if (email.isEmpty()) {
            lbl_error_Email.setText("Email is required.");
            hasError = true;
        } else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            lbl_error_Email.setText("Please enter a valid email address.");
            hasError = true;
        } else {
            lbl_error_Email.setText("");
        }

        // Validate name
        if (name.isEmpty()) {
            lbl_error_Name.setText("Name is required.");
            hasError = true;
        } else if (name.matches(".*\\d.*")) {
            lbl_error_Name.setText("Name cannot contain numbers.");
            hasError = true;
        } else {
            lbl_error_Name.setText("");
        }

        // Validate surname
        if (surname.isEmpty()) {
            lbl_error_Surname.setText("Surname is required.");
            hasError = true;
        } else if (surname.matches(".*\\d.*")) {
            lbl_error_Surname.setText("Surname cannot contain numbers.");
            hasError = true;
        } else {
            lbl_error_Surname.setText("");
        }

        // Validate city
        if (city.isEmpty()) {
            lbl_error_City.setText("City is required.");
            hasError = true;
        } else if (city.matches(".*\\d.*")) {
            lbl_error_City.setText("City cannot contain numbers.");
            hasError = true;
        } else {
            lbl_error_City.setText("");
        }

        // Validate street
        if (street.isEmpty()) {
            lbl_error_Street.setText("Street is required.");
            hasError = true;
        } else if (street.matches(".*\\d.*")) {
            lbl_error_Street.setText("Street cannot contain numbers.");
            hasError = true;
        } else {
            lbl_error_Street.setText("");
        }

        // Validate ZIP code
        if (zip.isEmpty()) {
            lbl_error_Zip.setText("ZIP code is required.");
            hasError = true;
        } else if (!zip.matches("\\d{1,10}")) {
            lbl_error_Zip.setText("Invalid ZIP code.");
            hasError = true;
        } else {
            lbl_error_Zip.setText("");
        }

        //Validate DNI
        if (dni.isEmpty()) {
            lbl_error_Dni.setText("DNI is required.");
            hasError = true;
        } else if (!dni.matches("\\d{8}[A-HJ-NP-TV-Z]")) {  // 8 digits followed by a valid letter
            lbl_error_Dni.setText("Invalid DNI.");
            hasError = true;
        } else {
            lbl_error_Dni.setText("");
        }

        //Validate Telephone
        if (telephone.isEmpty()) {
            lbl_error_Telephone.setText("Phone number is required.");
            hasError = true;
        } else if (!telephone.matches("\\d{9}")) {  // Exactly 9 digits
            lbl_error_Telephone.setText("Invalid phone number. Exactly 9 digits.");
            hasError = true;
        } else {
            lbl_error_Telephone.setText("");
        }

        if (!hasError) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            ButtonType CloseButton = new ButtonType("Close");
            alert2.getButtonTypes().setAll(CloseButton);
            customer = new Customer();
            customer.setLogIn(fld_Email.getText());
            customer.setName(fld_Name.getText());
            customer.setSurname(fld_Surname.getText());
            customer.setCity(fld_City.getText());
            customer.setStreet(fld_Street.getText());
            customer.setZip(Integer.parseInt(fld_Zip.getText()));
            customer.setDni(fld_Dni.getText());
            customer.setTelephone(fld_Telephone.getText());
            CustomerClientFactory.customerLogic().createCustomer(customer);

            alert2.setTitle("Successful");
            alert2.setHeaderText("Customer created successfully.");
            alert2.setContentText("You can now enroll more customers.");
            alert2.showAndWait();
            clearFields();
            logger.info("Customer created correctly.");
        }

    }

    private void clearFields() {
        fld_Email.clear();
        fld_Name.clear();
        fld_City.clear();
        fld_Street.clear();
        fld_Zip.clear();
        fld_Surname.clear();
        fld_Dni.clear();
        fld_Telephone.clear();
        lbl_error_Email.setText("");
        lbl_error_Name.setText("");
        lbl_error_City.setText("");
        lbl_error_Street.setText("");
        lbl_error_Zip.setText("");
        lbl_error_Telephone.setText("");
        lbl_error_Dni.setText("");
    }

}
