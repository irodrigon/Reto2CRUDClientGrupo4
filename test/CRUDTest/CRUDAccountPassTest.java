/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRUDTest;

import com.tartanga.grupo4.main.Aplication;
import com.tartanga.grupo4.models.AccountBean;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;

/**
 *
 * @author rabio
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDAccountPassTest extends ApplicationTest {

    private TableView table;

    @Override
    public void start(Stage stage) throws Exception {
        new Aplication().start(stage);
        table = lookup("#tableAccounts").query();
    }

    public CRUDAccountPassTest() {

    }

    @Test
    public void testA_addAccount() {
        int account0 = 0;
        List<AccountBean> accounts = table.getItems();
        String accountNumber;
        int rowCount = table.getItems().size();
        for (AccountBean account : accounts) {
            if (account.getBalance() == 0.0) {
                account0 += 1;
            }
        }
        //Miro que haya una cuenta mas que las que habia antes
        clickOn("#addButton");
        accounts = table.getItems();
        accountNumber = accounts.get(accounts.size()-1).getAccountNumber();
        assertEquals(rowCount + 1, table.getItems().size());
        //Miro que hay una cuenta mas con un balance de 0.0 de las que ya habia
        assertEquals("The account has not been created",
                accounts.stream().filter(u -> u.getBalance() == 0.0).count(), account0 + 1);
        assertEquals("The account has not been created",
                accounts.stream().filter(u -> u.getAccountNumber().equals(accountNumber)).count(), 1);
    }

    @Test
    public void testB_modifyItem() {
        clickOn("0.0");
        //Recoger cuenta seleccionada
        AccountBean account = (AccountBean) table.getSelectionModel().getSelectedItem();
        //Asgurarnos que el valor es 0.0       
        assertEquals(0.0, account.getBalance(), 0.0001);
        doubleClickOn("0.0");
        write("5666.0");
        press(KeyCode.ENTER);
        //Recoger la misma cuenta despues haber sido modifica
        account = (AccountBean) table.getSelectionModel().getSelectedItem();
        //Comparar que el nuevo valor a sido introducido correctamente
        assertEquals(5666.0, account.getBalance(), 0.0001);
    }

    @Test
    public void testC_getAccount() {
        List<AccountBean> accounts = table.getItems();
        //conseguir la primera cuenta de la lista
        String accountNumber = accounts.get(0).getAccountNumber();
        //introducir y buscar la cuenta en al base de datos
        clickOn("#searchButtons");
        clickOn("#itemAccountNum");
        clickOn("#accountNumber");
        write(accountNumber);
        clickOn("#accountNumberSearchButton");
        //Comprobar que la unica cuenta encontrada en la base de datos y que
        //aparece en la tabla sea la que hemos introducido en el buscador
        assertEquals("The account is not in the table",
                accounts.stream().filter(u -> u.getAccountNumber().equals(accountNumber)).count(), 1);
        clickOn("#searchButtons");
        clickOn("#itemAll");
    }

    @Test
    public void testD_deleteAccount() {
        List<AccountBean> accounts = table.getItems();
        Node row = lookup(".table-row-cell").nth(0).query();
        //pinchamos en la primera linea disponible y conseguimos su numero de cuenta
        clickOn(row);
        AccountBean account = (AccountBean) table.getSelectionModel().getSelectedItem();
        String accountNumber = account.getAccountNumber();
        //borramos la cuenta
        verifyThat("#deleteButton", isEnabled());
        clickOn("#deleteButton");
        clickOn("Yes");
        //verificamos que la el numero de cuenta de la cuenta borrada no este en la tabla
        assertEquals("The account has not been deleted",
                accounts.stream().filter(u -> u.getAccountNumber().equals(accountNumber)).count(), 0);
    }

}
