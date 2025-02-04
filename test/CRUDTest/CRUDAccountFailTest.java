/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRUDTest;

import com.tartanga.grupo4.main.Aplication;
import com.tartanga.grupo4.models.AccountBean;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 *
 * @author rabio
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDAccountFailTest extends ApplicationTest {

    private TableView table;

    @Override
    public void start(Stage stage) throws Exception {
        new Aplication().start(stage);
        table = lookup("#tableAccounts").query();
        ObservableList<AccountBean> data = FXCollections.observableArrayList();
        data.add(new AccountBean("1234-4567-4567-4569-7896", "12/12/2015", 1536.3, 999));
        table.setItems(data);
    }

    public CRUDAccountFailTest() {

    }

    @Test
    public void testA_getAccountsByDAte() {
        clickOn("#searchButtons");
        clickOn("#itemDate");
        clickOn("#startDate");
        write("01/05/2015");
        clickOn("#endDate");
        write("31/12/2015");
        press(KeyCode.ENTER);
        //clickOn("#dateSearchButton"); no funciona bien el listener para habilitar el boton
        verifyThat("An error happen in application", isVisible());
        clickOn("Aceptar");
    }

    @Test
    public void testB_CrearCuenta() {
        int rowCount = table.getItems().size();
        clickOn("#addButton");
        verifyThat("An error happened while creating account", isVisible());
        clickOn("Aceptar");
        assertEquals(rowCount, table.getItems().size());
    }

    @Test
    public void testC_BorrarCuenta() {
        List<AccountBean> accounts = table.getItems();
        String accountNumber = accounts.get(0).getAccountNumber();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data", rowCount, 0);
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table does not have that row. ", row);
        clickOn(row);
        verifyThat("#deleteButton", isEnabled());
        clickOn("#deleteButton");
        clickOn("Yes");
        verifyThat("An error happened while deleting account", isVisible());
        clickOn("Aceptar");
        assertEquals("The account has not been created",
                accounts.stream().filter(u -> u.getAccountNumber().equals(accountNumber)).count(), 1);
    }

    @Test
    public void testD_modifyItem() {
        List<AccountBean> accounts = table.getItems();
        Double balance = accounts.get(0).getBalance();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        doubleClickOn("1536.3");
        write("5666.0");
        press(KeyCode.ENTER);
        verifyThat("An error happened while updating the account", isVisible());
        clickOn("Aceptar");
        assertEquals("The account has not been created",
                accounts.stream().filter(u -> u.getBalance() == 1536.3).count(), 1);
    }
}
