/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.main;

import com.tartanga.grupo4.controller.LoanController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 *
 * @author egure
 */
public class LoanWindowMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankLoanView.fxml"));
        Parent root = (Parent) loader.load();
        LoanController controller = (LoanController) loader.getController();
        controller.setStage(stage);
        controller.initStage(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
