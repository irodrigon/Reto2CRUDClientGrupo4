/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rabio
 */
public class MenuBarController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger("javaClient");
    @FXML
    private Menu menuPrint;

    @FXML
    private MenuBar menuBar;

    /**
     * Initializes the controller class.
     *
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuPrint.setVisible(false);
    }

    @FXML
    public void handleAccountView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/AccountView.fxml"));

            Parent root = (Parent) loader.load();

            AccountController controller = (AccountController) loader.getController();
            controller.initStage(root);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading Accounts window");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading Accounts: {}", error.getMessage());
        }

    }

    @FXML
    public void handleMainView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankMainView.fxml"));

            Parent root = (Parent) loader.load();

            RovoBankMainController controller = (RovoBankMainController) loader.getController();
            controller.initStage(root);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading the main window");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController:An error happen when loading the main view: {}", error.getMessage());
        }

    }

    @FXML
    public void handleCreditCardView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankCreditCardView.fxml"));

            Parent root = (Parent) loader.load();

            RovoBankCreditCardController controller = (RovoBankCreditCardController) loader.getController();
            controller.initStage(root);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading credit cards window");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading credit card view: {}", error.getMessage());
        }

    }

    @FXML
    public void handleTransferView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/Transfer.fxml"));

            Parent root = (Parent) loader.load();

            TransferController controller = (TransferController) loader.getController();
            controller.initStage(root);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading transfer window");
            alertE.showAndWait();
            error.printStackTrace();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading transfer view: {0}", error.getMessage());
        }

    }

    @FXML
    public void handleLoanView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankLoanView.fxml"));

            Parent root = (Parent) loader.load();

            LoanController controller = (LoanController) loader.getController();
            controller.initStage(root);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading the loans window");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading loan view: {}", error.getMessage());
        }

    }

    @FXML
    public void handleLogoutView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankSignInView.fxml"));

            Parent root = (Parent) loader.load();

            RovoBankSignInController controller = (RovoBankSignInController) loader.getController();
            controller.initStage(root);

            AdminManager.getInstance().setAdmin(null);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen while loging out");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading SignIn view: {}", error.getMessage());
        }

    }

    @FXML
    public void handleProfileView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankMyProfileView.fxml"));

            Parent root = (Parent) loader.load();

            RovoBankMyProfileController controller = (RovoBankMyProfileController) loader.getController();

            controller.initStage(root);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading your profile window");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading MyProfile view: {}", error.getMessage());
        }

    }


    @FXML
    public void handleHelpView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/AccountHelp.fxml"));

            Parent root = (Parent) loader.load();

            HelpController controller = (HelpController) loader.getController();
            controller.initStage(root);
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading the help window");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading help view: {}", error.getMessage());
        }

    }
    
            @FXML
    public void handleCustomerView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankCustomersView.fxml"));

            Parent root = (Parent) loader.load();

            RovoBankCustomersController controller = (RovoBankCustomersController) loader.getController();

            controller.initStage(root);

            Stage currentStage = (Stage) menuBar.getScene().getWindow();
            currentStage.close();
        } catch (Exception error) {
            Alert alertE = new Alert(Alert.AlertType.ERROR, "An error happen when loading your profile window");
            alertE.showAndWait();
            LOGGER.log(Level.INFO, "MenuBarController: An error happen when loading MyProfile view: {}", error.getMessage());
        }

    }

    @FXML
    private void handleViewAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ButtonType CloseButton = new ButtonType("Close");
        alert.getButtonTypes().setAll(CloseButton);
        alert.setHeaderText("About");
        alert.setContentText("Authors: Aitor Barrio Pinos, Alin Marian Dadacu, Aratz Eguren Zarraga, Inigo Rodriguez Gonzalo\n\n\n"
                + "                           Workplace: Tartanga, Erandio");
        alert.showAndWait();
    }

}
