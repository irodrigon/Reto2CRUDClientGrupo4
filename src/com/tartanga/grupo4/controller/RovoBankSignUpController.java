/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.AdminClientFactory;
import com.tartanga.grupo4.exception.ReadException;
import com.tartanga.grupo4.models.Admin;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.UrlBase64;
import security.Hash;

/**
 *
 * @author Iñi
 */
public class RovoBankSignUpController {

       static {
        //Poner BouncyCastle como provider
        Security.addProvider(new BouncyCastleProvider());
    }
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

    @FXML
    private Button btn_register;

    @FXML
    private TextField fld_Surname;

    private Admin admin;

    public Stage getStage() {
        return stage;
    }
    private Hash security = new Hash();
    private static final Logger LOGGER = Logger.getLogger("javaClient");
    @FXML
    private void initialize() {
        btn_Back.setOnAction(this::handleGoBack);
        btn_Register.setOnAction(this::handleRegister);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankSignInView.fxml"));
            Parent root = (Parent) loader.load();

            RovoBankSignInController controller = (RovoBankSignInController) loader.getController();
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

    private void handleRegister(ActionEvent event) {
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        ButtonType CloseButton = new ButtonType("Close");
        alert2.getButtonTypes().setAll(CloseButton);
        Stage alertStage = (Stage) alert2.getDialogPane().getScene().getWindow();

        String email = fld_Email.getText();

        if (hiddenFieldPassword.isVisible()) {
            fld_Password.setText(hiddenFieldPassword.getText());
        }

        if (hiddenFieldConfirm.isVisible()) {
            fld_Confirm.setText(hiddenFieldConfirm.getText());
        }

        String password = fld_Password.getText();
        String confirm = fld_Confirm.getText();
        String name = fld_Name.getText();
        String city = fld_City.getText();
        String street = fld_Street.getText();
        String zip = fld_Zip.getText();
        boolean isActive = chb_Active.isSelected();
        boolean hasError = false;

        // Validate email
     /*   if (email.isEmpty()) {
            lbl_error_Email.setText("Email is required.");
            hasError = true;
        } else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            lbl_error_Email.setText("Please enter a valid email address.");
            hasError = true;
        } else {
            lbl_error_Email.setText("");
        }

        // Validate password
        if (password.isEmpty()) {
            lbl_error_Password.setText("Password is required.");
            hasError = true;
        } else if (!password.matches("^.{6,}$")) {
            lbl_error_Password.setText("Password must be at least 6 characters long.");
            hasError = true;
        } else if (!password.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*")) {
            lbl_error_Password.setText("Password must include at least one uppercase letter, one lowercase letter, and one number.");
            hasError = true;
        } else {
            lbl_error_Password.setText("");
        }

        // Validate password confirmation
        if (confirm.isEmpty()) {
            lbl_error_Confirm.setText("Password confirmation is required.");
            hasError = true;
        } else if (!password.equals(confirm)) {
            lbl_error_Confirm.setText("Passwords don’t match.");
            hasError = true;
        } else {
            lbl_error_Confirm.setText("");
        }*/

        // Validate name
        if (name.isEmpty()) {
            lbl_error_Name.setText("Name is required.");
            hasError = true;
        } else if (name.matches(".*\\d.*")) {
            lbl_error_Name.setText("Name cannot contain numbers.");
            hasError = true;
        } else {
            lbl_error_Name.setText("");
        }

        // Validate city
        if (city.isEmpty()) {
            lbl_error_City.setText("City is required.");
            hasError = true;
        } else if (city.matches(".*\\d.*")) {
            lbl_error_City.setText("City cannot contain numbers.");
            hasError = true;
        } else {
            lbl_error_City.setText("");
        }

        // Validate street
        if (street.isEmpty()) {
            lbl_error_Street.setText("Street is required.");
            hasError = true;
        } else if (street.matches(".*\\d.*")) {
            lbl_error_Street.setText("Street cannot contain numbers.");
            hasError = true;
        } else {
            lbl_error_Street.setText("");
        }

        // Validate ZIP code
        if (zip.isEmpty()) {
            lbl_error_Zip.setText("ZIP code is required.");
            hasError = true;
        } else if (!zip.matches("\\d{1,10}")) {
            lbl_error_Zip.setText("Invalid ZIP code.");
            hasError = true;
        } else {
            lbl_error_Zip.setText("");
        }
        
        
        
        // Proceed with registration if no errors
        if (!hasError) {
            
            admin = new Admin();
            admin.setLogIn(fld_Email.getText());
            admin.setPassword(encriptar());
            admin.setName(fld_Name.getText());
            admin.setSurname(fld_Surname.getText());
            admin.setCity(fld_City.getText());
            admin.setStreet(fld_Street.getText());
            admin.setZip(Integer.parseInt(fld_Zip.getText()));
            if (chb_Active.isSelected()) {
                admin.setActive(true);
            } else {
                admin.setActive(false);
            }

            try {
                admin = AdminClientFactory.adminLogic().countAdminByLogIn(new GenericType<Admin>() {
                }, fld_Email.getText());

                alert2.setTitle("User already exists");
                alert2.setHeaderText("User has been already created.");
                alert2.setContentText("Try again with another user.");
                alert2.showAndWait();
            } catch (InternalServerErrorException e) {
                AdminClientFactory.adminLogic().createAdmin(admin);
                alert2.setTitle("Successful");
                alert2.setHeaderText("User created successfully.");
                alert2.setContentText("Go back to sign in to your account.");
                alert2.showAndWait();
                clearFields();
               } catch (Exception error) { 
                   error.printStackTrace();
//                logger.info("User created correctly.");
            }

        }
    }

    /**
     * Displays an alert with the specified title, header, and content.
     *
     * @param title The title of the alert.
     * @param header The header message of the alert.
     * @param content The content message of the alert.
     */
    private void alert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Image icon = new Image("/com/tartanga/grupo4/resources/images/servericon.png");
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(32);
        iconView.setFitHeight(32);
        ButtonType CloseButton = new ButtonType("Close");
        alert.getButtonTypes().setAll(CloseButton);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(icon);
        alert.showAndWait();
    }

    /**
     * Clears all input fields in the Sign-Up form.
     */
    private void clearFields() {
        fld_Email.clear();
        fld_Name.clear();
        fld_City.clear();
        fld_Street.clear();
        fld_Zip.clear();
        fld_Password.clear();
        hiddenFieldPassword.clear();
        hiddenFieldConfirm.clear();
        fld_Confirm.clear();
        chb_Active.setSelected(false);
        lbl_error_Email.setText("");
        lbl_error_Name.setText("");
        lbl_error_City.setText("");
        lbl_error_Street.setText("");
        lbl_error_Zip.setText("");
        lbl_error_Password.setText("");
        lbl_error_Confirm.setText("");
    }
    
    public String encriptar() {
        String encryptedPass64=null;
        try {
            //Recuperar la llave del fichero
            InputStream input = RovoBankSignInController.class.getClassLoader().getResourceAsStream("security/Public.key");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            input.close();

            byte[] publicKeyBytes = buffer.toByteArray();

            //Reconstruir la llave Publica
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
            PublicKey publicKey = keyFactory.generatePublic(spec);

            //Encriptar password con llave publica
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedPass = cipher.doFinal(fld_Password.getText().getBytes());

            //Convertirlo a String usando BASE64
            encryptedPass64 = new String(UrlBase64.encode(encryptedPass));
        } catch (Exception error) {
            logger.log(Level.SEVERE, "RovoBankSignInController: Exception while encripting the password: ", error.getMessage());
        }

        return encryptedPass64;
    }

}
