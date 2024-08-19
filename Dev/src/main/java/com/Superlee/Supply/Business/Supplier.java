package com.Superlee.Supply.Business;
import com.Superlee.Supply.DataAccess.ContactDTO;
import com.Superlee.Supply.DataAccess.SupplierAgreementDTO;
import com.Superlee.Supply.DataAccess.SupplierDTO;
import java.util.Map;

public class Supplier {
    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        CHECK,
        BANK_TRANSFER
    }


    private int supplierId;
    private String address;
    private int contactId = 0;
    private String name;
    private String compNumber;
    private String bankNumber;
    private PaymentMethod payment;
    public Contact contact;
    public SupplierAgreement supplierAgreement;
    private ContactDTO contactDTO;
    public SupplierDTO supplierDTO;
    public SupplierAgreementDTO supplierAgreementDTO;

    public Supplier(int supplierId, String name, String compNumber, String bankNumber, PaymentMethod payment, Contact contact) {
        this.supplierId = supplierId;
        this.name = name;
        this.compNumber = compNumber;
        this.bankNumber = bankNumber;
        this.payment = payment;
        this.contact = contact;
        this.contactId += 1;
        this.supplierAgreement = new SupplierAgreement(supplierId);
    }

    public Supplier(int supplierId, String name, String compNumber, String bankNumber, PaymentMethod payment, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.address = address;
        this.compNumber = compNumber;
        this.bankNumber = bankNumber;
        this.payment = payment;
        this.supplierAgreement = new SupplierAgreement(supplierId);
        this.supplierDTO = new SupplierDTO(supplierId, name, bankNumber, compNumber, payment.toString(), address);
        this.supplierAgreementDTO = new SupplierAgreementDTO(supplierId, 0, 0, "");
    }

    public Supplier(Supplier supplier) {
        this.name = supplier.getName();
        this.supplierId = supplier.getSupplierId();
        this.bankNumber = supplier.getBankNumber();
        this.compNumber = supplier.getCompNumber();
        // Distinguish betweeen fixed delivery days and demand orders?
        //this.shouldDeliver = supplier.getShouldDeliver();
        this.payment = supplier.getPayment();
        this.contact = supplier.getContact();
        this.supplierAgreement = new SupplierAgreement(supplierId);
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getCompNumber() {
        return compNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public Contact getContact() {
        return contact;
    }

    public PaymentMethod getPayment() {
        return payment;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setBankNumber(String newBankAccount) {
        this.bankNumber = newBankAccount;
    }

    public void setPayment(PaymentMethod newPaymentMethod) {
        this.payment = newPaymentMethod;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    // Adds contact to supplier
    public void addContact(String contactName, String phoneNumber) {
        this.contactDTO = new ContactDTO(contactId, supplierId, contactName, phoneNumber);
        this.contactDTO.insert();
        this.contact = new Contact(contactName, phoneNumber, contactId++);
    }

    public SupplierAgreement getSupplierAgreement() {
        return supplierAgreement;
    }

    public void updateProductPrice(int catalogNumber, double newPrice) {
        supplierAgreement.updateProductPrice(catalogNumber, newPrice);
    }

    public void addProductDiscountAccordingToAmount(int catalogNumber, int amount, int discountPercentage) {
        supplierAgreement.addProductDiscountAccordingToAmount(catalogNumber, amount, discountPercentage);
    }

    public void updateProductDiscountAccordingToAmount(int catalogNumber, int amount, int newDiscountPercentage) {
        supplierAgreement.updateProductDiscountAccordingToAmount(catalogNumber, amount, newDiscountPercentage);    
    }

    public void removeProductDiscountAccordingToAmount(int catalogNumber, int amount) {
        supplierAgreement.removeProductDiscountAccordingToAmount(catalogNumber, amount);
    }

    public void addProduct(SupplierProduct product) {
        supplierAgreement.addProduct(product);
    }

    public void removeProduct(int catalogNumber) {
        supplierAgreement.removeProduct(catalogNumber);
    }

    public void updateProductName(int catalogNumber, String newName) {
        supplierAgreement.updateProductName(catalogNumber, newName);
    }

    // HW2
    public double getProductPriceAccordingToAmount(String name, int amount){
        return supplierAgreement.getProductPriceByName(name, amount) * amount;
    }

    // HW2
    public int getProductIdByName(String name){
        return supplierAgreement.getProductIdByName(name);
    }
}
