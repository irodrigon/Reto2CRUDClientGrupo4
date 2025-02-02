package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.TransferRESTFull;
import com.tartanga.grupo4.businesslogic.Itransfer;
import com.tartanga.grupo4.businesslogic.TransferFactory;
import com.tartanga.grupo4.models.Account;
import com.tartanga.grupo4.models.Currency;
import com.tartanga.grupo4.models.Customer;
import com.tartanga.grupo4.models.Transfers;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

public class TransferController implements Initializable {

    @FXML
    private TableView<Transfers> tbTransfer;

    @FXML
    private TableColumn<Transfers, Integer> tbcTransferId;

    @FXML
    private TableColumn<Transfers, String> tbcSender;

    @FXML
    private TableColumn<Transfers, String> tbcReciever;

    @FXML
    private TableColumn<Transfers, Date> tbcDate;

    @FXML
    private TableColumn<Transfers, Double> tbcAmount;

    @FXML
    private DatePicker dtpFirst;

    @FXML
    private DatePicker dtpLast;

    @FXML
    private Button btnFindDate;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox<String> cmbAccount;

    @FXML
    private ComboBox<Currency> cmbMoney;

    @FXML
    private TextField fldAccount;

    @FXML
    private Button btnFindAccount;

    @FXML
    private MenuItem cnmReset;

    @FXML
    private MenuItem cnmNew;

    @FXML
    private MenuItem cnmDelete;

    private ObservableList<Transfers> transferData;

    private Itransfer transferManager;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        transferManager = TransferFactory.getItransfer();

        // Configurar ComboBox de cuentas
        cmbAccount.getItems().addAll("Sender", "Reciever", "Id");
        cmbAccount.setValue("Sender");

        cmbMoney.getItems().addAll(Currency.values());
        cmbMoney.setValue(Currency.EURO); // Divisa por defecto

        // Listener para manejar el cambio de divisa
        cmbMoney.setOnAction(this::ParaFuncinar);

