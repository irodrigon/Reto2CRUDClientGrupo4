
package com.tartanga.grupo4.main;


import com.tartanga.grupo4.controller.RovoBankMainController;
import com.tartanga.grupo4.controller.RovoBankSignInController;
import com.tartanga.grupo4.controller.TransferController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainApplicationRovoBank extends Application {

    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/Transfer.fxml"));
        Parent root = (Parent)loader.load();
        
        TransferController controller = (TransferController)loader.getController();

        controller.initStage(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}