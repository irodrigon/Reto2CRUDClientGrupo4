/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.CreditCardClient;
import com.tartanga.grupo4.businesslogic.CreditCardClientFactory;
import com.tartanga.grupo4.businesslogic.CustomerClientFactory;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javax.xml.bind.annotation.XmlElement;
import com.tartanga.grupo4.models.CreditCardBean;
import com.tartanga.grupo4.models.CreditCard;
import com.tartanga.grupo4.models.Customer;
import com.tartanga.grupo4.models.Product;
import java.time.ZoneId;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author egure
 */
public class RovoBankCreditCardController {

    @FXML
    private Label label;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    private TableView<CreditCardBean> tableViewCreditCard;

    @FXML
    ComboBox<String> comboSelectDate;

    @FXML
    private TableColumn<CreditCardBean, String> columnOwner;
    @FXML
    private TableColumn<CreditCardBean, String> columnCreditNumber;
    @FXML
    private TableColumn<CreditCardBean, LocalDate> columnCreationDate;
    @FXML
    private TableColumn<CreditCardBean, LocalDate> columnExpDate;
    @FXML
    private TableColumn<CreditCardBean, String> columnCvv;
    @FXML
    private TableColumn<CreditCardBean, String> columnPin;
    @FXML
    private TableColumn<CreditCardBean, String> columnAccNumber;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnRemoveCard;

    @FXML
    private Button btnGotoMovements;

    @FXML
    private TextField txtFieldFind;

    @FXML
    private DatePicker dtPickerFrom;

    @FXML
    private DatePicker dtPickerTo;

    @FXML
    private Button btnShowAllData;

    @FXML
    private void initialize() {

        tableViewCreditCard.getSelectionModel().selectedItemProperty().addListener(this::handleActivateButtons);

        txtFieldFind.focusedProperty().addListener(this::handleFindFocus);

        dtPickerFrom.focusedProperty().addListener(this::handleFindFocus);

        dtPickerTo.focusedProperty().addListener(this::handleFindFocus);

        btnShowAllData.setOnAction(this::handleShowAllData);

    }

    @FXML
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Credit Cards");
        stage.setResizable(false);
        loadAllData();
        stage.show();

    }

    private void loadAllData() {
        
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        List<CreditCard> cs = CreditCardClientFactory.creditCardLogic().findAllCreditCards(new GenericType<List<CreditCard>>() {
        });

        List<Customer> lcus = CustomerClientFactory.customerLogic().findAllCustomers(new GenericType<List<Customer>>() {
        });

        List<CreditCardBean> csb = new ArrayList<>();

        for (Customer cus : lcus) {

            CreditCardBean cb = new CreditCardBean();

            cb.setOwner(cus.getDni());

            for (CreditCard c : cs) {

                if (cus.getProducts() != null) {

                    for (Product p : cus.getProducts()) {

                        if (p.getIDProduct().equals(c.getIDProduct())) {
                            cb.setCreditCardNumber(String.valueOf(c.getCreditCardNumber()));
                            cb.setCreationDate(c.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            cb.setExpirationDate(c.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            cb.setCvv(c.getCvv());
                            cb.setPin(c.getPin());
                            cb.setAccountNumber(c.getAccount().getAccountNumber());
                        }

                    }

                }
            }
            csb.add(cb);
        }

        columnOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        columnCreditNumber.setCellValueFactory(new PropertyValueFactory<>("creditCardNumber"));
        columnCvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));
        columnPin.setCellValueFactory(new PropertyValueFactory<>("pin"));
        columnCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        columnExpDate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        columnAccNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        ObservableList<CreditCardBean> dataList = FXCollections.observableArrayList(csb);

        //tableViewCreditCard.setEditable(true);
        tableViewCreditCard.setItems(dataList);

        //columnOwner.setCellValueFactory(cellData -> cellData.getValue().getOwner());
        //columnAccNumber.setCellValueFactory(cellData -> cellData.getValue().getAccountNumber());
        comboSelectDate.getItems().addAll("Creation Date", "Expiration Date");

        btnSearch.setDisable(true);

        btnRemoveCard.setDisable(true);

        btnGotoMovements.setDisable(true);
    }

    @FXML
    private void handleActivateButtons(ObservableValue observable, Object oldValue, Object newValue) {

        if (newValue != null) {
            btnRemoveCard.setDisable(false);
            btnGotoMovements.setDisable(false);

            btnRemoveCard.setOnAction(this::handleDeleteItem);
        }
    }

    private void handleFindFocus(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            updateButtonState(false);
        } else {
            updateButtonState(tableViewCreditCard.getSelectionModel().getSelectedItem() != null);
        }
    }

    private void updateButtonState(boolean enable) {
        btnRemoveCard.setDisable(!enable);

        btnRemoveCard.setOnAction(this::handleDeleteItem);

        btnGotoMovements.setDisable(!enable);
    }

    private void handleDeleteItem(ActionEvent event) {
        CreditCardClientFactory.creditCardLogic().deleteCreditCardByCardNumber(tableViewCreditCard.getSelectionModel().getSelectedItem().getCreditCardNumber());
        tableViewCreditCard.getItems().remove(tableViewCreditCard.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleShowAllData(ActionEvent event) {
        loadAllData();
        tableViewCreditCard.refresh();
    }

}
