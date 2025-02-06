/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.CreditCardClientFactory;
import com.tartanga.grupo4.models.CreditCard;
import com.tartanga.grupo4.models.Movement;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iñi
 */
public class RovoBankMovementController {

    private static Logger logger;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    private Button btnGoBacktoCreditCards;
    
    @FXML
    private ImageView viewLogout;

    @FXML
    private void initialize() {
        btnGoBacktoCreditCards.setOnAction(this::handleGoBackToCreditCardsView);
        
    }

    @FXML
    private TableView<Movement> tableViewMovements;

    @FXML
    private TableColumn<Movement, Long> columnIdMovement;
    @FXML
    private TableColumn<Movement, Date> columnDate;
    @FXML
    private TableColumn<Movement, Double> columnAmount;
    @FXML
    private TableColumn<Movement, String> columnCurrency;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("Movements");
        stage.setResizable(false);

        String creditCardNumber = (String) stage.getUserData();

        List<CreditCard> cs = CreditCardClientFactory.creditCardLogic().findCreditCardByCardNumber(new GenericType<List<CreditCard>>() {
        }, creditCardNumber);

        List<Movement> movementList = new ArrayList<>();

        for (CreditCard c : cs) {
            for (Movement m : c.getMovementList()) {
                if (creditCardNumber.equals(String.valueOf(c.getCreditCardNumber()))) {
                    Movement movement = new Movement();
                    movement.setIDMovement(m.getIDMovement());
                    movement.setTransactionDate(m.getTransactionDate());
                    movement.setAmount(m.getAmount());
                    movement.setCurrency(m.getCurrency());
                }
                movementList.add(m);
            }
        }

        columnIdMovement.setCellValueFactory(new PropertyValueFactory<>("IDMovement"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnCurrency.setCellValueFactory(new PropertyValueFactory<>("currency"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        columnDate.setCellFactory(column -> new TableCell<Movement, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Convert Date to LocalDate
                    LocalDate localDate = item.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    setText(localDate.format(formatter));
                }
            }
        });

        ObservableList<Movement> datalist = FXCollections.observableArrayList(movementList);

        tableViewMovements.setItems(datalist);

        stage.show();
    }

    public void handleGoBackToCreditCardsView(Event event) {
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
    
    @FXML
    private void handleLogout(Event event
    ) {
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
}
