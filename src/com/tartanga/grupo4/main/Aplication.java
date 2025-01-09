/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.main;

import com.tartanga.grupo4.controller.AccountController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 *
 * @author rabio
 */
public class Aplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/AccountView.fxml"));

        Parent root = (Parent) loader.load();

        AccountController controller = (AccountController) loader.getController();
        controller.setStage(stage);
        controller.initStage(root);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
