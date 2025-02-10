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
public class RovoBankCreditCardControllerTestOnFail extends ApplicationTest {

    private TableView table;

    @Override
    public void start(Stage stage) throws Exception {
        new MainApplicationRovoBank().start(stage);
    }

    @Override
    public void stop() {
    }

    public RovoBankCreditCardControllerTestOnFail() {
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
        verifyThat("An error Ocurred. Contact you system administrator", isVisible());
        clickOn("Close");
        clickOn("#menuNavigation");
        clickOn("#menuItemCredit");
        verifyThat("An error Ocurred. Contact you system administrator", isVisible());
        clickOn("Close");
        sleep(500);
    }
    
    @After
    public void doLogout(){
        clickOn("#menuMyProfile");
        clickOn("#menuItemLogout");
    }

    @Test
    public void test_1_createCreditCard() {
        sleep(10000);
        clickOn("#btnAddCard");
        verifyThat("An error Ocurred. Contact you system administrator", isVisible());
        clickOn("Close");
        verifyThat("An error Ocurred. Contact you system administrator", isVisible());
        clickOn("Close");
    }
    
    @Test
    public void test_2_readCreditCard() {
        sleep(10000);
        clickOn("#btnShowAllData");
        verifyThat("An error Ocurred. Contact you system administrator", isVisible());
        clickOn("Close");
    }

    @Test
    public void test_3_updateCreditCard() {
        sleep(10000);
        table = lookup("#tableViewCreditCard").queryTableView();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data: Cannot test.", rowCount, 0);
        Node cellCardNumber = lookup(".table-row-cell").nth(table.getItems().size() - 1).lookup(".table-cell").nth(0).query();
        assertNotNull("Cell credit card number is null: table has not that cell. ", cellCardNumber);
        doubleClickOn(cellCardNumber);
        verifyThat("An error Ocurred. Contact you system administrator", isVisible());
        clickOn("Close");
    }

    @Test
    public void test_4_DeleteCreditCard() {
        sleep(10000);
        table = lookup("#tableViewCreditCard").queryTableView();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data: Cannot test.", rowCount, 0);
        Node row = lookup(".table-row-cell").nth(table.getItems().size() - 1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btnRemoveCard", isEnabled());
        clickOn("#btnRemoveCard");
        verifyThat("Confirm removing credit card:", isVisible());
        clickOn("Yes");
        verifyThat("An error Ocurred. Contact you system administrator", isVisible());
        clickOn("Close");
    }

}
