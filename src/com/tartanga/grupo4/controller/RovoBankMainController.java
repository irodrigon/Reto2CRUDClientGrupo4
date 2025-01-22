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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
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
public class RovoBankMainController {

    private static Logger logger;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
    
    @FXML
    private MenuItem menuItemCredit;

    @FXML
    private void initialize() {
        menuItemCredit.setOnAction(this::handleCreditCard);
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

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
        }
    }

}
