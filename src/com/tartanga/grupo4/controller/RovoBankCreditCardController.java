/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.AccountClientFactory;
import com.tartanga.grupo4.businesslogic.CreditCardClientFactory;
import com.tartanga.grupo4.models.Account;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import com.tartanga.grupo4.models.CreditCardBean;
import com.tartanga.grupo4.models.CreditCard;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Iñi
 */
public class RovoBankCreditCardController {

    @FXML
    private Label label;

    private Stage stage;

    private static Logger logger = Logger.getLogger(RovoBankCreditCardController.class.getName());

    ;

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
    private TableColumn<CreditCardBean, String> columnCreditNumber;
    @FXML
    private TableColumn<CreditCardBean, String> columnCreationDate;
    @FXML
    private TableColumn<CreditCardBean, String> columnExpDate;
    @FXML
    private TableColumn<CreditCardBean, String> columnCvv;
    @FXML
    private TableColumn<CreditCardBean, String> columnPin;
    @FXML
    private TableColumn<CreditCardBean, String> columnAccNumber;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnFind;

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
    private Button btnAddCard;

    @FXML
    private ImageView viewLogout;

    @FXML
    private MenuItem contextAdd;

    @FXML
    private MenuItem contextRemove;

    @FXML
    private MenuItem contextClear;

    @FXML
    private MenuItem menuItemPrint;

    @FXML
    private void initialize() {

        tableViewCreditCard.getSelectionModel().selectedItemProperty().addListener(this::handleActivateButtons);

        txtFieldFind.focusedProperty().addListener(this::handleFindFocus);

        dtPickerFrom.focusedProperty().addListener(this::handleFindFocus);

        dtPickerTo.focusedProperty().addListener(this::handleFindFocus);

        btnShowAllData.setOnAction(this::handleShowAllData);

        btnAddCard.setOnAction(this::handleAddTableRow);

        viewLogout.setOnMouseClicked(this::handleLogout);

        btnFind.setOnAction(this::handleFindCreditCardByCardNumber);

        comboSelectDate.valueProperty().addListener(this::handleActivateDatePickerFrom);

        contextAdd.setOnAction(this::handleAddTableRow);

        contextClear.setOnAction(this::handleClearTable);

        creditCardEditingLogic();

        menuItemPrint.setOnAction(this::handlePrintCreditCardReport);
    }

    @FXML
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Credit Cards");
        stage.setResizable(false);

        stage.setOnCloseRequest(this::onCloseRequestWindowEvent);

        comboSelectDate.getItems().addAll("Creation Date", "Expiration Date");

        loadAllData();

