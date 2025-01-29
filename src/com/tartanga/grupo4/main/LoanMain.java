package com.tartanga.grupo4.main;

import com.tartanga.grupo4.RESTfull.LoanRESTFull;
import com.tartanga.grupo4.businesslogic.LoanFactory;
import com.tartanga.grupo4.controller.LoanController;
import com.tartanga.grupo4.models.Loan;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 * Clase principal de la aplicación.
 */
public class LoanMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/com/tartanga/grupo4/views/RovoBankLoanView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        

    }

 
   
    
    /**
     * Método principal.
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
       
    }
}
