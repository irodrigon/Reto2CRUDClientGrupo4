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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class LoanController {

    private static Logger LOGGER = Logger.getLogger(LoanController.class.getName());
// Declaración de elementos FXML
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

    @FXML
    private void initialize() {
        loanTable.setEditable(true);
        choiceSearch.getItems().addAll("Search by ID", "Search by Date", "Search by interest rate", "Search by Amount");
        // Configuración de las columnas
        tcLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        tcStartingDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tcEndingDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tcTotalAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcRemainingAmount.setCellValueFactory(new PropertyValueFactory<>("rAmount"));
        tcInterestRate.setCellValueFactory(new PropertyValueFactory<>("interest"));
        tcPeriod.setCellValueFactory(new PropertyValueFactory<>("period"));

        btnSearch.setOnAction(this::onSearch);

        tcTotalAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
            private Double previousAmount; // Cambiado a Double para coincidir con el tipo de la columna

            @Override
            public Double fromString(String value) {
                try {
                    previousAmount = Double.valueOf(value); // Guarda el valor válido antes de la edición
                    return previousAmount;
                } catch (NumberFormatException e) {
                    LOGGER.severe("Entrada inválida: " + value);
                    return previousAmount; // Devuelve el último valor válido
                }
            }
        }));

        tcInterestRate.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            private Integer previousValue;

            @Override
            public Integer fromString(String value) {
                try {
                    previousValue = Integer.valueOf(value);
                    return previousValue;
                } catch (NumberFormatException e) {
                    LOGGER.severe("Entrada inválida: " + value);
                    return previousValue;
                }
            }
        }));

        tcPeriod.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            private Integer previousValue;

            @Override
            public Integer fromString(String value) {
                try {
                    previousValue = Integer.valueOf(value);
                    return previousValue;
                } catch (NumberFormatException e) {
                    LOGGER.severe("Entrada inválida: " + value);
                    return previousValue;
                }
            }
        }));

        // Configuración para las celdas de fecha
        tcStartingDate.setCellFactory(column -> new LoanCellDatePicker());
        tcStartingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date oldStartDate = loan.getStartDate();  // Guardamos el valor anterior
            try {
                loan.setStartDate(event.getNewValue());
                if (loan.getStartDate() == null || loan.getEndDate() == null || loan.getStartDate().after(loan.getEndDate())) {
                    throw new Exception("Start date cannot be later than end date.");
                }
                updateLoan(loan);
            } catch (Exception e) {
                showErrorAlert("Invalid Date", "Start date cannot be later than end date.");
                loan.setStartDate(oldStartDate);  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });

        tcEndingDate.setCellFactory(column -> new LoanCellDatePicker());
        tcEndingDate.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Date oldEndDate = loan.getEndDate();  // Guardamos el valor anterior

            loan.setEndDate(event.getNewValue());
            try {

                if (loan.getStartDate() == null || loan.getEndDate() == null || loan.getStartDate().after(loan.getEndDate())) {
                    throw new Exception("Start date cannot be later than end date.");
                }
                updateLoan(loan);
            } catch (Exception e) {
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
        tcInterestRate.setOnEditCommit((TableColumn.CellEditEvent<Loan, Integer> event) -> {
            Loan loan = event.getRowValue();
            Integer oldInterestRate = loan.getInterest();  // Guardamos el valor anterior
            try {
                String newInterestRateString = event.getNewValue().toString();
                Integer newInterestRate = Integer.valueOf(newInterestRateString);
                if (newInterestRate < 0 || newInterestRate > 50) {
                    throw new Exception("Interest rate should be greater than 0 and lower than 50!.");
                }
                loan.setInterest(newInterestRate);
                updateLoan(loan);
            } catch (Exception e) {
                showErrorAlert("Invalid Input", "The interest rate should be an integer greater than 0 and less than 50!");
                loan.setInterest(oldInterestRate);  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });

        tcPeriod.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Integer oldPeriod = loan.getPeriod();  // Guardamos el valor anterior
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
                loan.setPeriod(oldPeriod);  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });

        tcTotalAmount.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();

            try {
                Double newAmountString = event.getNewValue();
                Double newAmount = Double.valueOf(newAmountString);
                if (newAmount < 0 || newAmount > 100000000) {
                    throw new Exception("Amount should be greater than 0 and lower than 100 milion!.");
                }
                loan.setAmount(newAmount);
                updateLoan(loan);
            } catch (Exception e) {
                showErrorAlert("Invalid Input", "(\"Amount should be greater than 0 and lower than 100 milion!");
                loan.setAmount(loan.getAmount());  // Restauramos el valor anterior
                loanTable.refresh();
            }
        });
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
            LOGGER.severe("Error adding loan: " + e.getMessage());
            e.printStackTrace();

        }
    }

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
            LOGGER.severe("Error generating report: " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void mostrarTodosLosPrestamos() {
        ObservableList<Loan> data = FXCollections.observableArrayList(LoanFactory.getInstance().getILoans().findAll_XML(new GenericType<List<Loan>>() {
        }));
        loanTable.setItems(data);
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String selectedOption = choiceSearch.getValue();
        ObservableList<Loan> filteredLoans = FXCollections.observableArrayList();

        if (selectedOption != null) {
            // Filtrar por ID
            if (selectedOption.equals("Search by ID")) {
                String loanIdText = tfSearch.getText().trim();
                if (!loanIdText.isEmpty()) {
                    try {
                        // Intentar convertir el texto a un valor Integer
                        Integer loanId = Integer.parseInt(loanIdText);
                        Loan loan = LoanFactory.getInstance().getILoans().find_XML(new GenericType<List<Loan>>() {
                        }, loanId).stream().findFirst().orElse(null);
                        if (loan != null) {
                            filteredLoans.add(loan);
                        } else {
                            showErrorAlert("No Loan Found", "No loan found with the given ID.");
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Invalid ID", "Please enter a valid loan ID.");
                        return;
                    }
                }

            } // Filtrar por fecha
            else if (selectedOption.equals("Search by Date")) {
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
            } // Filtrar por tasa de interés
            else if (selectedOption.equals("Search by interest rate")) {
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
                        return;
                    }
                }
            } // Filtrar por cantidad
            else if (selectedOption.equals("Search by Amount")) {
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
                        return;
                    }
                }
            }
        }

        // Actualizar la tabla con los préstamos filtrados
        loanTable.setItems(filteredLoans);
    }

    @FXML
    private void Print(ActionEvent event) {
        try {
            JasperReport report
                    = JasperCompileManager.compileReport(getClass()
                            .getResourceAsStream("/com/tartanga/grupo4/resources/reports/LoanReport.jrxml"));

            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<Loan>) this.loanTable.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "AccountController(handlePrintReport): Exception while creating the report {0}", error.getMessage());
        }
    }

}
