/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Iñi
 */
public class RovoBankSignUpController {
    
    private static Logger logger;
    
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Register;

    @FXML
    private TextField fld_Email;

    @FXML
    private TextField fld_Name;

    @FXML
    private TextField fld_City;

    @FXML
    private TextField fld_Street;
    
    @FXML
    private TextField fld_Zip;
    
    @FXML
    private PasswordField fld_Password;
    
    @FXML
    private PasswordField fld_Confirm;

    @FXML
    private CheckBox chb_Active;
    
    @FXML
    private Button btnSeePassword;

    @FXML
    private TextField hiddenFieldPassword;

    @FXML
    private Button btnSeeConfirm;

    @FXML
    private TextField hiddenFieldConfirm;

    private boolean isOn;

   
    private boolean isOnConfirm = false;

    
    @FXML
    private Label lbl_error_Email;

    
    @FXML
    private Label lbl_error_Password;

    @FXML
    private Label lbl_error_Confirm;

    @FXML
    private Label lbl_error_Name;

    @FXML
    private Label lbl_error_City;

    @FXML
    private Label lbl_error_Street;

  
    @FXML
    private Label lbl_error_Zip;
    
    public Stage getStage(){
        return stage;
    }
    
    @FXML
    private void initialize() {
        btn_Back.setOnAction(this::handleGoBack);
        //btn_Register.setOnAction(this::handleRegister);
        btnSeePassword.setOnAction(this::handleViewPassword);
        btnSeeConfirm.setOnAction(this::handleViewConfirm);
    }
    
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        hiddenFieldPassword.setVisible(false);
        hiddenFieldConfirm.setVisible(false);
        
        Image image = new Image("/com/tartanga/grupo4/resources/images/eyeopened.png");

        ImageView imageView = new ImageView(image);
        ImageView imageViewConfirm = new ImageView(image);

        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setPreserveRatio(true);

        imageViewConfirm.setFitWidth(25);
        imageViewConfirm.setFitHeight(25);
        imageViewConfirm.setPreserveRatio(true);

        btnSeePassword.setMinSize(25, 25);
        btnSeePassword.setMaxSize(25, 25);

        btnSeePassword.setGraphic(imageView);

        btnSeePassword.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");

        btnSeeConfirm.setMinSize(25, 25);
        btnSeeConfirm.setMaxSize(25, 25);

        btnSeeConfirm.setGraphic(imageViewConfirm);

        btnSeeConfirm.setStyle("-fx-background-color: transparent; -fx-border-color:transparent");
        
        stage.setTitle("SignUp");
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    private void handleGoBack(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankSignInView.fxml"));
            Parent root = (Parent)loader.load();
        
            RovoBankSignInController controller = (RovoBankSignInController)loader.getController();
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
}
