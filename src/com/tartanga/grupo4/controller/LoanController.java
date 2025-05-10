package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.businesslogic.LoanFactory;
import com.tartanga.grupo4.models.Loan;
import java.time.LocalDate;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.core.GenericType;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * The LoanController class is responsible for managing the user interface for
 * loan operations. It handles the display, editing, searching, and deletion of
 * loans, as well as generating reports.
 */
public class LoanController {

    private static Logger LOGGER = Logger.getLogger(LoanController.class.getName());

    // FXML elements
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox vBoxTop;
    @FXML
    private HBox hBoxHeader;
    @FXML
    private ChoiceBox<String> choiceSearch;
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
    private TextField tfSearch;

    @FXML
    private Button btnNew;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnDelete;

    private Stage stage;

    /**
     * Initializes the controller. This method is automatically called after the
     * FXML file has been loaded. It sets up the table columns, configures
     * editable cells, and loads all loans into the table.
     */
    @FXML
    private void initialize() {
        loanTable.setEditable(true);
        choiceSearch.getItems().addAll("Search by Date", "Search by interest rate", "Search by Amount");

        // Configure table columns
        tcLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        tcStartingDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tcEndingDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tcTotalAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcRemainingAmount.setCellValueFactory(new PropertyValueFactory<>("rAmount"));
        tcInterestRate.setCellValueFactory(new PropertyValueFactory<>("interest"));
        tcPeriod.setCellValueFactory(new PropertyValueFactory<>("period"));

        btnSearch.setOnAction(this::onSearch);

        // Configure editable cells for total amount
        tcTotalAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
            private Double previousAmount;

            @Override
            public Double fromString(String value) {
                try {
                    previousAmount = Double.valueOf(value);
                    return previousAmount;
                } catch (NumberFormatException e) {
                    LOGGER.severe("Invalid input: " + value);
                    return previousAmount;
                }
            }
        }));

        // Configure editable cells for interest rate
        tcInterestRate.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            private Integer previousValue;

            @Override
            public Integer fromString(String value) {
                try {
                    previousValue = Integer.valueOf(value);
                    return previousValue;
                } catch (NumberFormatException e) {
                    LOGGER.severe("Invalid input: " + value);
                    return previousValue;
                }
            }
        }));

        // Configure editable cells for period
        tcPeriod.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            private Integer previousValue;

            @Override
            public Integer fromString(String value) {
                try {
                    previousValue = Integer.valueOf(value);
                    return previousValue;
                } catch (NumberFormatException e) {
                    LOGGER.severe("Invalid input: " + value);
                    return previousValue;
                }
            }
        }));

        // Configure date picker cells for start and end dates
        tcStartingDate.setCellFactory(column -> new LoanCellDatePicker());
        tcStartingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date oldStartDate = loan.getStartDate();
            try {
                loan.setStartDate(event.getNewValue());
                if (loan.getStartDate() == null || loan.getEndDate() == null || loan.getStartDate().after(loan.getEndDate())) {
                    throw new Exception("Start date cannot be later than end date.");
                }
                updateLoan(loan);
            } catch (Exception e) {
 LOGGER.severe("Invalid Date, end date cannot be before start date.");                showErrorAlert("Invalid Date", "Start date cannot be later than end date.");
                loan.setStartDate(oldStartDate);
                loanTable.refresh();
            }
        });

        tcEndingDate.setCellFactory(column -> new LoanCellDatePicker());
        tcEndingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date oldEndDate = loan.getEndDate();
            loan.setEndDate(event.getNewValue());
            try {
                if (loan.getStartDate() == null || loan.getEndDate() == null || loan.getStartDate().after(loan.getEndDate())) {
                    throw new Exception("Start date cannot be later than end date.");
                }
                updateLoan(loan);
            } catch (Exception e) {
                LOGGER.severe("Invalid Date, end date cannot be before start date.");
                showErrorAlert("Invalid Date", "End date cannot be before start date.");
                loan.setEndDate(oldEndDate);
                loanTable.refresh();
            }
        });

        choiceSearch.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if ("Search by Date".equals(newValue)) {
                mostrarPorFecha();
            } else {
                mostrarPorTexto();
            }
        });
        tfSearch.setVisible(false);
        tfSearch.setManaged(false);
        btnSearch.setVisible(false);
        fromDate.setVisible(false);
        fromDate.setManaged(false);
        fromDate.setPromptText("Fecha desde:");
        toDate.setVisible(false);
        toDate.setManaged(false);
        toDate.setPromptText("Fecha hasta:");
        // Configure editable columns
        configureEditableColumns();

        // Load all loans into the table
        mostrarTodosLosPrestamos();
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);

        stage.setTitle("Loans");
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Configures the editable columns for interest rate, period, and total
     * amount. Ensures that the input values are within valid ranges.
     */
    private void configureEditableColumns() {
        tcInterestRate.setOnEditCommit((TableColumn.CellEditEvent<Loan, Integer> event) -> {
            Loan loan = event.getRowValue();
            Integer oldInterestRate = loan.getInterest();
            try {
                String newInterestRateString = event.getNewValue().toString();
                Integer newInterestRate = Integer.valueOf(newInterestRateString);
                if (newInterestRate < 0 || newInterestRate > 50) {
                    throw new Exception("Interest rate should be greater than 0 and lower than 50!.");
                }
                loan.setInterest(newInterestRate);
                updateLoan(loan);
            } catch (Exception e) {
                LOGGER.severe("The interest rate should be an integer greater than 0 and less than 50!");
                showErrorAlert("Invalid Input", "The interest rate should be an integer greater than 0 and less than 50!");
                loan.setInterest(oldInterestRate);
                loanTable.refresh();
            }
        });

        tcPeriod.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Integer oldPeriod = loan.getPeriod();
            try {
                String newPeriodString = event.getNewValue().toString();
                Integer newPeriod = Integer.valueOf(newPeriodString);
                if (newPeriod < 0 || newPeriod > 1000) {
                    throw new Exception("Period should be greater than 0 and lower than 1000!.");
                }
                loan.setPeriod(newPeriod);
                updateLoan(loan);
            } catch (Exception e) {
                showErrorAlert("Invalid Input", "Period should be greater than 0 and lower than 1000!.");
                loan.setPeriod(oldPeriod);
                loanTable.refresh();
            }
        });

        tcTotalAmount.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            try {
                Double newAmountString = event.getNewValue();
                Double newAmount = Double.valueOf(newAmountString);
                if (newAmount < 0 || newAmount > 100000000) {
                    throw new Exception("Amount should be greater than 0 and lower than 100 million!.");
                }
                loan.setAmount(newAmount);
                updateLoan(loan);
            } catch (Exception e) {
                showErrorAlert("Invalid Input", "Amount should be greater than 0 and lower than 100 million!");
                loan.setAmount(loan.getAmount());
                loanTable.refresh();
            }
        });
    }

    /**
     * Updates the loan in the database.
     *
     * @param loan The loan to be updated.
     */
    private void updateLoan(Loan loan) {
        try {
            LoanFactory.getInstance().getILoans().edit_XML(loan, loan.getIDProduct());
        } catch (Exception e) {
            showErrorAlert("Error Updating Loan", "Error updating loan: " + e.getMessage());
        }
        loanTable.refresh();
    }

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title The title of the alert.
     * @param message The message to be displayed.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the deletion of a selected loan.
     *
     * @param event The action event triggered by the delete button.
     */
    @FXML
    private void DeleteLoan(ActionEvent event) {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            confirmAndDeleteLoan(selectedLoan);
        } else {
            showErrorAlert("No Loan Selected", "Please select a loan to delete.");
        }
    }

    /**
     * Confirms and deletes the selected loan.
     *
     * @param selectedLoan The loan to be deleted.
     */
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
                    showErrorAlert("Unexpected error.", "Please try again or contact support if the problem persists.");
                    LOGGER.severe("Error Deleting Loan: " + e.getMessage());
                }

            }
        });
    }

    @FXML
    private void nuevoPrestamo(ActionEvent event) {
        addNewLoan(event);
    }

    @FXML
    private void imprimirPrestamo(ActionEvent event) {
        Print(event);
    }

    @FXML
    private void eliminarPrestamo(ActionEvent event) {
        DeleteLoan(event);
    }

    /**
     * Adds a new loan to the database and refreshes the table.
     *
     * @param event The action event triggered by the new loan button.
     */
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
            showErrorAlert("Unexpected error.", "Please try again or contact support if the problem persists.");
            LOGGER.severe("Error adding loan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates and displays a report of all loans.
     *
     * @param event The action event triggered by the print button.
     */
    @FXML
    private void PrintLoan(ActionEvent event) {
        try {
            LOGGER.info("Beginning print action...");
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/com/tartanga/grupo4/resources/reports/LoanReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource(LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
            }));
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "Loan Report");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            showErrorAlert("Unexpected error.", "Please try again or contact support if the problem persists.");
            LOGGER.severe("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads all loans into the table.
     */
    @FXML
    private void mostrarTodosLosPrestamos() {
        try {
            ObservableList<Loan> data = FXCollections.observableArrayList(LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
            }));
            loanTable.setItems(data);
        } catch (Exception e) {
            showErrorAlert("Unexpected error.", "Please try again or contact support if the problem persists.");
            LOGGER.log(Level.SEVERE, "LoanController: Exception while retrieveing loan data from the server: ", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the search functionality based on the selected search option.
     *
     * @param event The action event triggered by the search button.
     */
    private void onSearch(ActionEvent event) {
        try {
            String selectedOption = choiceSearch.getValue();
            ObservableList<Loan> filteredLoans = FXCollections.observableArrayList();

            if (selectedOption != null) {
                switch (selectedOption) {

                    case "Search by Date":
                        LocalDate startDate = fromDate.getValue();
                        LocalDate endDate = toDate.getValue();

                        if (startDate != null && endDate != null && startDate.isBefore(endDate)) {
                            filteredLoans = FXCollections.observableArrayList(
                                    LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
                                    }).stream()
                                            .filter(loan -> {
                                                Date loanStartDate = loan.getStartDate();
                                                Date loanEndDate = loan.getEndDate();
                                                return loanStartDate != null && loanEndDate != null
                                                        && !loanStartDate.before(java.sql.Date.valueOf(startDate))
                                                        && !loanEndDate.after(java.sql.Date.valueOf(endDate));
                                            })
                                            .collect(Collectors.toList())
                            );
                        } else {
                            showErrorAlert("Invalid Date Range", "Please select a valid date range.");
                            return;
                        }
                        break;

                    case "Search by interest rate":
                        String interestRateText = tfSearch.getText();
                        if (!interestRateText.isEmpty()) {

                            try {
                                int interestRate = Integer.parseInt(interestRateText);
                                filteredLoans = FXCollections.observableArrayList(
                                        LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
                                        }).stream()
                                                .filter(loan -> loan.getInterest() == interestRate)
                                                .collect(Collectors.toList())
                                );
                            } catch (NumberFormatException e) {
                                showErrorAlert("Invalid Interest Rate", "Please enter a valid interest rate.");
                                LOGGER.log(Level.SEVERE, "Error on LoanController: Exception while searching by interest rate", e.getMessage());
                                return;
                            }
                        } else {
                            showErrorAlert("Invalid interest rate", "Please select a valid value.");
                            return;
                        }
                        break;

                    case "Search by Amount":
                        String amountText = tfSearch.getText();
                        if (!amountText.isEmpty()) {
                            try {
                                double amount = Double.parseDouble(amountText);
                                filteredLoans = FXCollections.observableArrayList(
                                        LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
                                        }).stream()
                                                .filter(loan -> loan.getAmount() == amount)
                                                .collect(Collectors.toList())
                                );
                            } catch (NumberFormatException e) {
                                showErrorAlert("Invalid Amount", "Please enter a valid amount.");
                                LOGGER.log(Level.SEVERE, "Error on LoanController: Exception while searching by amount", e.getMessage());
                                return;
                            }
                        } else {
                            showErrorAlert("Invalid Amount", "Please enter a valid amount.");
                            return;
                        }
                        break;

                    default:
                        showErrorAlert("Invalid Option", "Please select a valid search option.");
                        return;
                }
            } else {
                showErrorAlert("Not filter selected.", "Please select a filtering option.");
            }

            // Update the table with the filtered loans
            loanTable.setItems(filteredLoans);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while filtering: ", e.getMessage());
            showErrorAlert("Unexpected error.", "Please try again or contact support if the problem persists.");
            e.printStackTrace();
        }
    }

    /**
     * Generates and displays a report of the loans currently displayed in the
     * table.
     *
     * @param event The action event triggered by the print button.
     */
    @FXML
    private void Print(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass()
                    .getResourceAsStream("/com/tartanga/grupo4/resources/reports/LoanReport.jrxml"));

            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Loan>) this.loanTable.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
            LOGGER.log(Level.INFO, "Printed succesfully");
        } catch (Exception error) {
            showErrorAlert("Unexpected error printing.", "Please try again or contact support if the problem persists.");
            LOGGER.log(Level.SEVERE, "LoanController(handlePrintReport): Exception while creating the report {0}", error.getMessage());
        }
    }

    private void mostrarPorFecha() {
        tfSearch.setVisible(false);
        tfSearch.setManaged(false);
        btnSearch.setVisible(true);
        fromDate.setVisible(true);
        fromDate.setManaged(true);
        fromDate.setPromptText("Fecha desde:");
        toDate.setVisible(true);
        toDate.setManaged(true);
        toDate.setPromptText("Fecha hasta:");

    }

    private void mostrarPorTexto() {
        tfSearch.setVisible(true);
        tfSearch.setManaged(true);
        btnSearch.setVisible(true);
        fromDate.setVisible(false);
        fromDate.setManaged(false);
        toDate.setVisible(false);
        toDate.setManaged(false);
    }
}


