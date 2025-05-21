/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.AccountFactory;
import com.tartanga.grupo4.exception.AccountDoesNotExistException;
import com.tartanga.grupo4.models.Account;
import com.tartanga.grupo4.models.AccountBean;
import com.tartanga.grupo4.models.Customer;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.ws.rs.core.GenericType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TablePosition;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.ws.rs.WebApplicationException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Aitor
 *
 * COSAS POR HACER: Validacion de datos al editar tabla Al pulsar anadir cuenta
 * meter una nueva linea Menu contextual solo sale en celdas Implementar borrado
 * de lineas Configurar el menu contextual Igual seria mejor hacer una combo
 * para nombre y apellido no sea que meta un nuevo cliente y no tengamos sus
 * datos?
 */
public class AccountController implements Initializable {

    ResourceBundle resourceBundle = ResourceBundle.getBundle("com/tartanga/grupo4/resources/files/configuration");
    private String formato = resourceBundle.getString("formato_fecha");
    private ObservableList<AccountBean> data = FXCollections.observableArrayList();
    private AccountBean tableAccount;
    private SimpleDateFormat formateador = new SimpleDateFormat(formato);
    private DateTimeFormatter formateadorL = DateTimeFormatter.ofPattern(formato);
    private static final Logger LOGGER = Logger.getLogger("javaClient");
    private List<Customer> customers;
    private List<Account> accounts;
    private Stage stage;
    private Account account = new Account();
    private LocalDate today = LocalDate.now();
    @FXML
    private MenuItem itemAccountNum;
    @FXML
    private MenuItem itemOwner;
    @FXML
    private MenuItem itemDate;
    @FXML
    private MenuItem itemAll;
    @FXML
    private AnchorPane paneDate;
    @FXML
    private AnchorPane paneAccount;
    @FXML
    private AnchorPane paneCustomer;
    @FXML
    private Label showAll;
    @FXML
    private TextField customerName;
    @FXML
    private TextField customerSurname;
    @FXML
    private TextField accountNumber;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Button dateSearchButton;
    @FXML
    private Button customerSearchButton;
    @FXML
    private Button accountNumberSearchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<AccountBean> tableAccounts;
    @FXML
    private TableColumn<AccountBean, String> colAccountNumber;
    @FXML
    private TableColumn<AccountBean, String> colName;
    @FXML
    private TableColumn<AccountBean, String> colSurname;
    @FXML
    private TableColumn<AccountBean, String> colCreationDate;
    @FXML
    private TableColumn<AccountBean, Double> colBalance;
    @FXML
    private Button printButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        if (stage == null) {
            stage = new Stage();
        }
        stage.setScene(scene);
        stage.setTitle("Account");
        stage.setResizable(false);
        paneDate.setVisible(false);
        paneAccount.setVisible(false);
        paneCustomer.setVisible(false);
        showAll.setText("Showing all the accounts");
        customizeDatePickers();
        formatAccountNumber();
        iniTabla();
        deleteButton.setDisable(true);
        dateSearchButton.setDisable(true);
        accountNumberSearchButton.setDisable(true);

