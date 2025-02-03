package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.LoanFactory;
import com.tartanga.grupo4.models.Loan;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.apache.log4j.Logger;
import javax.ws.rs.core.GenericType;
import java.util.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class LoanController {

    private static final Logger LOGGER = Logger.getLogger(LoanController.class);

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

        // Configuración de las columnas
        tcLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        tcStartingDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tcEndingDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tcTotalAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcRemainingAmount.setCellValueFactory(new PropertyValueFactory<>("rAmount"));
        tcInterestRate.setCellValueFactory(new PropertyValueFactory<>("interest"));
        tcPeriod.setCellValueFactory(new PropertyValueFactory<>("period"));

        // Configuración para las celdas editables
        tcTotalAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tcInterestRate.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcPeriod.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Configuración para las celdas de fecha
        tcStartingDate.setCellFactory(column -> new EditingCellDatePicker());
        tcStartingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date oldStartDate = loan.getStartDate();  // Guardamos el valor anterior

            loan.setStartDate(event.getNewValue());
            if (isValidDate(loan.getStartDate(), loan.getEndDate())) {
                updateLoan(loan);
            } else {
                showErrorAlert("Invalid Date", "Start date cannot be later than end date.");
                loan.setStartDate(oldStartDate);  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });

        tcEndingDate.setCellFactory(column -> new EditingCellDatePicker());
        tcEndingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date oldEndDate = loan.getEndDate();  // Guardamos el valor anterior

            loan.setEndDate(event.getNewValue());
            if (isValidDate(loan.getStartDate(), loan.getEndDate())) {
                updateLoan(loan);
            } else {
                showErrorAlert("Invalid Date", "End date cannot be before start date.");
                loan.setEndDate(oldEndDate);  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });

        // Configuración de columnas editables
        configureEditableColumns();

        // Cargar los préstamos al iniciar
        mostrarTodosLosPrestamos();
    }

    private void configureEditableColumns() {
        tcInterestRate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Integer oldInterestRate = loan.getInterest();  // Guardamos el valor anterior

            String newInterestRateString = event.getNewValue().toString();
            if (isValidInteger(newInterestRateString)) {
                Integer newInterestRate = Integer.valueOf(newInterestRateString);
                if (!isValidInterestRate(newInterestRate)) {
                    showErrorAlert("Invalid Interest Rate", "Interest rate must be between 0 and 50.");
                    loan.setInterest(oldInterestRate);  // Restauramos el valor anterior
                    loanTable.refresh();
                    return;
                }
                loan.setInterest(newInterestRate);
                updateLoan(loan);
            } else {
                showErrorAlert("Invalid Input", "Please enter a valid integer for the interest rate.");
                loan.setInterest(oldInterestRate);  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });

        tcPeriod.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Integer oldPeriod = loan.getPeriod();  // Guardamos el valor anterior

            String newPeriodString = event.getNewValue().toString();
            if (isValidInteger(newPeriodString)) {
                Integer newPeriod = Integer.valueOf(newPeriodString);
                if (!isValidPeriod(newPeriod)) {
                    showErrorAlert("Invalid Period", "Period must be positive and no more than four digits.");
                    loan.setPeriod(oldPeriod);  // Restauramos el valor anterior
                    loanTable.refresh();
                    return;
                }
                loan.setPeriod(newPeriod);
                updateLoan(loan);
            } else {
                showErrorAlert("Invalid Input", "Please enter a valid integer for the period.");
                loan.setPeriod(oldPeriod);  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });

        tcTotalAmount.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();

            String newAmountString = event.getNewValue().toString();
            if (isValidDouble(newAmountString)) {
                Double newAmount = Double.valueOf(newAmountString);
                if (!isValidTotalAmount(newAmount)) {
                    showErrorAlert("Invalid Amount", "Amount must be positive and not exceed 100 million.");
                    loan.setAmount(loan.getAmount());  // Restauramos el valor anterior
                    loanTable.refresh();
                    return;
                }
                loan.setAmount(newAmount);
                updateLoan(loan);
            } else {
                showErrorAlert("Invalid Input", "Please enter a valid number for the total amount.");
                loan.setAmount(loan.getAmount());  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });
    }

    private boolean isValidTotalAmount(Double amount) {
        return amount > 0 && amount <= 100000000;
    }

    private boolean isValidInterestRate(Integer interestRate) {
        return interestRate >= 0 && interestRate <= 50;
    }

    private boolean isValidPeriod(Integer period) {
        return period > 0 && period <= 9999;
    }

    private boolean isValidDate(Date startDate, Date endDate) {
        return startDate != null && endDate != null && !startDate.after(endDate);
    }

    private boolean isValidDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void updateLoan(Loan loan) {
        try {
            LoanFactory.getInstance().getILoans().edit_XML(loan, loan.getIDProduct());
        } catch (Exception e) {
            showErrorAlert("Error Updating Loan", "Error updating loan: " + e.getMessage());
        }
        loanTable.refresh();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void DeleteLoan(ActionEvent event) {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            confirmAndDeleteLoan(selectedLoan);
        } else {
            showErrorAlert("No Loan Selected", "Please select a loan to delete.");
        }
    }

    private void confirmAndDeleteLoan(Loan selectedLoan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this loan?");
        alert.setContentText("Loan ID: " + selectedLoan.getLoanId());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    LoanFactory.getInstance().getILoans().remove(selectedLoan.getIDProduct());
                    mostrarTodosLosPrestamos();
                } catch (Exception e) {
                    showErrorAlert("Error Deleting Loan", "Error deleting loan: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void addNewLoan(ActionEvent event) {
        Loan loan = new Loan();
        try {
            LoanFactory.getInstance().getILoans().create_XML(loan);
            List<Loan> datasList = LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
            });
            Loan mLoan = datasList.get(datasList.size() - 1);
            Integer mId = mLoan.getIDProduct();
            mLoan.setLoanId((long) mId);
            LoanFactory.getInstance().getILoans().edit_XML(mLoan, mId);
            mostrarTodosLosPrestamos();
        } catch (Exception e) {
            LOGGER.error("Error adding loan: ", e);
        }
    }

    @FXML
    private void PrintLoan(ActionEvent event) {
        try {
            LOGGER.info("Beginning print action...");
            JasperReport report = JasperCompileManager.compileReport(getClass()
                    .getResourceAsStream("resources.reports\\LoanReport.jrxml.xml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource(LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
            }));
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "Loan Report");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            LOGGER.error("Error generating report", e);
        }
    }

    private void mostrarTodosLosPrestamos() {
        ObservableList<Loan> data = FXCollections.observableArrayList(LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
        }));
        loanTable.setItems(data);
    }
}