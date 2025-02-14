/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRUDTest;

/**
 *
 * @author Alin
 */
import com.tartanga.grupo4.main.MainApplicationRovoBank;
import com.tartanga.grupo4.main.MainApplicationRovoBank;
import com.tartanga.grupo4.models.Transfers;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDTransferPassTest extends ApplicationTest {

    private TableView<Transfers> table;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Inicializar la aplicación UNA SOLA VEZ
        ApplicationTest.launch(MainApplicationRovoBank.class);
    }

    @Override
    public void start(Stage stage) {
        table = lookup("#tbTransfer").query();
    }

    @Test
    public void testA_addTransfer() {
        Transfers transfer = new Transfers();
        clickOn("#btnNew");
        Transfers newTransfer = table.getItems().get(table.getItems().size() - 1);

        assertTrue("La nueva transferencia no está en la tabla.", table.getItems().contains(newTransfer));
    }

    @Test
    public void testB_modifyTransfer() {
        // Seleccionar la última fila de la tabla
        int lastTransfer = table.getItems().size() - 1;
        Node row = lookup(".table-row-cell").nth(lastTransfer).query();
        clickOn(row);

        Node AmountCell = lookup(".table-row-cell").nth(lastTransfer).lookup(".table-cell").nth(4).query();
        doubleClickOn(AmountCell);
        write("500");
        type(KeyCode.ENTER);

        Node Sender = lookup(".table-row-cell").nth(lastTransfer).lookup(".table-cell").nth(1).query();
        doubleClickOn(Sender);
        write("Manuel roto");
        type(KeyCode.ENTER);

        Node Reciever = lookup(".table-row-cell").nth(lastTransfer).lookup(".table-cell").nth(2).query();
        doubleClickOn(Reciever);
        write("Odoo bombo");
        type(KeyCode.ENTER);

        Node Date = lookup(".table-row-cell").nth(lastTransfer).lookup(".table-cell").nth(3).query();
        doubleClickOn(Date);
        write("10/09/2004");
        type(KeyCode.ENTER);

        Transfers update = table.getItems().get(lastTransfer);
        assertEquals("La cantidad no se actualizó correctamente", 500, update.getAmount(), 0.0);
        assertEquals("El remitente no se actualizó correctamente", "Manuel roto", update.getSender());
        assertEquals("El receptor no se actualizó correctamente", "Odoo bombo", update.getReciever());
        
        Transfers updatedTransfer = table.getItems().get(lastTransfer);
        Date transferDate = updatedTransfer.getTransferDate();
        
        // Formatear la fecha en el formato esperado "dd/MM/yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(transferDate);

        assertEquals("La fecha no se actualizó correctamente", "10/09/2004", formattedDate);
    }

    @Test
    public void testC_getTransfer() {
        // Obtener la lista de transferencias y el último transferId
        List<Transfers> transfers = table.getItems();
        if (transfers.isEmpty()) {
            fail("No hay transferencias en la tabla.");
        }

        String lastTransferId = transfers.get(transfers.size() - 1).getTransferId().toString();

        clickOn("#cmbAccount");
        clickOn("Id");
        clickOn("#fldAccount");
        write(lastTransferId);
        clickOn("#btnFindAccount");

        assertTrue("La tabla contiene elementos que no son del tipo Transfers.",
                table.getItems().stream().allMatch(t -> t instanceof Transfers));
    }

    @Test
    public void testD_deleteTransfer() {
        List<Transfers> transfers = table.getItems();
        if (transfers.isEmpty()) {
            fail("No hay transferencias en la tabla.");
        }
        String lastTransferId = transfers.get(transfers.size() - 1).getTransferId().toString();
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        verifyThat("#btnDelete", isEnabled());
        clickOn("#btnDelete");

        boolean exists = table.getItems().stream()
                .anyMatch(t -> t.getTransferId().toString().equals(lastTransferId));

        assertFalse("La transferencia aún está en la tabla después de la búsqueda.", exists);
    }
}
