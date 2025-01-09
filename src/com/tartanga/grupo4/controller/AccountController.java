/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import java.net.URL;
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

/**
 *
 * @author Aitor
 *
 * COSAS POR HACER: Validacion de datos al editar tabla Al pulsar anadir cuenta
 * meter una nueva linea Menu contextual solo sale en celdas Implementar borrado
 * de lineas
 * Configurar el menu contextual
 * Igual seria mejor hacer una combo para nombre y apellido no sea que meta un nuevo cliente y no tengamos sus datos?
 */
public class AccountController implements Initializable {

    private ObservableList<AccountBean> testData;

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

    private static Logger logger = Logger.getLogger(AccountController.class.getName());
    ;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        itemAccountNum.setOnAction(this::menuButtonAccountHandler);
        itemOwner.setOnAction(this::menuButtonCustomerHandler);
        itemDate.setOnAction(this::menuButtonDateHandler);
        itemAll.setOnAction(this::menuButtonAllHandler);
    }

    public void initStage(Parent root) {
        logger.info("Initializing Login stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Account");
        stage.setResizable(false);
        paneDate.setVisible(false);
        paneAccount.setVisible(false);
        paneCustomer.setVisible(false);
        showAll.setText("Showing all the accounts");
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

    @FXML
    private void menuButtonAllHandler(ActionEvent event) {
        paneAccount.setVisible(false);
        paneCustomer.setVisible(false);
        paneDate.setVisible(false);
        showAll.setText("Showing all the accounts");
    }

    public void createTableView() {

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

        Callback<TableColumn<AccountBean, String>, TableCell<AccountBean, String>> cellFactory
                = (TableColumn<AccountBean, String> p) -> new EditingCellTextField(tableAccounts);

        //FACTORIA DE CELDAS
        colAccountNumber.setCellFactory(cellFactory);
        colAccountNumber.setOnEditCommit(
                (CellEditEvent<AccountBean, String> t) -> {
                    ((AccountBean) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setAccountNumber(t.getNewValue());
                });

        colName.setCellFactory(cellFactory);
        colName.setOnEditCommit(
                (CellEditEvent<AccountBean, String> t) -> {
                    ((AccountBean) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setName(t.getNewValue());
                });

        colSurname.setCellFactory(cellFactory);
        colSurname.setOnEditCommit(
                (CellEditEvent<AccountBean, String> t) -> {
                    ((AccountBean) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setSurname(t.getNewValue());
                });

        colBalance.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double balance) {
                return balance == null ? "" : balance.toString();
            }

            @Override
            public Double fromString(String balance) {
                try {
                    return Double.parseDouble(balance);
                } catch (NumberFormatException error) {
                    return 0.0;
                }
            }
        }));

        colBalance.setOnEditCommit((TableColumn.CellEditEvent<AccountBean, Double> t) -> {
            AccountBean account = t.getTableView().getItems().get(t.getTablePosition().getRow());
            account.setBalance(t.getNewValue());
        });

        //DATOS DE PRUEBA
        testData = FXCollections.observableArrayList(
                new AccountBean("12343-35678-39101-34567", "John", "Doe", "12-12-1956", 1000.50),
                new AccountBean("23451-16789-11012-23456", "Jane", "Smith", "23-11-2001", 2000.75),
                new AccountBean("34561-17890-11123-45432", "Bob", "Johnson", "20-10-2015", 300.25)
        );
        contextTabla.getItems().addAll(item1, item2, item3);
        tableAccounts.setContextMenu(contextTabla);
        tableAccounts.setItems(testData); // Set the data in the TableView

    }

}
