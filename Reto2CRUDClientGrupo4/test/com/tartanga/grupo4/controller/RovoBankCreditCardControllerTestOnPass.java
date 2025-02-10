/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.main.MainApplicationRovoBank;
import com.tartanga.grupo4.models.CreditCardBean;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobotInterface;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.toolkit.PrimaryStageFuture;

/**
 *
 * @author Iñi
 *
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RovoBankCreditCardControllerTestOnPass extends ApplicationTest {

    private TableView table;

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        new MainApplicationRovoBank().start(stage);
    }

    @Override
    public void stop() {
    }

    public RovoBankCreditCardControllerTestOnPass() {
    }
    
   

    
    
     @Before
    //Interaction with login window.
    public void doLogin() {
        clickOn("#userField");
        write("tartanga@eus.com");
        clickOn("#passwordField");
        write("Abcd*1234");
        clickOn("#btnSeePassword");
        clickOn("#btn_Login");
        clickOn("#menuNavigation");
        clickOn("#menuItemCredit");
        sleep(500);
    }
    
    @After
    public void doLogout(){
        clickOn("#menuMyProfile");
        clickOn("#menuItemLogout");
    }

    @Test
    public void test_1_createCreditCard() {
        table = lookup("#tableViewCreditCard").queryTableView();
        int rowCount = table.getItems().size();
        clickOn("#btnAddCard");
        assertEquals("The row has not been added!", rowCount + 1, table.getItems().size());
        table = lookup("#tableViewCreditCard").queryTableView();
        CreditCardBean lastLine = (CreditCardBean) table.getItems().get(rowCount);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        assertNotNull("The data inside the last row added is null.", lastLine);
        assertEquals("The date does not equals the date of creation of the credit card.", lastLine.getCreationDate(), format.format(LocalDate.now()));
        assertEquals("The date does not equals the date of expiration of the credit card.", lastLine.getExpirationDate(), format.format(LocalDate.now().plusYears(5)));
    }

    @Test
    public void test_2_readCreditCard() {
        //Interaction with credit card window.
        rightClickOn("#tableViewCreditCard");
        sleep(500);
        //Clears the table.
        clickOn("#contextClear");
        sleep(500);
        table = lookup("#tableViewCreditCard").queryTableView();
        int rowCountWithoutData = table.getItems().size();
        clickOn("#btnShowAllData");
        sleep(500);
        table = lookup("#tableViewCreditCard").queryTableView();
        int rowCountWithData = table.getItems().size();
        assertNotEquals("The table does not contain rows.", rowCountWithoutData, rowCountWithData);
        List<CreditCardBean> creditCardList = table.getItems();
        creditCardList.stream().forEach(creditCardBean -> assertNotNull("The data inside the rows is null.", creditCardBean));
    }

    @Test
    public void test_3_updateCreditCard() {
        table = lookup("#tableViewCreditCard").queryTableView();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data: Cannot test.", rowCount, 0);
        Node cellCardNumber = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(0).query();
        assertNotNull("Cell credit card number is null: table has not that cell. ", cellCardNumber);
        doubleClickOn(cellCardNumber);
        String originalCardNumber = ((TextFieldTableCell) cellCardNumber).getText();
        Random random = new Random();
        StringBuilder sbccn = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sbccn.append(random.nextInt(10));
        }
        String random16DigitNumber = sbccn.toString();
        write(random16DigitNumber);
        type(KeyCode.ENTER);
        verifyThat("Confirm editing credit card number:", isVisible());
        clickOn("Yes");
        sleep(500);
        cellCardNumber = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(0).query();
        String newCardNumber = ((TextFieldTableCell) cellCardNumber).getText();
        assertNotNull("Credit card number is null.", newCardNumber);
        assertNotEquals("The credit card number entered is the same as the original credit card number.", originalCardNumber, newCardNumber);
        LocalDate randomDate = LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(LocalDate.of(1969, 10, 10).toEpochDay(), LocalDate.now().toEpochDay()));
        Node cellCreationDate = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(1).query();
        assertNotNull("Cell is null: table has not that cell. ", cellCreationDate);
        doubleClickOn(cellCreationDate);
        String originalCreationDate = ((TextFieldTableCell) cellCreationDate).getText();
        write(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(randomDate));
        type(KeyCode.ENTER);
        verifyThat("Confirm editing creation date:", isVisible());
        clickOn("Yes");
        sleep(500);
        cellCreationDate = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(1).query();
        String newCreationDate = ((TextFieldTableCell) cellCreationDate).getText();
        assertNotNull("New creation date is null.", newCreationDate);
        assertNotEquals("The creation date entered is the same as the original creation date.", originalCreationDate, newCreationDate);
        randomDate = LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(LocalDate.now().toEpochDay(), LocalDate.of(2075, 10, 10).toEpochDay()));
        Node cellExpirationDate = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(2).query();
        assertNotNull("Cell is null: table has not that cell. ", cellExpirationDate);
        doubleClickOn(cellExpirationDate);
        String originalExpirationDate = ((TextFieldTableCell) cellExpirationDate).getText();
        write(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(randomDate));
        type(KeyCode.ENTER);
        verifyThat("Confirm editing expiration date:", isVisible());
        clickOn("Yes");
        sleep(500);
        cellExpirationDate = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(2).query();
        String newExpirationDate = ((TextFieldTableCell) cellExpirationDate).getText();
        assertNotNull("New expiration date is null.", newExpirationDate);
        assertNotEquals("The expiration date entered is the same as the original expiration date.", originalExpirationDate, newExpirationDate);
        Node cellCvv = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(3).query();
        assertNotNull("Cell CVV is null: table has not that cell. ", cellCvv);
        doubleClickOn(cellCvv);
        String originalCvv = ((TextFieldTableCell) cellCvv).getText();
        StringBuilder sbcvv = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            sbcvv.append(random.nextInt(10));
        }
        String random3DigitNumber = sbcvv.toString();
        write(random3DigitNumber);
        type(KeyCode.ENTER);
        verifyThat("Confirm editing CVV:", isVisible());
        clickOn("Yes");
        sleep(500);
        cellCvv = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(3).query();
        String newCvv = ((TextFieldTableCell) cellCvv).getText();
        assertNotNull("CVV is null.", newCardNumber);
        assertNotEquals("The CVV entered is the same as the original CVV.", originalCvv, newCvv);
        Node cellPin = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(4).query();
        assertNotNull("Cell PIN is null: table has not that cell. ", cellPin);
        doubleClickOn(cellPin);
        String originalPin = ((TextFieldTableCell) cellPin).getText();
        StringBuilder sbpin = new StringBuilder(3);
        for (int i = 0; i < 4; i++) {
            sbpin.append(random.nextInt(10));
        }
        String random4DigitNumber = sbpin.toString();
        write(random4DigitNumber);
        type(KeyCode.ENTER);
        verifyThat("Confirm editing PIN:", isVisible());
        clickOn("Yes");
        sleep(500);
        cellPin = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(4).query();
        String newPin = ((TextFieldTableCell) cellPin).getText();
        assertNotNull("PIN is null.", newPin);
        assertNotEquals("The PIN entered is the same as the original CVV.", originalPin, newPin);
        Node cellAccountNumber = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(5).query();
        doubleClickOn(cellAccountNumber);
        verifyThat("Confirm changing the account number:", isVisible());
        clickOn("Yes");
        ChoiceBox choice = from(cellAccountNumber).lookup(".choice-box").query();
        assertNotNull("Choice box is null.", choice);
        clickOn(choice);
        sleep(500);
        String originalAccountNumber = ((ChoiceBoxTableCell) cellAccountNumber).getText();
        sleep(1000);
        type(KeyCode.DOWN);
        sleep(500);
        type(KeyCode.DOWN);
        sleep(500);
        type(KeyCode.DOWN);
        sleep(500);
        type(KeyCode.ENTER);
        sleep(500);
        verifyThat("Confirm editing account number:", isVisible());
        clickOn("Yes");
        sleep(500);
        cellAccountNumber = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(5).query();
        String newAccountNumber = ((ChoiceBoxTableCell) cellAccountNumber).getText();
        assertNotNull("Account number is null.", newAccountNumber);
        assertNotEquals("The account number entered is the same as the original account number.", originalAccountNumber, newAccountNumber);
        table = lookup("#tableViewCreditCard").queryTableView();
        CreditCardBean lastLine = (CreditCardBean) table.getItems().get(rowCount - 1);
        assertEquals("The credit card number is not the same as entered.", newCardNumber, lastLine.getCreditCardNumber());
        assertEquals("The creation date is not the same as entered.", newCreationDate, lastLine.getCreationDate());
        assertEquals("The expiration date is not the same as entered.", newExpirationDate, lastLine.getExpirationDate());
        assertEquals("The CVV is not the same as entered.", newCvv, lastLine.getCvv());
        assertEquals("The PIN is not the same as entered.", newPin, lastLine.getPin());
        assertEquals("The AccountNumber is not the same as entered.", newAccountNumber, lastLine.getAccountNumber());
    }

    @Test
    public void test_4_DeleteCreditCard() {
        table = lookup("#tableViewCreditCard").queryTableView();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data: Cannot test.", rowCount, 0);
        Node row = lookup(".table-row-cell").nth(table.getItems().size() - 1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        table = lookup("#tableViewCreditCard").queryTableView();
        CreditCardBean lastLine = (CreditCardBean) table.getItems().get(rowCount - 1);
        clickOn(row);
        verifyThat("#btnRemoveCard", isEnabled());
        clickOn("#btnRemoveCard");
        verifyThat("Confirm removing credit card:", isVisible());
        clickOn("Yes");
        assertEquals("The row has not been deleted!", rowCount - 1, table.getItems().size());
        table = lookup("#tableViewCreditCard").queryTableView();
        assertFalse("The item has not been deleted", table.getItems().contains(lastLine));
    }

}
