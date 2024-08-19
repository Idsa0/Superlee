package com.Superlee.Supply.Service;

import com.Superlee.Supply.Business.Contact;
import com.Superlee.Supply.Business.Supplier;
import com.Superlee.Supply.Business.Supplier.PaymentMethod;
import com.Superlee.Supply.Business.SupplierAgreement;
import com.Superlee.Supply.Business.SupplierFacade;
import com.Superlee.Supply.Service.Responses.Response;
import com.Superlee.Supply.Service.Responses.ResponseT;

import java.util.List;

public class SupplierService {
    private final SupplierFacade supplierFacade;
    private static SupplierService instance;

    public SupplierService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }
    
    
    // Supplier related functions

    // Loads data into the system for testing
    public Response loadData(){
        try {
            //supplierFacade.addSupplier("A", "0", "0000", Supplier.PaymentMethod.CASH, "Afula");
            //supplierFacade.addSupplier("B", "1", "1111", Supplier.PaymentMethod.BANK_TRANSFER, "Beer Sheva");
            //supplierFacade.addSupplier("C", "2", "2222", Supplier.PaymentMethod.CREDIT_CARD, "Haifa");
            supplierFacade.loadAllSuppliers();
            supplierFacade.loadAllContacts();
            supplierFacade.loadAllSupplierAgreements();
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Adds a new supplier to the system
    public Response addSupplier(String name, String compNumber, String bankNumber, PaymentMethod payment, String address) {
        try {
            supplierFacade.addSupplier(name, compNumber, bankNumber, payment, address);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Removes the supplier with the given id
    public Response removeSupplier(int supplierId) {
        try {
            supplierFacade.removeSupplier(supplierId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Updates the name of the supplier with the given id
    public Response updateSupplierName(int supplierId, String newName)
    {
        try {
            supplierFacade.updateSupplierName(supplierId, newName);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Updates the bank account of the supplier with the given id
    public Response updateSupplierBankAccount(int supplierId, String newBankAccount) {
        try {
            supplierFacade.updateSupplierBankAccount(supplierId, newBankAccount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Updates the payment method of the supplier with the given id
    public Response updateSupplierPaymentMethod(int supplierId, PaymentMethod newPaymentMethod) {
        try {
            supplierFacade.updateSupplierPaymentMethod(supplierId, newPaymentMethod);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Returns the supplier with the given id
    public ResponseT<Supplier> getSupplier(int supplierId) {
        try {
            Supplier supplier = supplierFacade.getSupplier(supplierId);
            return new ResponseT<>(supplier);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    // Returns a list of all suppliers
    public ResponseT<List<Supplier>> getAllSuppliers() {
        try {
            List<Supplier> suppliers = supplierFacade.getAllSuppliers();
            return new ResponseT<>(suppliers);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    // Adds a new contact to the supplier with the given id
    public Response addContact(int supplierId, String contactName, String phoneNumber) {
        try {
            supplierFacade.addContact(supplierId, contactName, phoneNumber);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Returns a list of all contacts
    public ResponseT<List<Contact>> getAllContacts() {
        try {
            List<Contact> contacts = supplierFacade.getAllContacts();
            return new ResponseT<>(contacts);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    // Supplier Agreement related functions

    // Returns the supplier agreement of the supplier with the given id
    public ResponseT<SupplierAgreement> getSupplierAgreement(int supplierId) {
        try {
            return new ResponseT<>(supplierFacade.getSupplierAgreement(supplierId));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    // Changes a product's price
    public Response updateProductPrice(int supplierId, int catalogNumber, double newPrice) {
        try {
            supplierFacade.updateProductPrice(supplierId, catalogNumber, newPrice);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Sets a new product discount according to an amount given
    public Response addProductDiscountAccordingToAmount(int supplierId, int catalogNumber, int amount, int discountPercentage) {
        try {
            supplierFacade.addProductDiscountAccordingToAmount(supplierId, catalogNumber, amount, discountPercentage);
            return new Response();
        } catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    // Updates the discount to a discount given
    public Response updateProductDiscountAccordingToAmount(int supplierId, int catalogNumber, int amount, int newDiscountPercentage) {
        try {
            supplierFacade.updateProductDiscountAccordingToAmount(supplierId, catalogNumber, amount, newDiscountPercentage);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Removes a product discount
    public Response removeProductDiscountAccordingToAmount(int supplierId, int catalogNumber, int amount) {
        try {
            supplierFacade.removeProductDiscountAccordingToAmount(supplierId, catalogNumber, amount);
            return new Response();
        }  catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Adds a new product supplied by the given supplier
    public Response addProductToSupplier(int supplierId, int catalogNumber, double price, String name) {
        try {
            supplierFacade.addProductToSupplier(supplierId, catalogNumber, price, name);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Removes a product from the supplied products for a given supplier
    public Response removeProductFromSupplier(int supplierId, int catalogNumber) {
        try {
            supplierFacade.removeProduct(supplierId, catalogNumber);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Changes a supplier product's name
    public Response updateProductName(int supplierId, int catalogNumber, String newName) {
        try {
            supplierFacade.updateProductName(supplierId, catalogNumber, newName);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public static SupplierService getInstance(SupplierFacade supplierFacade) {
        if (instance == null)
            instance = new SupplierService(supplierFacade);
        return instance;
    }



}
