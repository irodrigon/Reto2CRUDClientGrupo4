package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.ILoan;
import com.tartanga.grupo4.businesslogic.LoanFactory;
import com.tartanga.grupo4.models.Loan;
import com.tartanga.grupo4.models.LoanBean;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.core.GenericType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;

public class LoanController {

    private ObservableList<LoanBean> data = FXCollections.observableArrayList();
    private List<Loan> loans;

    private static final Logger LOGGER = Logger.getLogger("javaClient");

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox vBoxTop;
    @FXML
    private HBox hBoxHeader;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private TableView<Loan> loanTable;
    @FXML
    private TableColumn<Loan, Long> tcLoanId;
    @FXML
    private TableColumn<Loan, Date> tcStartingDate;
    @FXML
    private TableColumn<Loan, Date> tcEndingDate;
    @FXML
    private TableColumn<Loan, Double> tcTotalAmount;
    @FXML
    private TableColumn<Loan, Double> tcRemainingAmount;
    @FXML
    private TableColumn<Loan, Integer> tcInterestRate;
     @FXML
    private TableColumn<Loan, Integer> tcPeriod;
    
    @FXML
    private Button btnNew;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;

    @FXML
    private void initialize() {
        loanTable.setEditable(true);

        tcLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        tcStartingDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tcEndingDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tcTotalAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcRemainingAmount.setCellValueFactory(new PropertyValueFactory<>("rAmount"));
        tcInterestRate.setCellValueFactory(new PropertyValueFactory<>("interest"));
        tcPeriod.setCellValueFactory(new PropertyValueFactory<>("period"));

        tcTotalAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tcInterestRate.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcPeriod.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Set DatePicker for date columns
        tcStartingDate.setCellFactory(column -> new EditingCellDatePicker());
        tcStartingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            loan.setStartDate(event.getNewValue());
            LoanFactory.getInstance().getILoans().edit_XML(loan, loan.getIDProduct());
        });

        tcEndingDate.setCellFactory(column -> new EditingCellDatePicker());
        tcEndingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            loan.setStartDate(event.getNewValue());
            LoanFactory.getInstance().getILoans().edit_XML(loan, loan.getIDProduct());
        });

        // Configuring columns for editing
        configureEditableColumns();

        // Load all loans on start
        mostrarTodosLosPrestamos();
    }

    private void configureEditableColumns() {
        tcInterestRate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Integer newInterestRate = event.getNewValue();

            if (newInterestRate < 0) {
                LOGGER.log(Level.WARNING, "Interest rate cannot be negative.");
                return;
            }

            loan.setInterest(newInterestRate);
            updateLoan(loan);
        });
        
         tcPeriod.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Integer newPeriod = event.getNewValue();

            if (newPeriod < 0) {
                LOGGER.log(Level.WARNING, "Period cannot be negative.");
                return;
            }

            loan.setPeriod(newPeriod);
            updateLoan(loan);
        });

        tcTotalAmount.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Double newAmount = event.getNewValue();

            if (newAmount <= 0) {
                LOGGER.log(Level.WARNING, "Amount must be greater than zero.");
                return;
            }

            loan.setAmount(newAmount);
            updateLoan(loan);
        });

        tcStartingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date newDate = event.getNewValue();
            if (newDate == null) {
                LOGGER.log(Level.WARNING, "Invalid date: null value provided.");
                return;
            }
            loan.setStartDate(newDate);
            updateLoan(loan);
        });

        tcEndingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date newDate = event.getNewValue();
            if (newDate == null) {
                LOGGER.log(Level.WARNING, "Invalid date: null value provided.");
                return;
            }
            loan.setEndDate(newDate);
            updateLoan(loan);
        });
    }

    private void updateLoan(Loan loan) {
        try {
            LoanFactory.getInstance().getILoans().edit_XML(loan, loan.getIDProduct());
            LOGGER.log(Level.INFO, "Loan updated successfully: {0}", loan.getLoanId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating loan: {0}", e.getMessage());
        }
        loanTable.refresh();
    }

    // Button Actions
    @FXML
    private void buttonSearchAll(ActionEvent event) {
        mostrarTodosLosPrestamos();
    }

    @FXML
    private void buttonFilter(ActionEvent event) {
        mostrarPrestamosPorFecha();
    }

    @FXML
    private void addNewLoan(ActionEvent event) {

        Loan loan = new Loan();

        try {
            LoanFactory.getInstance().getILoans().create_XML(loan);
            List<Loan> datasList = LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
            });
            // datasList.get(datasList.size()-1).getIDProduct();
          
            Loan mLoan = datasList.get(datasList.size() - 1);
            Integer mId =(datasList.get(datasList.size() - 1).getIDProduct());
            
            mLoan.setLoanId((long) mId);

            LoanFactory.getInstance().getILoans().edit_XML(mLoan, mId);
            mostrarTodosLosPrestamos();
            LOGGER.log(Level.INFO, "Loan added successfully: {0}", loan.toString());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding loan to the table", e);
        }
    }

    @FXML
    private void SaveLoan(ActionEvent event) {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            LoanFactory.getInstance().getILoans().edit_XML(selectedLoan, selectedLoan.getIDProduct());
            mostrarTodosLosPrestamos();
            LOGGER.log(Level.INFO, "Datos guardados en la base de datos con éxito.");
        } else {
            showAlert("No Loan Selected", "Please select a loan to save.");
        }
    }

    @FXML
    private void DeleteLoan(ActionEvent event) {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            confirmAndDeleteLoan(selectedLoan);
        } else {
            showAlert("No Loan Selected", "Please select a loan to delete.");
        }
    }

    private void confirmAndDeleteLoan(Loan selectedLoan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar este préstamo?");
        alert.setContentText("ID del préstamo: " + selectedLoan.getLoanId());

        alert.showAndWait().ifPresent((ButtonType response) -> {
            if (response == ButtonType.OK) {
                try {
                    LoanFactory.getInstance().getILoans().remove(selectedLoan.getIDProduct());
                    mostrarTodosLosPrestamos();
                    LOGGER.log(Level.INFO, "Préstamo eliminado: {0}", selectedLoan.toString());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al eliminar el préstamo: {0}", e.getMessage());
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Data population
    private void mostrarTodosLosPrestamos() {
        try {
            LOGGER.log(Level.INFO, "LoanController(mostrarTodosLosPrestamos): Getting Loans");
            List<Loan> datas = LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
            });
            loanTable.setItems(FXCollections.observableArrayList(datas));
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "LoanController(mostrarTodosLosPrestamos): Exception while populating table, {0}", error.getMessage());
        }
    }

    private void mostrarPrestamosPorFecha() {
        try {
            LOGGER.log(Level.INFO, "LoanController(mostrarPrestamosPorFecha): Filtering Loans");

            loans = LoanFactory.getInstance().getILoans().getLoansByDate(new GenericType<List<Loan>>() {
            }, fromDate.getValue(), toDate.getValue());
            data = organizarData(loans);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "LoanController(mostrarPrestamosPorFecha): Exception while filtering loans, {0}", error.getMessage());
        }
    }

    private ObservableList<LoanBean> organizarData(List<Loan> loans) {
        ObservableList<LoanBean> loanData = FXCollections.observableArrayList();
        for (Loan loan : loans) {
            LoanBean loanBean = new LoanBean(
                    loan.getLoanId(),
                    loan.getCreationDate(),
                    loan.getInterest(),
                    loan.getEndDate(),
                    loan.getAmount(),
                    loan.getRAmount()
            );
            loanData.add(loanBean);
        }
        return loanData;
    }
}