        stage.show();

    }

    private void loadAllData() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            List<CreditCard> cs = CreditCardClientFactory.creditCardLogic().findAllCreditCards(new GenericType<List<CreditCard>>() {
            });

            List<CreditCardBean> csb = new ArrayList<>();

            for (CreditCard c : cs) {

                CreditCardBean cb = new CreditCardBean();

                cb.setiDProduct(c.getIDProduct());
                cb.setCreditCardNumber(String.valueOf(c.getCreditCardNumber()));
                cb.setCreationDate(c.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                if (c.getExpirationDate() != null) {
                    cb.setExpirationDate(c.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                cb.setCvv(c.getCvv());
                cb.setPin(c.getPin());
                if (c.getAccount() != null && c.getAccount().getAccountNumber() != null) {
                    cb.setAccountNumber(c.getAccount().getAccountNumber());
                }
                csb.add(cb);

            }

            columnCreditNumber.setCellValueFactory(new PropertyValueFactory<>("creditCardNumber"));
            columnCvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));
            columnPin.setCellValueFactory(new PropertyValueFactory<>("pin"));
            columnCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
            columnExpDate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
            columnAccNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

            columnCreditNumber.setCellFactory(TextFieldTableCell.forTableColumn());
            columnCreationDate.setCellFactory(TextFieldTableCell.forTableColumn());
            columnExpDate.setCellFactory(TextFieldTableCell.forTableColumn());
            columnCvv.setCellFactory(TextFieldTableCell.forTableColumn());
            columnPin.setCellFactory(TextFieldTableCell.forTableColumn());

            List<String> accountNumberList = new ArrayList();

            List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
            });

            for (Account a : accountList) {
                accountNumberList.add(a.getAccountNumber());
            }

            ObservableList<String> accountChoices = FXCollections.observableArrayList(accountNumberList);

            columnAccNumber.setCellFactory(ChoiceBoxTableCell.forTableColumn(accountChoices));

            ObservableList<CreditCardBean> dataList = FXCollections.observableArrayList(csb);

            tableViewCreditCard.setEditable(true);
            tableViewCreditCard.setItems(dataList);

        } catch (ProcessingException e) {
            logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            ButtonType CloseButton = new ButtonType("Close");
            alert.getButtonTypes().setAll(CloseButton);
            alert.setHeaderText("Server Error.");
            alert.setContentText("An error Ocurred. Contact you system administrator");
            alert.showAndWait();
        }

        dtPickerFrom.setDisable(true);

        dtPickerTo.setDisable(true);

        btnSearch.setDisable(true);

        btnRemoveCard.setDisable(true);

        contextRemove.setDisable(true);

        btnGotoMovements.setDisable(true);

        comboSelectDate.setValue("Select creation/expiration date");
    }

    @FXML
    private void handleActivateButtons(ObservableValue observable, Object oldValue, Object newValue) {

        if (newValue != null) {
            btnRemoveCard.setDisable(false);
            btnGotoMovements.setDisable(false);

            contextRemove.setDisable(false);

            btnRemoveCard.setOnAction(this::handleDeleteItem);

            contextRemove.setOnAction(this::handleDeleteItem);

            btnGotoMovements.setOnAction(this::handleShowMovements);
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

        contextRemove.setDisable(!enable);

        btnRemoveCard.setOnAction(this::handleDeleteItem);

        contextRemove.setOnAction(this::handleDeleteItem);

        btnGotoMovements.setDisable(!enable);

        btnGotoMovements.setOnAction(this::handleShowMovements);
    }

    private void handleDeleteItem(ActionEvent event) {
        Alert alert = alertYesNo("User confirmation", "Confirm removing credit card:", "Are you sure you want to remove the following credit card: " + tableViewCreditCard.getSelectionModel().getSelectedItem().getCreditCardNumber() + "?");
        if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
            try {
                CreditCardClientFactory.creditCardLogic().deleteCreditCardByCardNumber(tableViewCreditCard.getSelectionModel().getSelectedItem().getCreditCardNumber());
                tableViewCreditCard.getItems().remove(tableViewCreditCard.getSelectionModel().getSelectedItem());
                loadAllData();
                tableViewCreditCard.refresh();
            } catch (ProcessingException e) {
                logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
                alertError("Server Error", "An error occurred", "Contact your system administrator");
            }
        } else {
            loadAllData();
            tableViewCreditCard.refresh();
        }
    }

    @FXML
    private void handleShowAllData(ActionEvent event
    ) {
        loadAllData();
        tableViewCreditCard.refresh();
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

    @FXML
    private void handleAddTableRow(ActionEvent event
    ) {
        //It is going to add a random credit card number, pin, cvv and the first account it finds in the database.
        Random random = new Random();
        StringBuilder sbccn = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sbccn.append(random.nextInt(10));  // Generates a random digit between 0 and 9
        }

        String random16DigitNumber = sbccn.toString();

        StringBuilder sbcvv = new StringBuilder(3);

        for (int i = 0; i < 3; i++) {
            sbcvv.append(random.nextInt(10));
        }

        String random3DigitNumber = sbcvv.toString();

        StringBuilder sbpin = new StringBuilder(3);

        for (int i = 0; i < 4; i++) {
            sbpin.append(random.nextInt(10));
        }

        String random4DigitNumber = sbpin.toString();

        CreditCardBean creditCardBean = new CreditCardBean("", "", "", "");
        tableViewCreditCard.getItems().add(creditCardBean);
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber(Long.parseLong(random16DigitNumber));

        LocalDate localDate = LocalDate.now().plusYears(5);
        // Convert LocalDate to Instant
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        // Convert Instant to Date
        Date expirationDate = Date.from(instant);

        creditCard.setExpirationDate(expirationDate);

        creditCard.setCvv(random3DigitNumber);

        creditCard.setPin(random4DigitNumber);

        try {

            List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
            });

            creditCard.setAccount(accountList.get(0));

            CreditCardClientFactory.creditCardLogic().createCreditCard(creditCard);

        } catch (ProcessingException e) {
            logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            ButtonType CloseButton = new ButtonType("Close");
            alert.getButtonTypes().setAll(CloseButton);
            alert.setHeaderText("Server Error.");
            alert.setContentText("An error Ocurred. Contact you system administrator");
            alert.showAndWait();
        }

        loadAllData();
    }

    private void creditCardEditingLogic() {

        columnCreditNumber.setOnEditCommit(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    String newValue = t.getNewValue();
                    CreditCardBean creditCardBean = t.getRowValue();
                    if (!newValue.matches("\\d{16}")) {
                        alertError("Error inserting credit card number", "Wrong credit card number", "Credit card numbers are made of 16 digits.");

                        // Revert to the old value if the new value is invalid.
                        creditCardBean.setCreditCardNumber((String) t.getOldValue());
                        //This is deferring the event of the placing of the cursor inside the cell when the alert is closed.
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCreditNumber);
                        });
                    } else {
                        Alert alert = alertYesNo("User confirmation", "Confirm editing credit card number:", "Are you sure you want to edit following credit card number: " + newValue + "?");
                        if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                            ((CreditCardBean) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())).setCreditCardNumber(newValue);

                            CreditCard creditCard = new CreditCard();

                            creditCard.setIDProduct(creditCardBean.getiDProduct());
                            creditCard.setCreditCardNumber(Long.parseLong(newValue));

                            LocalDate updateCreationDate = LocalDate.parse(creditCardBean.getCreationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setCreationDate(Date.from(updateCreationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            LocalDate updateExpirationDate = LocalDate.parse(creditCardBean.getExpirationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setExpirationDate(Date.from(updateExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            creditCard.setCvv(creditCardBean.getCvv());
                            creditCard.setPin(creditCardBean.getPin());

                            try {
                                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                                });

                                for (Account a : accountList) {
                                    if (creditCardBean.getAccountNumber().equals(a.getAccountNumber())) {
                                        creditCard.setAccount(a);
                                    }
                                }

                                CreditCardClientFactory.creditCardLogic().updateCreditCard(creditCard, String.valueOf(creditCardBean.getiDProduct()));

                            } catch (ProcessingException e) {
                                logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
                                alertError("Server Error", "An error occurred", "Contact your system administrator");
                            }
                            loadAllData();
                            tableViewCreditCard.refresh();
                        } else {
                            //If I press I don't want to confirm editing the card number, I continue editing.
                            Platform.runLater(() -> {
                                tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCreditNumber);
                            });
                        }
                    }
                }
        );
        columnCreditNumber.setOnEditCancel(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    Alert alert = alertYesNo("User confirmation", "Cancel editing credit card number:", "Are you sure you want to cancel editing credit card number?");
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                        loadAllData();
                    } else {
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCreditNumber);
                        });
                    }
                }
        );

        columnCreationDate.setOnEditCommit(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    String newValue = t.getNewValue();
                    CreditCardBean creditCardBean = t.getRowValue();
                    if (!newValue.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$")) {
                        alertError("Error inserting creation date", "Wrong creation date", "Please insert a valid date in format dd/MM/yyyy");

                        // Revert to the old value if the new value is invalid.
                        creditCardBean.setCreationDate(LocalDate.parse((String) t.getOldValue(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        //This is deferring the event of the placing of the cursor inside the cell when the alert is closed.
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCreationDate);
                        });
                    } else {
                        Alert alert = alertYesNo("User confirmation", "Confirm editing creation date:", "Are you sure you want to edit following creation date: " + newValue + "?");
                        if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                            ((CreditCardBean) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())).setCreationDate(LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                            CreditCard creditCard = new CreditCard();

                            creditCard.setIDProduct(creditCardBean.getiDProduct());

                            creditCard.setCreditCardNumber(Long.parseLong(creditCardBean.getCreditCardNumber()));

                            LocalDate updateCreationDate = LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setCreationDate(Date.from(updateCreationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            LocalDate updateExpirationDate = LocalDate.parse(creditCardBean.getExpirationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setExpirationDate(Date.from(updateExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            creditCard.setCvv(creditCardBean.getCvv());
                            creditCard.setPin(creditCardBean.getPin());
                            try {
                                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                                });

                                for (Account a : accountList) {
                                    if (creditCardBean.getAccountNumber().equals(a.getAccountNumber())) {
                                        creditCard.setAccount(a);
                                    }
                                }

                                CreditCardClientFactory.creditCardLogic().updateCreditCard(creditCard, String.valueOf(creditCardBean.getiDProduct()));
                            } catch (ProcessingException e) {
                                logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
                                alertError("Server Error", "An error occurred", "Contact your system administrator");
                            }
                            loadAllData();
                            tableViewCreditCard.refresh();
                        } else {
                            //If I press I don't want to confirm editing the card number, I continue editing.
                            Platform.runLater(() -> {
                                tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCreationDate);
                            });
                        }
                    }
                }
        );

        columnCreationDate.setOnEditCancel(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    Alert alert = alertYesNo("User confirmation", "Cancel editing creation date:", "Are you sure you want to cancel editing creation date?");
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                        loadAllData();
                    } else {
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCreationDate);
                        });
                    }
                }
        );

        columnExpDate.setOnEditCommit(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    String newValue = t.getNewValue();
                    CreditCardBean creditCardBean = t.getRowValue();
                    if (!newValue.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$")) {
                        alertError("Error inserting expiration date", "Wrong expiration date", "Please insert a valid date in format dd/MM/yyyy");

                        // Revert to the old value if the new value is invalid.
                        creditCardBean.setExpirationDate(LocalDate.parse((String) t.getOldValue(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        //This is deferring the event of the placing of the cursor inside the cell when the alert is closed.
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnExpDate);
                        });
                    } else {
                        Alert alert = alertYesNo("User confirmation", "Confirm editing expiration date:", "Are you sure you want to edit following expiration date: " + newValue + "?");
                        if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                            ((CreditCardBean) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())).setExpirationDate(LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                            CreditCard creditCard = new CreditCard();

                            creditCard.setIDProduct(creditCardBean.getiDProduct());

                            creditCard.setCreditCardNumber(Long.parseLong(creditCardBean.getCreditCardNumber()));

                            LocalDate updateCreationDate = LocalDate.parse(creditCardBean.getCreationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setCreationDate(Date.from(updateCreationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            LocalDate updateExpirationDate = LocalDate.parse(newValue, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setExpirationDate(Date.from(updateExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            creditCard.setCvv(creditCardBean.getCvv());
                            creditCard.setPin(creditCardBean.getPin());
                            try {
                                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                                });

                                for (Account a : accountList) {
                                    if (creditCardBean.getAccountNumber().equals(a.getAccountNumber())) {
                                        creditCard.setAccount(a);
                                    }
                                }

                                CreditCardClientFactory.creditCardLogic().updateCreditCard(creditCard, String.valueOf(creditCardBean.getiDProduct()));
                            } catch (ProcessingException e) {
                                logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
                                alertError("Server Error", "An error occurred", "Contact your system administrator");
                            }
                            loadAllData();
                            tableViewCreditCard.refresh();
                        } else {
                            //If I press I don't want to confirm editing the card number, I continue editing.
                            Platform.runLater(() -> {
                                tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnExpDate);
                            });
                        }
                    }
                }
        );

        columnExpDate.setOnEditCancel(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    Alert alert = alertYesNo("User confirmation", "Cancel editing expiration date:", "Are you sure you want to cancel editing expiration date?");
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                        loadAllData();
                    } else {
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnExpDate);
                        });
                    }
                }
        );

        columnCvv.setOnEditCommit(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    String newValue = t.getNewValue();
                    CreditCardBean creditCardBean = t.getRowValue();
                    if (!newValue.matches("\\d{3}")) {
                        alertError("Error inserting CVV", "Wrong CVV", "CVVs are made of 3 digits");

                        // Revert to the old value if the new value is invalid.
                        creditCardBean.setCvv((String) t.getOldValue());
                        //This is deferring the event of the placing of the cursor inside the cell when the alert is closed.
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCvv);
                        });
                    } else {
                        Alert alert = alertYesNo("User confirmation", "Confirm editing CVV:", "Are you sure you want to edit the following CVV: " + t.getNewValue() + "?");
                        if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                            ((CreditCardBean) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())).setCvv(newValue);

                            CreditCard creditCard = new CreditCard();

                            creditCard.setIDProduct(creditCardBean.getiDProduct());
                            creditCard.setCreditCardNumber(Long.parseLong(creditCardBean.getCreditCardNumber()));

                            LocalDate updateCreationDate = LocalDate.parse(creditCardBean.getCreationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setCreationDate(Date.from(updateCreationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            LocalDate updateExpirationDate = LocalDate.parse(creditCardBean.getExpirationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setExpirationDate(Date.from(updateExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            creditCard.setCvv(newValue);
                            creditCard.setPin(creditCardBean.getPin());
                            try {
                                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                                });

                                for (Account a : accountList) {
                                    if (creditCardBean.getAccountNumber().equals(a.getAccountNumber())) {
                                        creditCard.setAccount(a);
                                    }
                                }

                                CreditCardClientFactory.creditCardLogic().updateCreditCard(creditCard, String.valueOf(creditCardBean.getiDProduct()));
                            } catch (ProcessingException e) {
                                logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
                                alertError("Server Error", "An error occurred", "Contact your system administrator");
                            }
                            loadAllData();
                            tableViewCreditCard.refresh();
                        } else {
                            //If I press I don't want to confirm editing the card number, I continue editing.
                            Platform.runLater(() -> {
                                tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCvv);
                            });
                        }
                    }
                }
        );
        columnCvv.setOnEditCancel(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    Alert alert = alertYesNo("User Confirmation", "Cancel editing CVV:", "Are you sure you want to cancel editing CVV?");
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                        loadAllData();
                    } else {
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnCvv);
                        });
                    }
                }
        );

        columnPin.setOnEditCommit(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    String newValue = t.getNewValue();
                    CreditCardBean creditCardBean = t.getRowValue();
                    if (!newValue.matches("\\d{4}")) {
                        alertError("Error inserting PIN", "Wrong PIN", "PINs are made of 4 digits");

                        // Revert to the old value if the new value is invalid.
                        creditCardBean.setPin((String) t.getOldValue());
                        //This is deferring the event of the placing of the cursor inside the cell when the alert is closed.
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnPin);
                        });
                    } else {
                        Alert alert = alertYesNo("User Confirmation", "Confirm editing PIN:", "Are you sure you want to edit the following PIN: " + newValue + "?");
                        if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {

                            ((CreditCardBean) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())).setPin(newValue);

                            CreditCard creditCard = new CreditCard();

                            creditCard.setIDProduct(creditCardBean.getiDProduct());
                            creditCard.setCreditCardNumber(Long.parseLong(creditCardBean.getCreditCardNumber()));

                            LocalDate updateCreationDate = LocalDate.parse(creditCardBean.getCreationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setCreationDate(Date.from(updateCreationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            LocalDate updateExpirationDate = LocalDate.parse(creditCardBean.getExpirationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            creditCard.setExpirationDate(Date.from(updateExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                            creditCard.setCvv(creditCardBean.getCvv());
                            creditCard.setPin(newValue);
                            try {
                                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                                });

                                for (Account a : accountList) {
                                    if (creditCardBean.getAccountNumber().equals(a.getAccountNumber())) {
                                        creditCard.setAccount(a);
                                    }
                                }

                                CreditCardClientFactory.creditCardLogic().updateCreditCard(creditCard, String.valueOf(creditCardBean.getiDProduct()));
                            } catch (ProcessingException e) {
                                logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
                                alertError("Server Error", "An error occurred", "Contact your system administrator");
                            }
                            loadAllData();
                            tableViewCreditCard.refresh();
                        } else {
                            //If I press I don't want to confirm editing the card number, I continue editing.
                            Platform.runLater(() -> {
                                tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnPin);
                            });
                        }
                    }
                }
        );

        columnPin.setOnEditCancel(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    Alert alert = alertYesNo("User Confirmation", "Cancel editing PIN:", "Are you Sure you want to cancel editing PIN?");
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                        loadAllData();
                    } else {
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnPin);
                        });
                    }
                }
        );

        columnAccNumber.setOnEditStart(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    Alert alert = alertYesNo("User confirmation", "Confirm changing the account number:", "Are you sure you want to change the account number?");
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                        tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnAccNumber);
                    } else {
                        loadAllData();
                    }
                }
        );

        columnAccNumber.setOnEditCommit(
                (CellEditEvent<CreditCardBean, String> t) -> {

                    Alert alert = alertYesNo("User confirmation", "Confirm editing account number:", "Are you sure you want to edit the following account number: " + t.getNewValue() + "?");
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {

                        CreditCardBean creditCardBean = t.getRowValue();

                        CreditCard creditCard = new CreditCard();

                        creditCard.setIDProduct(creditCardBean.getiDProduct());
                        creditCard.setCreditCardNumber(Long.parseLong(creditCardBean.getCreditCardNumber()));

                        LocalDate updateCreationDate = LocalDate.parse(creditCardBean.getCreationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        creditCard.setCreationDate(Date.from(updateCreationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                        LocalDate updateExpirationDate = LocalDate.parse(creditCardBean.getExpirationDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        creditCard.setExpirationDate(Date.from(updateExpirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                        creditCard.setCvv(creditCardBean.getCvv());
                        creditCard.setPin(creditCardBean.getPin());

                        ((CreditCardBean) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setAccountNumber(t.getNewValue());
                        try {
                            List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                            });

                            for (Account a : accountList) {
                                if (t.getNewValue().equals(a.getAccountNumber())) {
                                    creditCard.setAccount(a);
                                }
                            }

                            CreditCardClientFactory.creditCardLogic().updateCreditCard(creditCard, String.valueOf(creditCardBean.getiDProduct()));
                        } catch (ProcessingException e) {
                            logger.log(Level.SEVERE, "Error with the Server. Contact your system administrator.", e.getMessage());
                            alertError("Server Error", "An error occurred", "Contact your system administrator");
                        }
                        loadAllData();
                        tableViewCreditCard.refresh();
                    } else {
                        loadAllData();
                        tableViewCreditCard.refresh();
                    }

                }
        );

        columnAccNumber.setOnEditCancel(
                (CellEditEvent<CreditCardBean, String> t) -> {
                    Alert alert = alertYesNo("User confirmation", "Cancel editing account number", "Are you Sure you want to cancel editing account number?");
                    //This line is going to get the "yes" button with the '0' index from the alert in the method and confirm if the user is pressing 'yes' or 'no', then acting accordingly.
                    if (alert.resultProperty().get().equals(alert.getButtonTypes().get(0))) {
                        loadAllData();
                    } else {
                        Platform.runLater(() -> {
                            tableViewCreditCard.edit(tableViewCreditCard.getSelectionModel().getSelectedIndex(), columnAccNumber);
                        });
                    }
                }
        );
    }

    private void alertError(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        ButtonType CloseButton = new ButtonType("Close");
        alert.getButtonTypes().setAll(CloseButton);
        alert.showAndWait();
    }

    private Alert alertYesNo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait();

        return alert;
    }

    @FXML
    private void handleFindCreditCardByCardNumber(Event event
    ) {
        try {
            List<CreditCard> creditCardsfiltered = CreditCardClientFactory.creditCardLogic().findCreditCardByCardNumber(new GenericType<List<CreditCard>>() {
            }, txtFieldFind.getText());
            if (creditCardsfiltered.isEmpty()) {
                logger.log(Level.INFO, "Credit card number not found: ");

                alertError("Error loading data", "No credit card found", "No credit card found with entered value: " + txtFieldFind.getText());
                loadAllData();

                txtFieldFind.setText("");

            } else {

                List<CreditCardBean> csb = new ArrayList<>();

                for (CreditCard c : creditCardsfiltered) {

                    CreditCardBean cb = new CreditCardBean();

                    cb.setiDProduct(c.getIDProduct());
                    cb.setCreditCardNumber(String.valueOf(c.getCreditCardNumber()));
                    cb.setCreationDate(c.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    if (c.getExpirationDate() != null) {
                        cb.setExpirationDate(c.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                    cb.setCvv(c.getCvv());
                    cb.setPin(c.getPin());
                    if (c.getAccount() != null && c.getAccount().getAccountNumber() != null) {
                        cb.setAccountNumber(c.getAccount().getAccountNumber());
                    }
                    csb.add(cb);

                }

                columnCreditNumber.setCellValueFactory(new PropertyValueFactory<>("creditCardNumber"));
                columnCvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));
                columnPin.setCellValueFactory(new PropertyValueFactory<>("pin"));
                columnCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
                columnExpDate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
                columnAccNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

                columnCreditNumber.setCellFactory(TextFieldTableCell.forTableColumn());
                columnCreationDate.setCellFactory(TextFieldTableCell.forTableColumn());
                columnExpDate.setCellFactory(TextFieldTableCell.forTableColumn());
                columnCvv.setCellFactory(TextFieldTableCell.forTableColumn());
                columnPin.setCellFactory(TextFieldTableCell.forTableColumn());

                List<String> accountNumberList = new ArrayList();

                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                });

                for (Account a : accountList) {
                    accountNumberList.add(a.getAccountNumber());
                }

                ObservableList<String> accountChoices = FXCollections.observableArrayList(accountNumberList);

                columnAccNumber.setCellFactory(ChoiceBoxTableCell.forTableColumn(accountChoices));

                ObservableList<CreditCardBean> dataList = FXCollections.observableArrayList(csb);

                tableViewCreditCard.setEditable(true);

                tableViewCreditCard.setItems(dataList);

                btnSearch.setDisable(true);

                btnRemoveCard.setDisable(true);

                btnGotoMovements.setDisable(true);

                txtFieldFind.setText("");
            }
        } catch (NotFoundException e) {
            logger.log(Level.INFO, "Credit card number not found: ", e.getMessage());

            alertError("Error loading data", "No credit card found", "No credit card found with entered value: " + txtFieldFind.getText());
            loadAllData();

            txtFieldFind.setText("");
        }
    }

    @FXML
    private void handleActivateDatePickerFrom(ObservableValue observable, Object oldValue, Object newValue) {

        if (newValue.equals(comboSelectDate.getValue())) {

            dtPickerFrom.setDisable(false);

            dtPickerFrom.valueProperty().addListener(this::handleActivateDatePickerTo);

        }
    }

    @FXML
    private void handleActivateDatePickerTo(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue != null) {
            dtPickerTo.setDisable(false);

            dtPickerTo.valueProperty().addListener(this::handleActivateSearchButton);
        }
    }

    @FXML
    private void handleActivateSearchButton(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue != null) {

            btnSearch.setDisable(false);

            btnSearch.setOnAction(this::handleSearchByDatesLogic);
        }
    }

    @FXML
    private void handleSearchByDatesLogic(Event event) {
        if (comboSelectDate.getValue().equals("Creation Date")) {
            List<CreditCard> creditCardsfiltered = CreditCardClientFactory.creditCardLogic().findCreditCardByCreationDate(new GenericType<List<CreditCard>>() {
            }, String.valueOf(dtPickerFrom.getValue()), String.valueOf(dtPickerTo.getValue()));

            if (creditCardsfiltered.isEmpty()) {
                logger.log(Level.INFO, "Creation Dates not Found: ");

                alertError("Error loading data", "No creation dates found", "No creation date found with entered values: " + String.valueOf(dtPickerFrom.getValue()) + " " + String.valueOf(dtPickerTo.getValue()));
                loadAllData();

            } else {

                List<CreditCardBean> csb = new ArrayList<>();

                for (CreditCard c : creditCardsfiltered) {

                    CreditCardBean cb = new CreditCardBean();

                    cb.setiDProduct(c.getIDProduct());
                    cb.setCreditCardNumber(String.valueOf(c.getCreditCardNumber()));
                    cb.setCreationDate(c.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    if (c.getExpirationDate() != null) {
                        cb.setExpirationDate(c.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                    cb.setCvv(c.getCvv());
                    cb.setPin(c.getPin());
                    if (c.getAccount() != null && c.getAccount().getAccountNumber() != null) {
                        cb.setAccountNumber(c.getAccount().getAccountNumber());
                    }
                    csb.add(cb);

                }

                columnCreditNumber.setCellValueFactory(new PropertyValueFactory<>("creditCardNumber"));
                columnCvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));
                columnPin.setCellValueFactory(new PropertyValueFactory<>("pin"));
                columnCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
                columnExpDate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
                columnAccNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

                columnCreditNumber.setCellFactory(TextFieldTableCell.forTableColumn());
                columnCreationDate.setCellFactory(TextFieldTableCell.forTableColumn());
                columnExpDate.setCellFactory(TextFieldTableCell.forTableColumn());
                columnCvv.setCellFactory(TextFieldTableCell.forTableColumn());
                columnPin.setCellFactory(TextFieldTableCell.forTableColumn());

                List<String> accountNumberList = new ArrayList();

                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                });

                for (Account a : accountList) {
                    accountNumberList.add(a.getAccountNumber());
                }

                ObservableList<String> accountChoices = FXCollections.observableArrayList(accountNumberList);

                columnAccNumber.setCellFactory(ChoiceBoxTableCell.forTableColumn(accountChoices));

                ObservableList<CreditCardBean> dataList = FXCollections.observableArrayList(csb);

                tableViewCreditCard.setEditable(true);

                tableViewCreditCard.setItems(dataList);

                btnSearch.setDisable(true);

                btnRemoveCard.setDisable(true);

                btnGotoMovements.setDisable(true);

                dtPickerFrom.setValue(LocalDate.now());

                dtPickerTo.setValue(LocalDate.now());

                dtPickerFrom.setDisable(true);

                dtPickerTo.setDisable(true);

                btnSearch.setDisable(true);

                comboSelectDate.setValue("Select creation/expiration date");

            }

        } else if (comboSelectDate.getValue().equals("Expiration Date")) {
            List<CreditCard> creditCardsfiltered = CreditCardClientFactory.creditCardLogic().findCreditCardByExpirationDate(new GenericType<List<CreditCard>>() {
            }, String.valueOf(dtPickerFrom.getValue()), String.valueOf(dtPickerTo.getValue()));

            if (creditCardsfiltered.isEmpty()) {
                logger.log(Level.INFO, "Expiration Dates not Found: ");

                alertError("Error loading data", "No expiration dates found", "No expiration date found with entered values: " + String.valueOf(dtPickerFrom.getValue()) + " " + String.valueOf(dtPickerTo.getValue()));
                loadAllData();

            } else {

                List<CreditCardBean> csb = new ArrayList<>();

                for (CreditCard c : creditCardsfiltered) {

                    CreditCardBean cb = new CreditCardBean();

                    cb.setiDProduct(c.getIDProduct());
                    cb.setCreditCardNumber(String.valueOf(c.getCreditCardNumber()));
                    cb.setCreationDate(c.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    if (c.getExpirationDate() != null) {
                        cb.setExpirationDate(c.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                    cb.setCvv(c.getCvv());
                    cb.setPin(c.getPin());
                    if (c.getAccount() != null && c.getAccount().getAccountNumber() != null) {
                        cb.setAccountNumber(c.getAccount().getAccountNumber());
                    }
                    csb.add(cb);

                }

                columnCreditNumber.setCellValueFactory(new PropertyValueFactory<>("creditCardNumber"));
                columnCvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));
                columnPin.setCellValueFactory(new PropertyValueFactory<>("pin"));
                columnCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
                columnExpDate.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
                columnAccNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

                columnCreditNumber.setCellFactory(TextFieldTableCell.forTableColumn());
                columnCreationDate.setCellFactory(TextFieldTableCell.forTableColumn());
                columnExpDate.setCellFactory(TextFieldTableCell.forTableColumn());
                columnCvv.setCellFactory(TextFieldTableCell.forTableColumn());
                columnPin.setCellFactory(TextFieldTableCell.forTableColumn());

                List<String> accountNumberList = new ArrayList();

                List<Account> accountList = AccountClientFactory.accountLogic().findAllAccounts(new GenericType<List<Account>>() {
                });

                for (Account a : accountList) {
                    accountNumberList.add(a.getAccountNumber());
                }

                ObservableList<String> accountChoices = FXCollections.observableArrayList(accountNumberList);

                columnAccNumber.setCellFactory(ChoiceBoxTableCell.forTableColumn(accountChoices));

                ObservableList<CreditCardBean> dataList = FXCollections.observableArrayList(csb);

                tableViewCreditCard.setEditable(true);

                tableViewCreditCard.setItems(dataList);

                btnSearch.setDisable(true);

                btnRemoveCard.setDisable(true);

                btnGotoMovements.setDisable(true);

                dtPickerFrom.setValue(LocalDate.now());

                dtPickerTo.setValue(LocalDate.now());

                dtPickerFrom.setDisable(true);

                dtPickerTo.setDisable(true);

                btnSearch.setDisable(true);

                comboSelectDate.setValue("Select creation/expiration date");

            }
        } else {
            logger.log(Level.INFO, "Unexpected error occurred: ");

            alertError("Error loading data", "Unexpected error", "Unexpected error.");
            loadAllData();

            btnGotoMovements.setDisable(true);

            dtPickerFrom.setValue(LocalDate.now());

            dtPickerTo.setValue(LocalDate.now());

            dtPickerFrom.setDisable(true);

            dtPickerTo.setDisable(true);

            btnSearch.setDisable(true);

            comboSelectDate.setValue("Select creation/expiration date");
        }
    }

    private void handleClearTable(Event event) {
        List<CreditCardBean> csb = new ArrayList<>();

        ObservableList<CreditCardBean> dataList = FXCollections.observableArrayList(csb);

        tableViewCreditCard.setEditable(true);
        tableViewCreditCard.setItems(dataList);

        dtPickerFrom.setDisable(true);

        dtPickerTo.setDisable(true);

        btnSearch.setDisable(true);

        btnRemoveCard.setDisable(true);

        contextRemove.setDisable(true);

        btnGotoMovements.setDisable(true);

        comboSelectDate.setValue("Select creation/expiration date");
    }

    private void handleShowMovements(Event event) {
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/RovoBankMovementView.fxml"));

            Parent root = (Parent) FXMLLoader.load();

            String creditCardNumber = tableViewCreditCard.getSelectionModel().getSelectedItem().getCreditCardNumber();

            stage.setUserData(creditCardNumber);

            RovoBankMovementController controller = (RovoBankMovementController) FXMLLoader.getController();
            controller.setStage(stage);
            controller.initStage(root);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Something went wrong when loading the window.", e.getMessage());

        }
    }

    @FXML
    private void handlePrintCreditCardReport(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/com/tartanga/grupo4/resources/reports/CreditCardReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<CreditCardBean>) this.tableViewCreditCard.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }

    @FXML
    private void onCloseRequestWindowEvent(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to close the application?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.setTitle("Closing Application");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.resultProperty().get().equals(yesButton)) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

}
