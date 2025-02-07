package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.main.Aplication;
import com.tartanga.grupo4.models.Loan;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * Test class for verifying CRUD operations related to loans in the application.
 * It includes tests for searching, adding, deleting, printing, and modifying loans.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDLoanFailTest extends ApplicationTest {

    private TableView table;

    /**
     * Initializes the application and sets up a loan for testing.
     * This method is automatically called by TestFX to set up the application for each test.
     *
     * @param stage the primary stage for this application
     * @throws Exception if any error occurs during the setup
     */
    @Override
    public void start(Stage stage) throws Exception {
        new Aplication().start(stage);
        table = lookup("#loanTable").query();
        Loan loan = new Loan();
        loan.setLoanId(1234L);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2021-01-20");
        Date endDate = dateFormat.parse("2025-01-01");

        loan.setStartDate(startDate);
        loan.setEndDate(endDate);
        loan.setAmount(500.0);
        table.getItems().add(loan);
    }

    /**
     * Test to search for loans by date range and loan ID.
     * Verifies that an error message is shown when attempting to search for loans.
     */
    @Test
    public void testA_searchLoansByDate() {
        clickOn("#btnSearch");
        clickOn("#choiceSearch");
        write("Loan ID");
        clickOn("#tfSearch");
        write("1234");
        clickOn("#fromDate");
        write("01/01/2020");
        clickOn("#toDate");
        write("31/12/2025");
        press(KeyCode.ENTER);
        verifyThat("An error occurred while searching loans", isVisible());
        clickOn("Aceptar");
    }

    /**
     * Test to add a new loan.
     * Verifies that an error message is shown when attempting to add a new loan,
     * and ensures that the number of rows in the table remains the same.
     */
    @Test
    public void testB_addNewLoan() {
        int rowCount = table.getItems().size();
        clickOn("#btnNew");
        verifyThat("An error occurred while adding a new loan", isVisible());
        clickOn("Aceptar");
        assertEquals(rowCount, table.getItems().size());
    }

    /**
     * Test to delete an existing loan.
     * Verifies that an error message is shown when attempting to delete a loan,
     * and ensures that the loan has not been deleted from the table.
     */
    @Test
    public void testC_deleteLoan() {
        List<Loan> loans = table.getItems();
        Long loanId = loans.get(0).getLoanId();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data", rowCount, 0);
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table does not have that row.", row);
        clickOn(row);
        verifyThat("#btnDelete", isEnabled());
        clickOn("#btnDelete");
        clickOn("Yes");
        verifyThat("An error occurred while deleting loan", isVisible());
        clickOn("Aceptar");
        assertEquals("The loan has not been deleted",
                loans.stream().filter(u -> u.getLoanId().equals(loanId)).count(), 1);
    }

    /**
     * Test to print a loan.
     * Verifies that an error message is shown when attempting to print the loan.
     */
    @Test
    public void testD_printLoan() {
        clickOn("#btnPrint");
        verifyThat("An error occurred while printing the loan", isVisible());
        clickOn("Aceptar");
    }

    /**
     * Test to modify the details of an existing loan.
     * Verifies that an error message is shown when attempting to modify the loan details,
     * and ensures that the loan details have been updated correctly.
     */
    @Test
    public void testE_modifyLoanDetails() {
        List<Loan> loans = table.getItems();
        Loan loan = loans.get(0);
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        doubleClickOn("1000");
        write("1200");
        press(KeyCode.ENTER);
        verifyThat("An error occurred while updating the loan", isVisible());
        clickOn("Aceptar");

        assertEquals("The loan details have not been updated", 1200.0, loan.getAmount(), 0.01);
    }
}
