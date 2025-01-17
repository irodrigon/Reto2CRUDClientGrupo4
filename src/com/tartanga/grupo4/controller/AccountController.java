/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.AccountFactory;
import com.tartanga.grupo4.businesslogic.CustomerFactory;
import com.tartanga.grupo4.models.Account;
import com.tartanga.grupo4.models.AccountBean;
import com.tartanga.grupo4.models.Customer;
import com.tartanga.grupo4.models.Product;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ws.rs.core.GenericType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import org.eclipse.persistence.jpa.jpql.parser.NewValueBNF;

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

    private ObservableList<AccountBean> data = FXCollections.observableArrayList();
    AccountBean tableAccount;
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    private static final Logger LOGGER = Logger.getLogger("javaClient");
    List<Customer> customers;
    List<Account> accounts;

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

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        itemAccountNum.setOnAction(this::menuButtonAccountHandler);
        itemOwner.setOnAction(this::menuButtonCustomerHandler);
        itemDate.setOnAction(this::menuButtonDateHandler);
        itemAll.setOnAction(this::buttonSearchAll);
        customerSearchButton.setOnAction(this::buttonSearchCustomer);
        accountNumberSearchButton.setOnAction(this::buttonSearchAccountNumber);
        dateSearchButton.setOnAction(this::buttonSearchByDates);
        setEnterKeyOnSearchButtons();

    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
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
        stage.show();
        stage.setOnCloseRequest(this::onCloseRequestWindowEvent);
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
        //Image icon = new Image("/com/tartanga/grupo4/resources/images/servericon.png");
        //ImageView iconView = new ImageView(icon);
        // iconView.setFitWidth(32);
        // iconView.setFitHeight(32);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to close the application?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.setTitle("Closing Application");
        alert.setHeaderText(null);
        // Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        // alertStage.getIcons().add(icon);
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
    }

    public void createTableView() {

    }

    //STACKPANE ACTION EVENTS
    @FXML
    private void buttonSearchCustomer(ActionEvent event) {
        mostrarCuentasNombreApellido(customerName.getText(), customerSurname.getText());
    }

    @FXML
    private void buttonSearchAll(ActionEvent event) {
        paneAccount.setVisible(false);
        paneCustomer.setVisible(false);
        paneDate.setVisible(false);
        showAll.setText("Showing all the accounts");
        mostrarTodasCuentas();
    }

    @FXML
    private void buttonSearchAccountNumber(ActionEvent event) {
        mostrarNumeroCuenta(accountNumber.getText());
    }

    @FXML
    private void buttonSearchByDates(ActionEvent event) {
        mostrarCuentasPorFecha(startDate.getValue().toString(), endDate.getValue().toString());
    }

    //MENU CONTEXTUAL
    ContextMenu contextTabla = new ContextMenu();
    MenuItem item1 = new MenuItem("Create account");
    MenuItem item2 = new MenuItem("Delete account");
    MenuItem item3 = new MenuItem("Modify data");

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
                            t.getTablePosition().getRow())).setName(t.getNewValue());//cambiarlo de lambda y aqui
                    //es donde yo valido datos y errores, excepciones y lo actualizo en la base de datos
                    
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
                    ((AccountBean) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setCreationDate(t.getNewValue());
                });

        colBalance.setCellFactory(cellFactoryDouble);
        colBalance.setOnEditCommit((TableColumn.CellEditEvent<AccountBean, Double> t) -> {
            AccountBean account = t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setBalance(t.getNewValue());
        });
        contextTabla.getItems().addAll(item1, item2, item3);
        tableAccounts.setContextMenu(contextTabla);

        mostrarTodasCuentas();

    }

    //METODOS PARA LLENAR LA TABLA
    private void mostrarTodasCuentas() {
        try {
            LOGGER.log(Level.INFO, "AccountController(mostrarTodasCuentas): Getting Accounts and Customers");

            accounts = AccountFactory.getInstance().getIaccounts()
                    .getAllAccounts(new GenericType<List<Account>>() {
                    });
            customers = CustomerFactory.getInstance().getIcustomer()
                    .findAll_XML(new GenericType<List<Customer>>() {
                    });

            data = organizarData(accounts, customers);
            tableAccounts.setItems(data);

        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarTodasCuentas): Exception while populating table, {0}", error.getMessage());
        }
    }

    private void mostrarCuentasPorFecha(String fechaIni, String fechaFin) {
        try {
            LOGGER.log(Level.INFO, "AccountController(mostrarCuentasPorFecha): Getting Accounts and Customers");

            accounts = AccountFactory.getInstance().getIaccounts()
                    .findByDates(new GenericType<List<Account>>() {
                    }, fechaIni, fechaFin);
            customers = CustomerFactory.getInstance().getIcustomer()
                    .findAll_XML(new GenericType<List<Customer>>() {
                    });

            data = organizarData(accounts, customers);
            tableAccounts.setItems(data);

        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarCuentasPorFecha): Exception while populating table, {0}", error.getMessage());
        }
    }

    private void mostrarNumeroCuenta(String accountNumber) {
        try {
            LOGGER.log(Level.INFO, "AccountController(mostrarNumeroCuenta): Getting the Account and Customers");
            accounts.clear();
            accounts.add(AccountFactory.getInstance().getIaccounts()
                    .findByAccount(new GenericType<Account>() {
                    }, accountNumber));
            customers = CustomerFactory.getInstance().getIcustomer()
                    .findAll_XML(new GenericType<List<Customer>>() {
                    });

            data = organizarData(accounts, customers);
            tableAccounts.setItems(data);

        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarNumeroCuenta): Exception while populating table, {0}", error.getMessage());
        }
    }

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

            data = organizarData(accounts, customers);
            tableAccounts.setItems(data);

        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(mostrarCuentasNomreApellido): "
                    + "Exception while populating table , {0}", error.getMessage());
        }
    }

    private ObservableList<AccountBean> organizarData(List<Account> accounts, List<Customer> customers) {
        LOGGER.log(Level.INFO, "AccountController(organizarData): preparing the Observable List");
        data.clear();
        for (Customer customer : customers) {
            if (customer.getProducts() != null) {
                for (Product product : customer.getProducts()) {
                    for (Account account : accounts) {
                        if (Objects.equals(product.getIDProduct(), account.getIDProduct())) {
                            tableAccount = new AccountBean(
                                    account.getAccountNumber(),
                                    customer.getName(),
                                    customer.getSurname(),
                                    formateador.format(account.getCreationDate()),
                                    account.getBalance());

                            data.add(tableAccount);
                        }
                    }
                }
            }
        }

        return data;
    }
    //METODOS DE MODIFICAR ELEMENTOS
    private void formatAccountNumber(){
    accountNumber.textProperty().addListener((observable, oldValue, newValue) -> {

    String accountNumberF = newValue.replaceAll("[^\\d]", "");


    StringBuilder formatted = new StringBuilder();
    for (int i = 0; i < accountNumberF.length(); i++) {
        if (i > 0 && i % 4 == 0) {
            formatted.append("-");
        }
        formatted.append(accountNumberF.charAt(i));
    }
    accountNumber.setText(formatted.toString());

    accountNumber.positionCaret(formatted.length());
});
    }
    
    private void customizeDatePickers() {

        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(
                                LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");

                        }

                        if (datePicker == endDate && item.isBefore(
                                startDate.getValue())) {//Esto da NullPointer si no se a elegido valor en startDate previamente
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                            //endDate.setValue(startDate.getValue().plusDays(1));
                        }
                    }
                };
            }
        };
        endDate.setDayCellFactory(dayCellFactory);
        startDate.setDayCellFactory(dayCellFactory);
        startDate.setPromptText("dd/mm/yyyy");
        endDate.setPromptText("dd/mm/yyyy");
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

}
