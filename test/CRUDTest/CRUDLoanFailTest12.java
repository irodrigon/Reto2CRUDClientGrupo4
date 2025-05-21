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
import org.junit.Assert;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDLoanFailTest12 extends ApplicationTest {

    private TableView<Loan> loanTable;
    private static boolean isAppStarted = false;

    @Override
    public void start(Stage stage) throws Exception {
        if (!isAppStarted) {
            new LoanWindowMain().start(stage);
            isAppStarted = true;
            sleep(5000);
            //Hay que apagar el GlashFish.
        }
        loanTable = lookup("#loanTable").queryTableView();
    }

    @Test
    public void testA_addLoan() {
        clickOn("#btnNew");
        waitForFxEvents();
        verifyThat("Please try again or contact support if the problem persists.", NodeMatchers.isVisible());
    }

    @Test
    public void testB_readLoan() {
        clickOn("Aceptar");
        clickOn("#btnShow");
        waitForFxEvents();
        verifyThat("Please try again or contact support if the problem persists.", NodeMatchers.isVisible());

    }

    @Test
    public void testC_updateLoanAmount() {
        clickOn("Aceptar");

        Node row = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).query();
        clickOn(row);
        Node totalAmountCell = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).lookup(".table-cell").nth(5).query();
        doubleClickOn(totalAmountCell);
        write("15000");
        type(KeyCode.ENTER);
        waitForFxEvents();
        verifyThat("Server error, the changes made may not be saved in the database.", NodeMatchers.isVisible());
    }

    @Test
    public void testC_updateLoanInterest() {
        clickOn("Aceptar");

        Node row = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).query();
        clickOn(row);
        Node interestCell = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).lookup(".table-cell").nth(3).query();
        doubleClickOn(interestCell);
        write("10");
        type(KeyCode.ENTER);
        waitForFxEvents();
        verifyThat("Server error, the changes made may not be saved in the database.", NodeMatchers.isVisible());
    }

    @Test
    public void testC_updateLoanPeriod() {
        clickOn("Aceptar");

        Node row = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).query();
        clickOn(row);
        Node periodCell = lookup(".table-row-cell").nth(loanTable.getItems().size() - 1).lookup(".table-cell").nth(4).query();
        doubleClickOn(periodCell);
        write("10");
        type(KeyCode.ENTER);
        waitForFxEvents();
        verifyThat("Server error, the changes made may not be saved in the database.", NodeMatchers.isVisible());
    }

    @Test
    public void testC_updateLoanDates() {
        clickOn("Aceptar");

        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        Node startingDateCell = lookup(".table-row-cell").nth(0).lookup(".table-cell").nth(1).query();
        waitForFxEvents();
        doubleClickOn(startingDateCell);
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        press(KeyCode.DELETE);
        sleep(200);
        write("20/02/2010");
        type(KeyCode.ENTER);
        clickOn(row);
        waitForFxEvents();
        verifyThat("Server error, the changes made may not be saved in the database.", NodeMatchers.isVisible());
    }

    @Test
    public void testC_updateLoanDate2() {
        clickOn("Aceptar");

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
        waitForFxEvents();
        verifyThat("Server error, the changes made may not be saved in the database.", NodeMatchers.isVisible());
    }

    @Test
    public void testD_deleteLoan() {
        clickOn("Aceptar");

        int initialRowCount = loanTable.getItems().size();

        Long lastID1 = loanTable.getItems().get(loanTable.getItems().size() - 1).getLoanId();

        Node row = lookup(".table-row-cell").nth(initialRowCount - 1).query();
        clickOn(row);
        sleep(200);
        clickOn("#btnDelete");
        sleep(200);
        clickOn("Aceptar");
        waitForFxEvents();
        verifyThat("Please try again or contact support if the problem persists.", NodeMatchers.isVisible());
    }
}
