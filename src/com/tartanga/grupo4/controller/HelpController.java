/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author rabio
 */
public class HelpController {
    
    @FXML
    private WebView webView;
    
    public void initStage(Parent root){
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Help");
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleHelpWindow);
        stage.show();
    }
    
        private void handleHelpWindow(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        //Load help page.
        webEngine.load(getClass()
                .getResource("/com/tartanga/grupo4/views/AccountHelp.html").toExternalForm());
    }
}
