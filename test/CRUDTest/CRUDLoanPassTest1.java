package CRUDTest;

import com.tartanga.grupo4.main.LoanWindowMain;
import com.tartanga.grupo4.models.Loan;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDLoanPassTest1 extends ApplicationTest {

    private TableView<Loan> loanTable;
    private static boolean isAppStarted = false;

    @Override
    public void start(Stage stage) throws Exception {
        if (!isAppStarted) {
            new LoanWindowMain().start(stage);
            isAppStarted = true;
        }
        loanTable = lookup("#loanTable").queryTableView();
    }

    @Test
    public void testA_addLoan() {
        clickOn("#btnNew");
        waitForFxEvents();

        int initialRowCount = loanTable.getItems().size();
        Integer lastID = loanTable.getItems().get(initialRowCount - 1).getIDProduct();

        Loan lastLoan = loanTable.getItems().get(loanTable.getItems().size() - 1);

        LocalDate today = LocalDate.now();

        LocalDate startDate = lastLoan.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = lastLoan.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(lastID.longValue(), lastLoan.getLoanId(), 0.01);
        assertEquals(0.0, lastLoan.getAmount(), 0.01);
        assertEquals(0, lastLoan.getInterest(), 0.01);
        assertEquals(30, lastLoan.getPeriod(), 0.01);
        assertEquals(today, startDate);
        assertEquals(today, endDate);
    }

    @Test
    public void testB_searchLoan() {
        List<Loan> loans = loanTable.getItems();

        assertTrue(loans.stream().allMatch(loan -> loan instanceof Loan));

    }

    @Test
    public void testC_updateLoanAmount() {
        Node row = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).query();
        clickOn(row);
        Node totalAmountCell = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).lookup(".table-cell").nth(5).query();
        doubleClickOn(totalAmountCell);
        write("15000");
        type(KeyCode.ENTER);
        Loan updatedLoan = loanTable.getItems().get(loanTable.getItems().size() - 1);
        assertEquals(15000, updatedLoan.getAmount(), 0.01);
    }

    @Test
    public void testC_updateLoanInterest() {
        Node row = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).query();
        clickOn(row);
        Node interestCell = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).lookup(".table-cell").nth(3).query();
        doubleClickOn(interestCell);
        write("10");
        type(KeyCode.ENTER);
        Loan updatedLoan = loanTable.getItems().get(loanTable.getItems().size() - 1);
        assertEquals(10, updatedLoan.getInterest(), 0.01);
    }

    @Test
    public void testC_updateLoanPeriod() {
        Node row = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).query();
        clickOn(row);
        Node periodCell = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).lookup(".table-cell").nth(4).query();
        doubleClickOn(periodCell);
        write("10");
        type(KeyCode.ENTER);
        Loan updatedLoan = loanTable.getItems().get(loanTable.getItems().size() - 1);
        assertEquals(10, updatedLoan.getPeriod(), 0.01);
    }

    @Test
    public void testC_updateLoanDates() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        Node startingDateCell = lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();

        // Ensure the Node is visible
        waitForFxEvents();
        doubleClickOn(startingDateCell);

        // Select all text in the cell before deleting it
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);  // For Windows, use COMMAND on macOS

        // Delete the selected content
        press(KeyCode.DELETE);
        sleep(200); // Wait for the text to be deleted

        // Now write the new date
        write("20/02/2010");
        type(KeyCode.ENTER);

        clickOn(row);
        Loan updatedLoan = loanTable.getItems().get(0);

        // Convert the actual date to LocalDate for comparison
        LocalDate actualStartDate = updatedLoan.getStartDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Compare only the date (without the time part)
        assertEquals(LocalDate.of(2010, 2, 20), actualStartDate);
    }

    @Test
    public void testC_updateLoanDate2() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        Node startingDateCell = lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(2).query();

        waitForFxEvents();
        doubleClickOn(startingDateCell);
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);

        press(KeyCode.DELETE);
        sleep(200);

        write("20/02/2028");
        type(KeyCode.ENTER);

        clickOn(row);
        Loan updatedLoan = loanTable.getItems().get(0);

        // Convert the actual date to LocalDate for comparison
        LocalDate actualEndDate = updatedLoan.getEndDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Compare only the date (without the time part)
        assertEquals(LocalDate.of(2028, 2, 20), actualEndDate);
    }

    @Test
    public void testD_deleteLoan() {
        int initialRowCount = loanTable.getItems().size();

        Long lastID1 = loanTable.getItems().get(loanTable.getItems().size() - 1).getLoanId();

        Node row = lookup(".table-row-cell").nth(initialRowCount - 1).query();
        clickOn(row);
        sleep(200);
        clickOn("#btnDelete");
        sleep(200);
        clickOn("Aceptar");
        sleep(200);

        Long lastID2 = loanTable.getItems().get(loanTable.getItems().size() - 1).getLoanId();

        assertNotEquals(lastID1, lastID2);
    }
}
