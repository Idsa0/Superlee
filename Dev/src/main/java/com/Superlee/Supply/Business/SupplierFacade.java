package com.Superlee.Supply.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Superlee.Supply.Business.Supplier.PaymentMethod;
import com.Superlee.Supply.DataAccess.*;

public class SupplierFacade {

    // <supplierId, Supplier>
    private Map<Integer, Supplier> suppliers = new HashMap<>();
    private int id = 0;

    public Supplier getSupplier(int supplierId) {
        return suppliers.get(supplierId);
    }

    public void addSupplier(String name, String compNumber, String bankNumber, PaymentMethod payment, String address) {
        Supplier supplier = new Supplier(id, name, compNumber, bankNumber, payment, address);
        supplier.supplierDTO.insert();
        suppliers.put(id, supplier);
        this.id++;
    }

    public void removeSupplier(int supplierId) {
        if (!suppliers.containsKey(supplierId))
            throw new IllegalArgumentException("Supplier does not exist");
        Supplier supplier = suppliers.get(supplierId);
        suppliers.remove(supplierId);
        supplier.supplierDTO.delete();
    }

    public void updateSupplierName(int supplierId, String newName) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.setName(newName);
        supplier.supplierDTO.setName(newName);
    }

    public void updateSupplierBankAccount(int supplierId, String newBankAccount) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.setBankNumber(newBankAccount);
        supplier.supplierDTO.setBankNumber(newBankAccount);
    }

    public void updateSupplierPaymentMethod(int supplierId, PaymentMethod newPaymentMethod) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.setPayment(newPaymentMethod);
        supplier.supplierDTO.setPayment(newPaymentMethod.toString());
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliersList = new ArrayList<>();
        for (Supplier supplier : suppliers.values()) {
            suppliersList.add(supplier);
        }
        return suppliersList;
    }

    // Adds contact to supplier
    public void addContact(int supplierId, String contactName, String phoneNumber) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.addContact(contactName, phoneNumber);
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        for (Supplier supplier : suppliers.values()) {
            contacts.add(supplier.getContact());
        }
        return contacts;
    }

    public void loadAllContacts() {
        ContactDAO contactDAO = new ContactDAO();
        List<ContactDTO> contacts = contactDAO.loadAllContacts();
        for (ContactDTO contact : contacts) {
            Supplier supplier = suppliers.get(contact.getSupplierId());
            if (supplier != null)
                supplier.contact = new Contact(contact.getName(), contact.getPhoneNumber(), contact.getContactId());
        }
    }

    public void loadAllSuppliers() {
        SupplierDAO supplierDAO = new SupplierDAO();
        List<SupplierDTO> suppliers = supplierDAO.loadAllSuppliers();
        for (SupplierDTO supplier : suppliers) {
            Supplier newSupplier = new Supplier(supplier.getSupplierId(), supplier.getName(), supplier.getCompNumber(),
                    supplier.getBankNumber(), PaymentMethod.valueOf(supplier.getPayment()), supplier.getAddress());
            this.id++;
            // check if the newSupplier already exists in this.suppliers
            if (!this.suppliers.containsKey(supplier.getSupplierId()))
                this.suppliers.put(supplier.getSupplierId(), newSupplier);
        }
    }

    public SupplierAgreement getSupplierAgreement(int supplierId) {
        return suppliers.get(supplierId).supplierAgreement;
    }

    public void updateProductPrice(int supplierId, int catalogNumber, double newPrice) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, catalogNumber, newPrice, "");
        supplier.supplierAgreementDTO.setPrice(newPrice);
        supplier.updateProductPrice(catalogNumber, newPrice);
    }

    public void addProductDiscountAccordingToAmount(int supplierId, int catalogNumber, int amount, int discountPercentage) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, catalogNumber, amount, discountPercentage);
        supplier.supplierAgreementDTO.insertSupplierProductAccordingTOAmount();
        supplier.addProductDiscountAccordingToAmount(catalogNumber, amount, discountPercentage);
    }

    public void updateProductDiscountAccordingToAmount(int supplierId, int catalogNumber, int amount, int newDiscountPercentage) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, catalogNumber, amount, newDiscountPercentage);
        supplier.supplierAgreementDTO.setDiscounts(newDiscountPercentage);
        supplier.updateProductDiscountAccordingToAmount(catalogNumber, amount, newDiscountPercentage);
    }

    public void removeProductDiscountAccordingToAmount(int supplierId, int catalogNumber, int amount) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, catalogNumber, amount, 0);
        supplier.supplierAgreementDTO.deleteProductDiscountAccordingToAmount(supplierId, catalogNumber);
        suppliers.get(supplierId).removeProductDiscountAccordingToAmount(catalogNumber, amount);    
    }

    public void addProductToSupplier(int supplierId, int catalogNumber, double price, String name) {
        SupplierProduct product = new SupplierProduct(supplierId, catalogNumber, price, name);
        Supplier supplier = suppliers.get(supplierId);
        supplier.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, catalogNumber, price, name);
        supplier.supplierAgreementDTO.insertSupplierItem();
        supplier.addProduct(product);
    }

    public void removeProduct(int supplierId, int catalogNumber) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, catalogNumber, 0, 0);
        supplier.supplierAgreementDTO.delete();
        supplier.removeProduct(catalogNumber);
    }

    public void updateProductName(int supplierId, int catalogNumber, String newName) {
        Supplier supplier = suppliers.get(supplierId);
        supplier.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, catalogNumber, 0, 0);
        supplier.supplierAgreementDTO.setName(newName);
        supplier.updateProductName(catalogNumber, newName);
    }

    public void loadAllSupplierAgreements() {
        SupplierAgreementDAO supplierAgreementDAO = new SupplierAgreementDAO();
        List<SupplierAgreementDTO> supplierAgreements = supplierAgreementDAO.loadAllSupplierAgreements();
        List<SupplierAgreementDTO> supplierDiscounts = supplierAgreementDAO.loadAllSupplierAgreementsDiscounts();
        for (SupplierAgreementDTO supplierAgreement : supplierAgreements) {
            Supplier supplier = suppliers.get(supplierAgreement.getSupplierId());
            if (supplier != null) {
                if (supplier.supplierAgreement.products.isEmpty())
                    supplier.supplierAgreement = new SupplierAgreement(supplierAgreement.getSupplierId());
                supplier.supplierAgreement.addProduct(new SupplierProduct(supplierAgreement.getSupplierId(), supplierAgreement.getCatalogNumber(), supplierAgreement.getPrice(), supplierAgreement.getName()));
            }
        }
        for (SupplierAgreementDTO supplierDiscount : supplierDiscounts) {
            Supplier supplier = suppliers.get(supplierDiscount.getSupplierId());
            if (supplier != null) {
                supplier.supplierAgreement.addProductDiscountAccordingToAmount(supplierDiscount.getCatalogNumber(),
                        supplierDiscount.getAmount(), supplierDiscount.getDiscounts());
            }
        }
    }

    // HW2
    public int[] pickCheapestOption(String name, int amount){
        double bestPrice = Double.MAX_VALUE;
        int cheapestProductId = -1;
        int supplierId = -1;
        for (Map.Entry<Integer,Supplier> entry : suppliers.entrySet()){
            Supplier supplier = entry.getValue();
            double price = supplier.getProductPriceAccordingToAmount(name, amount);
            if (price > 0 && bestPrice > price){
                bestPrice = price;
                cheapestProductId = supplier.getProductIdByName(name);
                supplierId = supplier.getSupplierId();
            }
        }
        return new int[]{cheapestProductId, supplierId};
    }

}
