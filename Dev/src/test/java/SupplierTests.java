import com.Superlee.Supply.Business.Contact;
import com.Superlee.Supply.Business.Supplier;
import com.Superlee.Supply.Business.SupplierAgreement;
import com.Superlee.Supply.Business.SupplierFacade;
import com.Superlee.Supply.DataAccess.DataBaseCreator;
import com.Superlee.Supply.Service.Responses.Response;
import com.Superlee.Supply.Service.Responses.ResponseT;
import com.Superlee.Supply.Service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class SupplierTests {

    private final SupplierService supplierService = new SupplierService(new SupplierFacade());

    public SupplierTests() {
    }

    @BeforeEach
    public void setUp() {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        dataBaseCreator.deleteAllTables();
        try {
            dataBaseCreator.CreateAllTables();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        supplierService.addSupplier("A", "0", "0000", Supplier.PaymentMethod.CASH, "Afula");
        supplierService.addSupplier("B", "1", "1111", Supplier.PaymentMethod.BANK_TRANSFER, "Beer Sheva");
        supplierService.addSupplier("C", "2", "2222", Supplier.PaymentMethod.CREDIT_CARD, "Haifa");
    }

    @Test
    @Order(1)
    public void testAddSupplier() {
        Response res = supplierService.addSupplier("D", "3", "3333", Supplier.PaymentMethod.CREDIT_CARD, "Jerusalem");
        ResponseT<List<Supplier>> l = supplierService.getAllSuppliers();
        assertFalse(res.errorOccurred());
        assertEquals(4, l.getValue().size());
    }

    @Test
    @Order(2)
    public void testRemoveSupplier() {
        Response res = supplierService.removeSupplier(2);
        ResponseT<List<Supplier>> l = supplierService.getAllSuppliers();
        assertFalse(res.errorOccurred());
        assertEquals(2, l.getValue().size());
    }

    @Test
    public void testGetSupplier() {
        ResponseT<Supplier> l = supplierService.getSupplier(1);
        assertEquals(1, l.getValue().getSupplierId());
    }

    @Test
    public void testAddProductToSupplier() {
        Response res1 = supplierService.addProductToSupplier(0, 0, 5, "Milk");
        Response res2 = supplierService.addProductToSupplier(0, 1, 6, "Butter");
        ResponseT<SupplierAgreement> responseAgree = supplierService.getSupplierAgreement(0);
        assertFalse(res1.errorOccurred());
        assertFalse(res2.errorOccurred());
        assertNotNull(responseAgree.getValue().getProduct(0));
        assertNotNull(responseAgree.getValue().getProduct(1));
        assertNull(responseAgree.getValue().getProduct(2));
    }

    @Test
    public void testRemoveProductFromSupplier() {
        supplierService.addProductToSupplier(0, 0, 5, "Milk");
        ResponseT<SupplierAgreement> responseAgree = supplierService.getSupplierAgreement(0);
        assertNotNull(responseAgree.getValue().getProduct(0));
        Response res = supplierService.removeProductFromSupplier(0, 0);
        ResponseT<SupplierAgreement> responseAgree2 = supplierService.getSupplierAgreement(0);
        assertFalse(res.errorOccurred());
        assertNull(responseAgree2.getValue().getProduct(0));
    }

    @Test
    public void testAddContact() {
        Response res1 = supplierService.addContact(0, "Jacob", "0501234567");
        assertFalse(res1.errorOccurred());
        ResponseT<List<Contact>> res2 = supplierService.getAllContacts();
        assertFalse(res2.errorOccurred());
        assertEquals("Jacob", res2.getValue().get(0).getName());
    }

    @Test
    public void testUpdateSupplierName() {
        Response res1 = supplierService.updateSupplierName(0, "Joe");
        assertFalse(res1.errorOccurred());
        ResponseT<Supplier> res2 = supplierService.getSupplier(0);
        assertEquals("Joe", res2.getValue().getName());
    }

    @Test
    public void testUpdateSupplierBankAccount() {
        Response res1 = supplierService.updateSupplierBankAccount(0, "0800");
        assertFalse(res1.errorOccurred());
        ResponseT<Supplier> res2 = supplierService.getSupplier(0);
        assertEquals("0800", res2.getValue().getBankNumber());
    }

    @Test
    public void testUpdateProductPrice() {
        supplierService.addProductToSupplier(0, 0, 5, "Milk");
        Response res1 = supplierService.updateProductPrice(0, 0, 4);
        assertFalse(res1.errorOccurred());
        ResponseT<Supplier> res2 = supplierService.getSupplier(0);
        assertEquals(4, res2.getValue().getSupplierAgreement().getProduct(0).getPrice());
    }

    @Test
    public void testUpdateProductName() {
        supplierService.addProductToSupplier(0, 0, 5, "Milk");
        Response res1 = supplierService.updateProductName(0, 0, "No Milk");
        assertFalse(res1.errorOccurred());
        ResponseT<Supplier> res2 = supplierService.getSupplier(0);
        assertEquals("No Milk", res2.getValue().getSupplierAgreement().getProduct(0).getName());
    }

    @Test
    public void testUpdateSupplierPaymentMethod() {
        Response res1 = supplierService.updateSupplierPaymentMethod(0, Supplier.PaymentMethod.CREDIT_CARD);
        assertFalse(res1.errorOccurred());
        ResponseT<Supplier> res2 = supplierService.getSupplier(0);
        assertEquals(Supplier.PaymentMethod.CREDIT_CARD.toString(), res2.getValue().getPayment().toString());
    }
}
