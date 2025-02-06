
package com.tartanga.grupo4.main;


import com.tartanga.grupo4.controller.RovoBankMainController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;


public class MainApplicationRovoBank extends Application {

    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankMainView.fxml"));
        Parent root = (Parent) loader.load();
        
        RovoBankMainController controller = (RovoBankMainController) loader.getController();
        controller.setStage(stage);
        controller.initStage(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