        printButton.setOnAction(this::printReport);
        itemAccountNum.setOnAction(this::menuButtonAccountHandler);
        itemOwner.setOnAction(this::menuButtonCustomerHandler);
        itemDate.setOnAction(this::menuButtonDateHandler);
        itemAll.setOnAction(this::buttonSearchAll);
        customerSearchButton.setOnAction(this::buttonSearchCustomer);
        accountNumberSearchButton.setOnAction(this::buttonSearchAccountNumber);
        dateSearchButton.setOnAction(this::buttonSearchByDates);
        deleteButton.setOnAction(this::handleAccountDelete);
        item2.setOnAction(this::handleAccountDelete);
        addButton.setOnAction(this::handleAccountCreation);
        item1.setOnAction(this::handleAccountCreation);
        setEnterKeyOnSearchButtons();
        accountNumberListener();
        datePickerListerners();
        item3.setOnAction(this::handleSetEdit);
        stage.show();
        stage.setOnCloseRequest(this::onCloseRequestWindowEvent);
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.F6)) {
                printReport(null);
            }
        });
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.F1)) {
                showHelp(null);
            }
        });

        //Quitarlo cuando implemente busqueda por customer
        itemOwner.setVisible(false);
        colSurname.setVisible(false);
        colName.setVisible(false);

    }

    public Stage getStage() {
        return stage;
    }

    /**
     * A set method for the stage.
     *
     * @param stage The stage you must set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
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

    //CONTROL DE STACK PANES
    @FXML
    private void menuButtonAccountHandler(ActionEvent event) {
        paneDate.setVisible(false);
        paneCustomer.setVisible(false);
        paneAccount.setVisible(true);
        showAll.setText("");
        accountNumber.clear();
        startDate.setValue(null);
        endDate.setValue(null);
    }

    @FXML
    private void menuButtonCustomerHandler(ActionEvent event) {
        paneDate.setVisible(false);
        paneAccount.setVisible(false);
        paneCustomer.setVisible(true);
        showAll.setText("");
    }

    @FXML
    private void menuButtonDateHandler(ActionEvent event) {
        paneAccount.setVisible(false);
        paneCustomer.setVisible(false);
        paneDate.setVisible(true);
        showAll.setText("");
        accountNumber.clear();
        startDate.setValue(null);
        endDate.setValue(null);
    }

    public void createTableView() {

    }

    @FXML
    private void buttonSearchAll(ActionEvent event) {
        paneAccount.setVisible(false);
        paneCustomer.setVisible(false);
        paneDate.setVisible(false);
        showAll.setText("Showing all the accounts");
        mostrarTodasCuentas();
        accountNumber.clear();
        startDate.setValue(null);
        endDate.setValue(null);
    }

    //STACKPANE ACTION EVENTS
    @FXML
    private void buttonSearchCustomer(ActionEvent event) {
        mostrarCuentasNombreApellido(customerName.getText(), customerSurname.getText());
    }

    @FXML
    private void buttonSearchAccountNumber(ActionEvent event) {
        String regex = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}-\\d{4}$";

        if (accountNumber.getText().length() != 24) {
            alertUser("Bank accounts consists of 20 numbers", 0);
        } else {
            if (accountNumber.getText().matches(regex)) {
                mostrarNumeroCuenta(accountNumber.getText());
            } else {
                alertUser("Only numbers are allowed \nAllowed format: XXXX-XXXX-XXXX-XXXX-XXXX", 0);
            }

        }

    }

    @FXML
    private void buttonSearchByDates(ActionEvent event) {
        if (startDate.getValue().isAfter(endDate.getValue())) {
            alertUser("The starting date can not be posterior to the ending date", 0);
        } else {
            mostrarCuentasPorFecha(startDate.getValue().toString(), endDate.getValue().toString());
        }

    }

    //MENU CONTEXTUAL
    ContextMenu contextTabla = new ContextMenu();
    MenuItem item1 = new MenuItem("Create");
    MenuItem item2 = new MenuItem("Delete");
    MenuItem item3 = new MenuItem("Update");

    //TABLA DE CUENTAS
    @FXML
    private void iniTabla() {
        tableAccounts.setEditable(true);
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));

        //FACTORIA DE CELDAS EDICION
        Callback<TableColumn<AccountBean, String>, TableCell<AccountBean, String>> cellFactoryTextField
                = (TableColumn<AccountBean, String> p) -> new EditingCellTextField(tableAccounts);

        Callback<TableColumn<AccountBean, String>, TableCell<AccountBean, String>> cellFactoryDatePicker
                = (TableColumn<AccountBean, String> p) -> new EditingCellDatePicker(tableAccounts);

        Callback<TableColumn<AccountBean, Double>, TableCell<AccountBean, Double>> cellFactoryDouble
                = (TableColumn<AccountBean, Double> p) -> new EditingCellDouble(tableAccounts);

        colName.setCellFactory(cellFactoryTextField);
        colName.setOnEditCommit(
                (CellEditEvent<AccountBean, String> t) -> {
                    ((AccountBean) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setName(t.getNewValue());
                });

        colSurname.setCellFactory(cellFactoryTextField);
        colSurname.setOnEditCommit(
                (CellEditEvent<AccountBean, String> t) -> {
                    ((AccountBean) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setSurname(t.getNewValue());
                });

        colCreationDate.setCellFactory(cellFactoryDatePicker);
        colCreationDate.setOnEditCommit(
                (CellEditEvent<AccountBean, String> t) -> {

                    Account accountD;
                    String dateNewValue;
                    LOGGER.log(Level.INFO, "AccountController(setOnEditCommit): Updating the date from account {0}",
                            t.getRowValue().getAccountNumber());
                    //Validacion de fechas introducidas por texto
                    if (LocalDate.parse(t.getNewValue(), formateadorL).isAfter(today)) {
                        dateNewValue = today.format(formateadorL);
                        accountD = toAccount(t.getRowValue(), dateNewValue);
                        alertUser("Creation date cannot be in the future \n Setting creation date with todays date", 1);
                    } else {
                        dateNewValue = t.getNewValue();
                        accountD = toAccount(t.getRowValue(), dateNewValue);
                    }
                    try {
                        AccountFactory.getInstance().getIaccounts().updateAccount(
                                accountD,
                                t.getRowValue().getId().toString());
                        ((AccountBean) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setCreationDate(dateNewValue);
                    } catch (Exception error) {
                        LOGGER.log(Level.SEVERE, "AccountController(mostrarTodasCuentas): "
                                + "Exception while updating the date, {0}", error.getMessage());
                        alertUser("An error happened while updating the account", 0);
                    }

                    tableAccounts.refresh();
                });

        colBalance.setCellFactory(cellFactoryDouble);
        colBalance.setOnEditCommit((TableColumn.CellEditEvent<AccountBean, Double> t) -> {
            LOGGER.log(Level.INFO, "AccountController(setOnEditCommit): Updating the balance from account {0}",
                    t.getRowValue().getAccountNumber());
            Account accountB = toAccount(t.getRowValue(), null);

            accountB.setBalance(t.getNewValue());
            try {
                AccountFactory.getInstance().getIaccounts().updateAccount(
                        accountB,
                        t.getRowValue().getId().toString());
                AccountBean accountBean = t.getTableView().getItems().get(t.getTablePosition().getRow());
                accountBean.setBalance(t.getNewValue());
            } catch (Exception error) {
                LOGGER.log(Level.SEVERE, "AccountController(mostrarTodasCuentas): "
                        + "Exception while updating the balance, {0}", error.getMessage());
                alertUser("An error happened while updating the account", 0);
            }
            tableAccounts.refresh();

        });
        item2.setDisable(true);
        item3.setDisable(true);
        contextTabla.getItems().addAll(item1, item2, item3);
        tableAccounts.setContextMenu(contextTabla);

        handleButtonDeselection();

        tableAccounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mostrarTodasCuentas();
        accountNumber.setPromptText("XXXX-XXXX-XXXX-XXXX-XXXX");
    }

    //METODOS PARA LLENAR LA TABLA
    private void mostrarTodasCuentas() {
        try {
            LOGGER.log(Level.INFO, "AccountController(mostrarTodasCuentas): Getting Accounts and Customers");

            accounts = AccountFactory.getInstance().getIaccounts()
                    .getAllAccounts(new GenericType<List<Account>>() {
                    });

            data = organizarData(accounts);
            if (data.isEmpty()) {
                throw new AccountDoesNotExistException();
            }
            tableAccounts.setItems(data);

        } catch (AccountDoesNotExistException error) {
            alertUser("Account(s) not found", 1);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarTodasCuentas): Exception while populating table, {0}", error.getMessage());
            //COMENTAR LA SIGUIENTE ALERTA ANTES DE LANZAR EL TEST CRUD FAIL********************************************************************
           alertUser("Cannot get accounts", 0);

        }
    }

    private void mostrarCuentasPorFecha(String fechaIni, String fechaFin) {
        try {
            LOGGER.log(Level.INFO, "AccountController(mostrarCuentasPorFecha): Getting Accounts and Customers");

            accounts = AccountFactory.getInstance().getIaccounts()
                    .getAccountsByDates(new GenericType<List<Account>>() {
                    }, fechaIni, fechaFin);

            data = organizarData(accounts);
            if (data.isEmpty()) {
                throw new AccountDoesNotExistException();
            }
            tableAccounts.setItems(data);
        } catch (AccountDoesNotExistException error) {
            alertUser("Account(s) not found", 1);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarCuentasPorFecha): Exception while populating table, {0}", error.getMessage());
            alertUser("An error happen in application", 0);
        }
    }

    private void mostrarNumeroCuenta(String accountNumber) {
        try {
            LOGGER.log(Level.INFO, "AccountController(mostrarNumeroCuenta): Getting the Account and Customers");
            accounts.clear();
            accounts.add(AccountFactory.getInstance().getIaccounts()
                    .getAccountByAccountNumber(new GenericType<Account>() {
                    }, accountNumber));

            data = organizarData(accounts);
            tableAccounts.setItems(data);

        } catch (WebApplicationException error) {
            alertUser("The introduced account does not exist", 1);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarNumeroCuenta): Exception while populating table, {0}", error.getMessage());
            alertUser("An error happen in application", 0);
        }
    }

    //No se usa de por el momento
    private void mostrarCuentasNombreApellido(String name, String surname) {
        try {

            if (!customerName.getText().equals("") && customerSurname.getText().equals("")) {
                customers = AccountFactory.getInstance().getIaccounts()
                        .findByName(new GenericType<List<Customer>>() {
                        }, name);
            } else if (customerName.getText().equals("") && !customerSurname.getText().equals("")) {
                customers = AccountFactory.getInstance().getIaccounts()
                        .findBySurname(new GenericType<List<Customer>>() {
                        }, surname);
            } else {
                customers = AccountFactory.getInstance().getIaccounts()
                        .findByNameSurname(new GenericType<List<Customer>>() {
                        }, name, surname);
            }

            LOGGER.log(Level.INFO, "AccountController(mostrarCuentasNombreApellido): Getting Accounts and Customers lists");
            accounts = AccountFactory.getInstance().getIaccounts()
                    .getAllAccounts(new GenericType<List<Account>>() {
                    });

            data = organizarData(accounts);
            tableAccounts.setItems(data);

        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarCuentasNomreApellido): "
                    + "Exception while populating table , {0}", error.getMessage());
        }
    }

    public ObservableList<AccountBean> organizarData(List<Account> accounts) {
        LOGGER.log(Level.INFO, "AccountController(organizarData): preparing the Observable List");
        data.clear();

        for (Account account : accounts) {

            tableAccount = new AccountBean(
                    account.getAccountNumber(),
                    formateador.format(account.getCreationDate()),
                    account.getBalance(),
                    account.getIDProduct());

            data.add(tableAccount);

        }

        return data;
    }

    //METODO ELIMINAR
    @FXML
    private void handleAccountDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Do you really want delete the selected accounts?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.setTitle("Delete");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.resultProperty().get().equals(yesButton)) {
            LOGGER.log(Level.INFO, "AccountController(handleAccountDelete): Deleting the selected items from the table");
            ObservableList<AccountBean> items;
            items = tableAccounts.getSelectionModel().getSelectedItems();
            List<AccountBean> itemsErase = new ArrayList(items);
            try {
                if (!itemsErase.isEmpty()) {
                    for (AccountBean bean : itemsErase) {
                        AccountFactory.getInstance().getIaccounts().deleteAccount(Integer.toString(bean.getId()));
                        tableAccounts.getItems().remove(bean);

                    }
                }
            } catch (Exception error) {
                LOGGER.log(Level.SEVERE, "AccountController(handleAccountDelete): Exception when deleting the account {0}", error.getMessage());
                alertUser("An error happened while deleting account", 0);
            }

            tableAccounts.refresh();
        } else {
            event.consume();
        }

    }

    //METODO MODIFICAR
    private Account toAccount(AccountBean accountBean, String date) {
        Account accountT = null;
        Date dateC = new Date();
        try {
            if (date != null) {
                dateC = formateador.parse(date);
            } else {
                dateC = formateador.parse(accountBean.getCreationDate());
            }
            accountT = new Account(accountBean.getAccountNumber(),
                    accountBean.getBalance(),
                    dateC,
                    accountBean.getId());
        } catch (ParseException error) {
            LOGGER.log(Level.INFO, "AccountController(toAccount): Exception while parsing to Date");
        }

        return accountT;
    }

    private void handleSetEdit(ActionEvent event) {
        TablePosition<AccountBean, ?> cell = tableAccounts.getSelectionModel().getSelectedCells().get(0);
        int row = cell.getRow();
        TableColumn<AccountBean, ?> column = cell.getTableColumn();
        if (column != colAccountNumber) {
            tableAccounts.edit(row, column);
        }
    }

    //METODO CREAR CUENTA
    @FXML
    private void handleAccountCreation(ActionEvent event) {
        Account accountC;
        Date today = new Date();

        //Generador de cuenta
        LOGGER.log(Level.INFO, "AccountController(handleAccountCreation): Creating a new account");
        Random random = new Random();

        StringBuilder accountNumberC = new StringBuilder();

        for (int i = 0; i < 20; i++) {
            int digit = random.nextInt(10);
            accountNumberC.append(digit);

            if ((i + 1) % 4 == 0 && i != 19) {
                accountNumberC.append("-");
            }
        }
        try {
            accountC = new Account(accountNumberC.toString(), 0.0, today, null);
            AccountFactory.getInstance().getIaccounts().createAccount(accountC);
            mostrarTodasCuentas();
        } catch (Exception error) {
            LOGGER.log(Level.INFO, "AccountController( handleAccountCreation): Exception while creating a new account");
            alertUser("An error happened while creating account", 0);
        }

    }

    //METODOS DE MODIFICAR ELEMENTOS VISTA
    private void handleButtonDeselection() {
        tableAccounts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                deleteButton.setDisable(true);
                item2.setDisable(true);
                item3.setDisable(true);

            } else {
                deleteButton.setDisable(false);
                item2.setDisable(false);
                item3.setDisable(false);
            }
        });
    }

    private void accountNumberListener() {
        accountNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 1) {
                accountNumberSearchButton.setDisable(false);
            } else {
                accountNumberSearchButton.setDisable(true);
            }
        });
    }

    private void datePickerListerners() {

        startDate.valueProperty().addListener((observableS, oldValueS, newValueS) -> {
            endDate.valueProperty().addListener((observableE, oldValueE, newValueE) -> {
                if (newValueE != null && newValueS != null) {
                    dateSearchButton.setDisable(false);
                } else {
                    dateSearchButton.setDisable(true);
                }

            });

        });
        endDate.valueProperty().addListener((observableS, oldValueS, newValueS) -> {
            startDate.valueProperty().addListener((observableE, oldValueE, newValueE) -> {
                if (newValueE != null && newValueS != null) {
                    dateSearchButton.setDisable(false);
                } else {
                    dateSearchButton.setDisable(true);
                }

            });

        });

    }

    private void formatAccountNumber() {
        accountNumber.textProperty().addListener((observable, oldValue, newValue) -> {

            String accountNumberF = newValue.replaceAll("[^\\d-]", "");

            if (accountNumberF.length() > 24) {
                accountNumberF = accountNumberF.substring(0, 24);
            }

            accountNumber.setText(accountNumberF);
        });
    }

    private void customizeDatePickers() {
        LocalDate today = LocalDate.now();
        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {

                        super.updateItem(item, empty);
                        //Pone el texto al valor de hoy si una fecha futura es introducida texto
                        if (startDate.getValue() != null && startDate.getValue().isAfter(today)) {
                            startDate.setValue(today);
                        }
                        if (endDate.getValue() != null && endDate.getValue().isAfter(today)) {
                            endDate.setValue(today);
                        }
                        if (item.isAfter(
                                LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");

                        }
                        //Desabilita fechas anteriores a la fecha elegida en startDate
                        if (startDate.getValue() != null && datePicker == endDate && item.isBefore(
                                startDate.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                        //Desabilita fechas posteriores a la fecha elegida en endDate
                        if (endDate.getValue() != null && datePicker == startDate && item.isAfter(
                                endDate.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        endDate.setDayCellFactory(dayCellFactory);
        startDate.setDayCellFactory(dayCellFactory);
        startDate.setPromptText(formato);
        endDate.setPromptText(formato);
    }

    private void setEnterKeyOnSearchButtons() {
        customerName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                customerSearchButton.fire();
            }
        });

        customerSurname.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                customerSearchButton.fire();
            }
        });

        accountNumber.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                accountNumberSearchButton.fire();
            }
        });

        startDate.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                dateSearchButton.fire();
            }
        });

        endDate.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                dateSearchButton.fire();
            }
        });
    }

    private static void alertUser(String msg, int tipo) {
        //0 para un mensaje de ERROR
        //1 para un mensaje informativo
        switch (tipo) {

            case 0:
                Alert alertE = new Alert(Alert.AlertType.ERROR, msg);
                alertE.showAndWait();
                break;

            case 1:
                Alert alertI = new Alert(Alert.AlertType.INFORMATION, msg);
                alertI.showAndWait();
        }

    }

    @FXML
    public void printReport(ActionEvent event) {
        try {
            JasperReport report
                    = JasperCompileManager.compileReport(getClass()
                            .getResourceAsStream("/com/tartanga/grupo4/resources/reports/AccountReport.jrxml"));

            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<AccountBean>) this.tableAccounts.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(handlePrintReport): Exception while creating the report {0}", error.getMessage());
            alertUser("An error happened while creating the report", 0);
        }

    }

    @FXML
    private void showHelp(ActionEvent event) {
        try {
            LOGGER.log(Level.INFO, "AccountController(showHelp): Loading the webView");
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/com/tartanga/grupo4/views/AccountHelp.fxml"));
            Parent root = (Parent) loader.load();
            HelpController controller
                    = ((HelpController) loader.getController());
            controller.initStage(root);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(showHelp): Exception while creating the report {0}", error.getMessage());
            alertUser("An error happened while trying to show the help window", 0);
        }
    }

}
