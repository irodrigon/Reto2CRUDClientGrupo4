/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.AdminClientFactory;
import com.tartanga.grupo4.models.Admin;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public class RovoBankMyProfileController {

    private static Logger logger;

    private Stage stage;

    private Admin admin;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblName;

    @FXML
    private Label lblSurname;

    @FXML
    private Label lblCity;

    @FXML
    private Label lblStreet;

    @FXML
    private Label lblZip;

    @FXML
    private Label lblActive;

    @FXML
    private Button btnSeePassword;

    @FXML
    private Button btnSeeCurrentPassword;

    @FXML
    private Button btnSeeConfirm;

    @FXML
    private TextField hiddenFieldPassword;

    @FXML
    private TextField hiddenFieldConfirm;

    @FXML
    private TextField hiddenFieldCurrentPassword;

    @FXML
    private PasswordField fld_Password;

    @FXML
    private PasswordField fld_Confirm;

    @FXML
    private PasswordField fld_CurrentPassword;

    private boolean isOn;

    private boolean isOnConfirm;

    private boolean isOnCurrentPassword;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @FXML
    private void initialize() {
        btnSeeCurrentPassword.setOnAction(this::handleViewCurrentPassword);
        btnSeePassword.setOnAction(this::handleViewNewPassword);
        btnSeeConfirm.setOnAction(this::handleViewConfirm);

        Platform.runLater(() -> {
            fld_Password.getParent().requestFocus();
        });
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        hiddenFieldPassword.setVisible(false);
        hiddenFieldConfirm.setVisible(false);
        hiddenFieldCurrentPassword.setVisible(false);

        Image image = new Image("/com/tartanga/grupo4/resources/images/eyeopened.png");

        ImageView imageView = new ImageView(image);
        ImageView imageViewConfirm = new ImageView(image);
        ImageView imageViewCurrentPassword = new ImageView(image);

        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setPreserveRatio(true);

        imageViewConfirm.setFitWidth(25);
        imageViewConfirm.setFitHeight(25);
        imageViewConfirm.setPreserveRatio(true);

        imageViewCurrentPassword.setFitWidth(25);
        imageViewCurrentPassword.setFitHeight(25);
        imageViewCurrentPassword.setPreserveRatio(true);

        btnSeePassword.setMinSize(25, 25);
        btnSeePassword.setMaxSize(25, 25);

        btnSeePassword.setGraphic(imageView);

        btnSeePassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");

        btnSeeConfirm.setMinSize(25, 25);
        btnSeeConfirm.setMaxSize(25, 25);

        btnSeeConfirm.setGraphic(imageViewConfirm);

        btnSeeConfirm.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");

        btnSeeCurrentPassword.setMinSize(25, 25);
        btnSeeCurrentPassword.setMaxSize(25, 25);

        btnSeeCurrentPassword.setGraphic(imageViewCurrentPassword);

        btnSeeCurrentPassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");

        lblUsername.setText(admin.getLogIn());
        lblName.setText(admin.getName());
        lblSurname.setText(admin.getSurname());
        lblCity.setText(admin.getCity());
        lblStreet.setText(admin.getStreet());
        lblZip.setText(String.valueOf(admin.getZip()));
        lblActive.setText(admin.getActive() ? "yes" : "no");

        stage.setTitle("My profile");
        stage.setResizable(false);

        stage.show();

    }

    @FXML
    private void handleViewNewPassword(ActionEvent event) {
        isOn = !isOn;

        if (isOn) {

            String password = fld_Password.getText();
            fld_Password.setVisible(false);
            hiddenFieldPassword.setVisible(true);
            hiddenFieldPassword.setText(password);
            fld_Password.setText(password);

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

            hiddenFieldPassword.setVisible(false);
            fld_Password.setText(hiddenFieldPassword.getText());
            fld_Password.setVisible(true);

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
    private void handleViewConfirm(ActionEvent event) {
        isOnConfirm = !isOnConfirm;

        if (isOnConfirm) {
            String password = fld_Confirm.getText();
            fld_Confirm.setVisible(false);
            hiddenFieldConfirm.setVisible(true);
            hiddenFieldConfirm.setText(password);
            fld_Confirm.setText(password);

            Image image = new Image("/com/tartanga/grupo4/resources/images/eyeclosed.png");

            ImageView imageViewConfirm = new ImageView(image);

            imageViewConfirm.setFitWidth(25);
            imageViewConfirm.setFitHeight(25);
            imageViewConfirm.setPreserveRatio(true);

            btnSeeConfirm.setMinSize(25, 25);
            btnSeeConfirm.setMaxSize(25, 25);

            btnSeeConfirm.setGraphic(imageViewConfirm);

            btnSeeConfirm.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");
        } else {

            hiddenFieldConfirm.setVisible(false);
            fld_Confirm.setText(hiddenFieldConfirm.getText());
            fld_Confirm.setVisible(true);

            Image image = new Image("/com/tartanga/grupo4/resources/images/eyeopened.png");

            ImageView imageViewConfirm = new ImageView(image);

            imageViewConfirm.setFitWidth(25);
            imageViewConfirm.setFitHeight(25);
            imageViewConfirm.setPreserveRatio(true);

            btnSeeConfirm.setMinSize(25, 25);
            btnSeeConfirm.setMaxSize(25, 25);

            btnSeeConfirm.setGraphic(imageViewConfirm);

            btnSeeConfirm.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");
        }
    }

    @FXML
    private void handleViewCurrentPassword(ActionEvent event) {
        isOnCurrentPassword = !isOnCurrentPassword;

        if (isOnCurrentPassword) {
            String password = fld_CurrentPassword.getText();
            fld_CurrentPassword.setVisible(false);
            hiddenFieldCurrentPassword.setVisible(true);
            hiddenFieldCurrentPassword.setText(password);
            fld_CurrentPassword.setText(password);

            Image image = new Image("/com/tartanga/grupo4/resources/images/eyeclosed.png");

            ImageView imageViewCurrentPassword = new ImageView(image);

            imageViewCurrentPassword.setFitWidth(25);
            imageViewCurrentPassword.setFitHeight(25);
            imageViewCurrentPassword.setPreserveRatio(true);

            btnSeeCurrentPassword.setMinSize(25, 25);
            btnSeeCurrentPassword.setMaxSize(25, 25);

            btnSeeCurrentPassword.setGraphic(imageViewCurrentPassword);

            btnSeeCurrentPassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");
        } else {

            hiddenFieldCurrentPassword.setVisible(false);
            fld_CurrentPassword.setText(hiddenFieldCurrentPassword.getText());
            fld_CurrentPassword.setVisible(true);

            Image image = new Image("/com/tartanga/grupo4/resources/images/eyeopened.png");

            ImageView imageViewCurrentPassword = new ImageView(image);

            imageViewCurrentPassword.setFitWidth(25);
            imageViewCurrentPassword.setFitHeight(25);
            imageViewCurrentPassword.setPreserveRatio(true);

            btnSeeCurrentPassword.setMinSize(25, 25);
            btnSeeCurrentPassword.setMaxSize(25, 25);

            btnSeeCurrentPassword.setGraphic(imageViewCurrentPassword);

            btnSeeCurrentPassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");
        }
    }
}