        // Listener para habilitar/deshabilitar btnDelete según la selección
        tbTransfer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnDelete.setDisable(newSelection == null); // Habilitar si hay una fila seleccionada
        });

        btnNew.setOnAction(this::createTransfer);
        cnmNew.setOnAction(this::createTransfer);

        btnDelete.setOnAction(this::deleteTransfer);
        cnmDelete.setOnAction(this::deleteTransfer);

        cnmReset.setOnAction(this::HandleReset);

        // Configurar los botones
        btnFindDate.setOnAction(this::filterByDate);
        btnFindAccount.setOnAction(this::filterByAccount);

        initTable();
    }

    public void HandleReset(ActionEvent event) {
        loadAllTransfers();
    }

    @FXML
    public void initTable() {

        tbTransfer.setEditable(true);

        tbcTransferId.setCellValueFactory(new PropertyValueFactory<>("transferId"));
        tbcSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        tbcReciever.setCellValueFactory(new PropertyValueFactory<>("reciever"));
        tbcDate.setCellValueFactory(new PropertyValueFactory<>("transferDate"));
        tbcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tbcSender.setCellFactory(TextFieldTableCell.<Transfers>forTableColumn());
        tbcSender.setOnEditCommit((CellEditEvent<Transfers, String> t) -> {
            Transfers transfer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            transfer.setSender(t.getNewValue());
            transferManager.edit_XML(transfer, transfer.getTransferId().toString());
        });

        tbcReciever.setCellFactory(TextFieldTableCell.<Transfers>forTableColumn());
        tbcReciever.setOnEditCommit((CellEditEvent<Transfers, String> t) -> {
            Transfers transfer = t.getTableView().getItems().get(t.getTablePosition().getRow());
            transfer.setReciever(t.getNewValue());
            transferManager.edit_XML(transfer, transfer.getTransferId().toString());
        });

        tbcDate.setCellFactory(colum -> new DatePickerCellEditer());
        tbcDate.setOnEditCommit(event -> {
            Transfers transfer = event.getRowValue();
            transfer.setTransferDate(event.getNewValue());
            transferManager.edit_XML(transfer, transfer.getTransferId().toString());
        });

        tbcAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        tbcAmount.setOnEditCommit((CellEditEvent<Transfers, Double> t) -> {
            Transfers transfer = t.getRowValue();
            transfer.setAmount(t.getNewValue());
            transferManager.edit_XML(transfer, transfer.getTransferId().toString());
        });

        loadAllTransfers();
    }

    @FXML
    private void ParaFuncinar(ActionEvent event) {
        updateAmountsForCurrency();
    }

    @FXML
    private void createTransfer(ActionEvent event) {
        // Crea una nueva transferencia 
        Transfers newTransfer = new Transfers();

        transferManager.create_XML(newTransfer);

        // Añade la nueva transferencia a la lista observable y a la tabla
        transferData.add(newTransfer);
        tbTransfer.setItems(transferData);

        // Refrescar la tabla para mostrar la nueva fila
        tbTransfer.refresh();

        loadAllTransfers();

    }

    @FXML
    private void deleteTransfer(ActionEvent event) {
        Transfers borrar = (Transfers) tbTransfer.getSelectionModel().getSelectedItem();
        transferManager.remove(String.valueOf(borrar.getTransferId()));
        tbTransfer.getItems().remove(borrar);
        tbTransfer.refresh();
    }

    private void updateAmountsForCurrency() {
        Currency selectedCurrency = cmbMoney.getValue();
        if (selectedCurrency == null) {
            return;
        }

        switch (selectedCurrency) {
            case EURO:
                for (Transfers transfer : transferData) {
                    transfer.setAmount(transfer.getAmount() / transfer.getCurrency().getExchangeRate());
                    transfer.setCurrency(selectedCurrency);
                }
                break;
            case DOLLAR:
                for (Transfers transfer : transferData) {
                    if (transfer.getCurrency() == Currency.EURO) {
                        transfer.setAmount(transfer.getAmount() * Currency.DOLLAR.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    } else {
                        transfer.setAmount(transfer.getAmount() / transfer.getCurrency().getExchangeRate() * Currency.DOLLAR.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    }
                }

                break;
            case YEN:
                for (Transfers transfer : transferData) {
                    if (transfer.getCurrency() == Currency.EURO) {
                        transfer.setAmount(transfer.getAmount() * Currency.YEN.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    } else {
                        transfer.setAmount(transfer.getAmount() / transfer.getCurrency().getExchangeRate() * Currency.YEN.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    }
                }
                break;
            case SWISSFRANC:
                for (Transfers transfer : transferData) {
                    if (transfer.getCurrency() == Currency.EURO) {
                        transfer.setAmount(transfer.getAmount() * Currency.SWISSFRANC.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    } else {
                        transfer.setAmount(transfer.getAmount() / transfer.getCurrency().getExchangeRate() * Currency.SWISSFRANC.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    }
                }
                break;
            case POUND:
                for (Transfers transfer : transferData) {
                    if (transfer.getCurrency() == Currency.EURO) {
                        transfer.setAmount(transfer.getAmount() * Currency.POUND.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    } else {
                        transfer.setAmount(transfer.getAmount() / transfer.getCurrency().getExchangeRate() * Currency.POUND.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    }
                }
                break;
            case LEI:
                for (Transfers transfer : transferData) {
                    if (transfer.getCurrency() == Currency.EURO) {
                        transfer.setAmount(transfer.getAmount() * Currency.LEI.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    } else {
                        transfer.setAmount(transfer.getAmount() / transfer.getCurrency().getExchangeRate() * Currency.LEI.getExchangeRate());
                        transfer.setCurrency(selectedCurrency);
                    }
                }
                break;
        }

        // Refrescar la tabla para mostrar los nuevos valores
        tbTransfer.refresh();
    }

    @FXML
    private void loadAllTransfers() {
        TransferRESTFull client = new TransferRESTFull();
        List<Transfers> transferList = client.findAll_XML(new GenericType<List<Transfers>>() {
        });
        transferData = FXCollections.observableArrayList(transferList);
        tbTransfer.setItems(transferData);
    }

    @FXML
    private void filterByDate(ActionEvent event) {
        // Validar que ambas fechas estén seleccionadas
        if (dtpFirst.getValue() == null || dtpLast.getValue() == null) {
            return;
        }

        // Convertir las fechas al formato esperado (aaaa/mm/dd)
        String startDate = dtpFirst.getValue().toString();
        String endDate = dtpLast.getValue().toString();

        // Llamar al método RESTful para obtener transferencias por fecha
        TransferRESTFull client = new TransferRESTFull();
        try {
            List<Transfers> filteredTransfers = client.findByDate(
                    new GenericType<List<Transfers>>() {
            },
                    startDate,
                    endDate
            );

            // Actualizar la tabla con los datos filtrados
            transferData = FXCollections.observableArrayList(filteredTransfers);
            tbTransfer.setItems(transferData);

        } catch (Exception e) {
            System.err.println("Error al filtrar transferencias por fecha: " + e.getMessage());
        }
    }

    @FXML
    private void filterByAccount(ActionEvent event) {
        // Validar que haya una selección en el ComboBox
        String selectedFilter = cmbAccount.getValue();
        if (selectedFilter == null || selectedFilter.isEmpty()) {
            System.out.println("Por favor, selecciona una opción en el ComboBox.");
            return;
        }

        // Validar que el campo de texto no esté vacío
        String accountValue = fldAccount.getText();
        if (accountValue == null || accountValue.isEmpty()) {
            System.out.println("Por favor, ingresa un valor en el campo de texto.");
            return;
        }

        // Llamar al método RESTful adecuado
        TransferRESTFull client = new TransferRESTFull();
        try {
            if ("Sender".equalsIgnoreCase(selectedFilter)) {
                // Filtrar por Sender (igual que antes)
                List<Transfers> filteredTransfers = client.findBySender(new GenericType<List<Transfers>>() {
                }, accountValue);
                // Actualizar la tabla con los datos filtrados
                transferData = FXCollections.observableArrayList(filteredTransfers);
                tbTransfer.setItems(transferData);

            } else if ("Reciever".equalsIgnoreCase(selectedFilter)) {
                // Filtrar por Reciever (igual que antes)
                List<Transfers> filteredTransfers = client.findByReciever(new GenericType<List<Transfers>>() {
                }, accountValue);
                // Actualizar la tabla con los datos filtrados
                transferData = FXCollections.observableArrayList(filteredTransfers);
                tbTransfer.setItems(transferData);

            } else if ("Id".equalsIgnoreCase(selectedFilter)) {
                // Filtrar por Id (cambia a usar un solo objeto de tipo Transfers)
                Transfers filteredTransfer = client.findByID(new GenericType<Transfers>() {
                }, accountValue);
                // Mostrar el resultado en la tabla (si solo hay un resultado)
                transferData = FXCollections.observableArrayList(filteredTransfer);
                tbTransfer.setItems(transferData);

            } else {
                loadAllTransfers();
            }

        } catch (Exception e) {
            System.err.println("Error al filtrar transferencias por cuenta: " + e.getMessage());
        }
    }
}
