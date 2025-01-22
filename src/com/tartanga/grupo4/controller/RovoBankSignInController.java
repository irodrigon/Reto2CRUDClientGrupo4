/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.AdminClientFactory;
import com.tartanga.grupo4.models.Admin;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public class RovoBankSignInController {

    private static Logger logger;

    private Admin admin;

    private boolean isOn = false;

    @FXML
    private Button btn_Login;

    @FXML
    private Hyperlink hl_create;

    @FXML
    private Button btnSeePassword;

    @FXML
    private TextField hiddenField;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    private void initialize() {
        btn_Login.setOnAction(this::handleLogin);
        hl_create.setOnAction(this::handleCreateUser);
        btnSeePassword.setOnAction(this::handleViewPassword);
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        hiddenField.setVisible(false);

        Image image = new Image("/com/tartanga/grupo4/resources/images/eyeopened.png");

        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setPreserveRatio(true);

        btnSeePassword.setMinSize(25, 25);
        btnSeePassword.setMaxSize(25, 25);

        btnSeePassword.setGraphic(imageView);

        btnSeePassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");

        stage.setTitle("SignIn");
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    private void handleCreateUser(ActionEvent event) {
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankSignUpView.fxml"));

            Parent root = (Parent) FXMLLoader.load();

            RovoBankSignUpController controller = (RovoBankSignUpController) FXMLLoader.getController();
            controller.setStage(stage);
            controller.initStage(root);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Something went wrong when loading the window.", e.getMessage());
        }
    }

    @FXML
    private void handleViewPassword(ActionEvent event) {
        isOn = !isOn;

        if (isOn) {

            String password = passwordField.getText();
            passwordField.setVisible(false);
            hiddenField.setVisible(true);
            hiddenField.setText(password);
            passwordField.setText(password);

            //I need to reload the image, else I cannot make it work.
            Image image = new Image("/com/tartanga/grupo4/resources/images/eyeclosed.png");

            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            imageView.setPreserveRatio(true);

            btnSeePassword.setMinSize(25, 25);
            btnSeePassword.setMaxSize(25, 25);

            btnSeePassword.setGraphic(imageView);

            btnSeePassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");
        } else {

            hiddenField.setVisible(false);
            passwordField.setText(hiddenField.getText());
            passwordField.setVisible(true);

            Image image = new Image("/com/tartanga/grupo4/resources/images/eyeopened.png");

            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            imageView.setPreserveRatio(true);

            btnSeePassword.setMinSize(25, 25);
            btnSeePassword.setMaxSize(25, 25);

            btnSeePassword.setGraphic(imageView);

            btnSeePassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setHeaderText(null);
        ButtonType oKButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(oKButton);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();

        if (hiddenField.isVisible()) {
            passwordField.setText(hiddenField.getText());
        }

        if (userField.getText().equals("") && passwordField.getText().equals("")) {
            alert.setTitle("Empty user fields");
            alert.setContentText("Please fill up the required fields.");
            alert.showAndWait();
        } else if (!userField.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            alert.setTitle("Invalid email address");
            alert.setContentText("Please enter a valid email address.");
            alert.showAndWait();
        } else if (!passwordField.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,}$")) {
            alert.setTitle("Invalid Password");
            alert.setContentText("The password must be at least 6 characters long and contain a capital letter and a number.");
            alert.showAndWait();
        } else {
            try {
                admin = AdminClientFactory.adminLogic().findAdminByCredentials(new GenericType<Admin>() {
                }, userField.getText(), passwordField.getText());
                FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankMainView.fxml"));

                Parent root = (Parent) FXMLLoader.load();

                RovoBankMainController controller = (RovoBankMainController) FXMLLoader.getController();
                controller.setStage(stage);
                controller.initStage(root);
            } catch (NotAuthorizedException e) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect User/Password.");
                alert.showAndWait();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Something went wrong when loading the window.", e.getMessage());
            }
        }
    }

}
