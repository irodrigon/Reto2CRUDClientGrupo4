/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRUDTest;

import com.tartanga.grupo4.main.Aplication;
import com.tartanga.grupo4.main.MainApplicationRovoBank;
import com.tartanga.grupo4.models.AccountBean;
import com.tartanga.grupo4.models.Currency;
import com.tartanga.grupo4.models.Transfers;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author Alin
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDTransferFailTest extends ApplicationTest {

    private TableView<Transfers> table;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Inicializar la aplicación UNA SOLA VEZ
        ApplicationTest.launch(MainApplicationRovoBank.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        table = lookup("#tbTransfer").query();
    }

    public CRUDTransferFailTest() {

    }
    
    //@Test
    public void testA_addTransfer() {
        sleep(10000);
                
        clickOn("#btnNew");
        verifyThat("Error al crear transferencias", isVisible());
        clickOn("Aceptar");
    }
    
    
    //@Test
    public void testB_modifyTransfer() {
        int lastTransfer = table.getItems().size() - 1;
        Node row = lookup(".table-row-cell").nth(lastTransfer).query();
        clickOn(row);

        Node AmountCell = lookup(".table-row-cell").nth(lastTransfer).lookup(".table-cell").nth(4).query();
        doubleClickOn(AmountCell);
        write("500");
        type(KeyCode.ENTER);
        
        verifyThat("Error al editor la cantidad", isVisible());
        clickOn("Aceptar");
    }

    @Test
    public void testC_getTransfer() {
        
        sleep(10000);
        clickOn("#fldAccount");
        write("asier barrio");
        clickOn("#btnFindAccount");
        
        verifyThat("Error al filtrar transferencias por cuenta", isVisible());
        clickOn("Aceptar");
    }

   @Test
    public void testD_deleteTransfer() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btnDelete");
        
        verifyThat("Error al eliminar transferencias", isVisible());
        clickOn("Aceptar");

    }

}
