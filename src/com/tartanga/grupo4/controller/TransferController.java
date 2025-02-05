package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.TransferRESTFull;
import com.tartanga.grupo4.businesslogic.Itransfer;
import com.tartanga.grupo4.businesslogic.TransferFactory;
import com.tartanga.grupo4.exception.DeleteException;
import com.tartanga.grupo4.models.Account;
import com.tartanga.grupo4.models.Currency;
import com.tartanga.grupo4.models.Customer;
import com.tartanga.grupo4.models.Transfers;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

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
    
    private static final Logger LOGGER = Logger.getLogger("javaClient");

    private Itransfer transferManager;
    
    private Stage stage;
    
    public void initstage(Parent root){

         stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.F6)) {
                printReport(null);
            }
        });
    }

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

        cnmReset.setOnAction(this::printReport);

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
            try {
                String newValue = t.getNewValue();
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                    showAlert("Error", "Solo se permiten letras en el remitente.");
                    tbTransfer.refresh();
                    return;
                }
                Transfers transfer = t.getRowValue();
                transfer.setSender(newValue);
                transferManager.edit_XML(transfer, transfer.getTransferId().toString());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al editar remitente", e);
            }
        });

        tbcReciever.setCellFactory(TextFieldTableCell.<Transfers>forTableColumn());
        tbcReciever.setOnEditCommit((CellEditEvent<Transfers, String> t) -> {
            try {
                String newValue = t.getNewValue();
                if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                    showAlert("Error", "Solo se permiten letras en el destinatario.");
                    tbTransfer.refresh();
                    return;
                }
                Transfers transfer = t.getRowValue();
                transfer.setReciever(newValue);
                transferManager.edit_XML(transfer, transfer.getTransferId().toString());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al editar destinatario", e);
            }
        });

        tbcDate.setCellFactory(colum -> new DatePickerCellEditer());
        tbcDate.setOnEditCommit(event -> {
            try {
                if (event.getNewValue() == null) {
                    showAlert("Error", "Formato de fecha incorrecto. Debe ser dd/mm/aaaa.");
                    tbTransfer.refresh();
                    return;
                }
                Transfers transfer = event.getRowValue();
                transfer.setTransferDate(event.getNewValue());
                transferManager.edit_XML(transfer, transfer.getTransferId().toString());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al editar fecha de transferencia", e);
            }
        });

        tbcAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tbcAmount.setOnEditCommit((CellEditEvent<Transfers, Double> t) -> {
            try {
                Transfers transfer = t.getRowValue();
                transfer.setAmount(t.getNewValue());
                transferManager.edit_XML(transfer, transfer.getTransferId().toString());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al editar monto de transferencia", e);
            }
        });

        loadAllTransfers();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void ParaFuncinar(ActionEvent event) {
        updateAmountsForCurrency();
    }

    @FXML
    private void createTransfer(ActionEvent event) {
        try {
            LOGGER.info("Creando nueva transferencia");
            Transfers newTransfer = new Transfers();
            transferManager.create_XML(newTransfer);
            transferData.add(newTransfer);
            tbTransfer.setItems(transferData);
            tbTransfer.refresh();
            LOGGER.info("Transferencia creada correctamente");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al crear transferencia", e);
        }
        loadAllTransfers();
    }

@FXML
    private void deleteTransfer(ActionEvent event) {
        try {
            LOGGER.info("Eliminando transferencia seleccionada");
            Transfers borrar = tbTransfer.getSelectionModel().getSelectedItem();
            transferManager.remove(String.valueOf(borrar.getTransferId()));
            tbTransfer.getItems().remove(borrar);
            tbTransfer.refresh();
            LOGGER.info("Transferencia eliminada correctamente");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar transferencia", e);
        }
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
        ObservableList<Transfers> add = FXCollections.observableArrayList(transferManager.findAll_XML(new GenericType<List<Transfers>>() {
        }));
        tbTransfer.setItems(add);
    }

  @FXML
    private void filterByDate(ActionEvent event) {
        try {
            LOGGER.info("Filtrando transferencias por fecha");
            if (dtpFirst.getValue() == null || dtpLast.getValue() == null) {
                LOGGER.warning("Fechas no seleccionadas para el filtro");
                return;
            }
            if (dtpLast.getValue().isBefore(dtpFirst.getValue())) {
                showAlert("Error", "La fecha final no puede ser anterior a la fecha inicial.");
                return;
            }
            String startDate = dtpFirst.getValue().toString();
            String endDate = dtpLast.getValue().toString();
            List<Transfers> resultList = transferManager.findByDate(new GenericType<List<Transfers>>() {}, startDate, endDate);
            transferData = FXCollections.observableArrayList(resultList);
            tbTransfer.setItems(transferData);
            LOGGER.info("Transferencias filtradas correctamente");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar transferencias por fecha", e);
        }
    }

    @FXML
    private void filterByAccount(ActionEvent event) {
        try {
            LOGGER.info("Iniciando filtrado por cuenta");
            String selectedFilter = cmbAccount.getValue();
            if (selectedFilter == null || selectedFilter.isEmpty()) {
                showAlert("Error", "Por favor, selecciona una opción en el ComboBox.");
                return;
            }
            String accountValue = fldAccount.getText();
            if (accountValue == null || accountValue.isEmpty()) {
                showAlert("Error", "Por favor, ingresa un valor en el campo de texto.");
                return;
            }
            ObservableList<Transfers> filteredTransfers;
            if ("Sender".equalsIgnoreCase(selectedFilter)) {
                List<Transfers> resultList = transferManager.findBySender(new GenericType<List<Transfers>>() {}, accountValue);
                filteredTransfers = FXCollections.observableArrayList(resultList);
            } else if ("Reciever".equalsIgnoreCase(selectedFilter)) {
                List<Transfers> resultList = transferManager.findByReciever(new GenericType<List<Transfers>>() {}, accountValue);
                filteredTransfers = FXCollections.observableArrayList(resultList);
            } else if ("Id".equalsIgnoreCase(selectedFilter)) {
                Transfers result = transferManager.findByID(new GenericType<Transfers>() {}, accountValue);
                filteredTransfers = FXCollections.observableArrayList(result);
            } else {
                loadAllTransfers();
                return;
            }
            transferData = FXCollections.observableArrayList(filteredTransfers);
            tbTransfer.setItems(transferData);
            LOGGER.info("Filtrado por cuenta completado correctamente");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar transferencias por cuenta", e);
            showAlert("Error", "Error al filtrar transferencias por cuenta");
        }
    }

        @FXML
    public void printReport(ActionEvent event) {
        try {
            JasperReport report
                    = JasperCompileManager.compileReport(getClass()
                            .getResourceAsStream("/com/tartanga/grupo4/resources/reports/TransferReport.jrxml"));

            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<Transfers>) this.tbTransfer.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(handlePrintReport): Exception while creating the report {0}", error.getMessage());
        }

    }
}
