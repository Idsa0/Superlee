package com.Superlee.Supply.DataAccess;

import com.Superlee.Supply.Business.Supplier;

public class SupplierDTO {
    private final String SupplierIdColumnName="SupplierId";
    private final String NameColumnName="Name";
    private final String BankNumberColumnName="BankNumber";
    private final String PaymentMethodColumnName="PaymentMethod";
    private final String AddressColumnName="Address";
    private final String CompNumberColumnName="CompNumber";
    private SupplierDAO supplierDAO;
    private int supplierId;
    private String name;
    private String bankNumber;
    private String compNumber;
    private String payment;
    private String address;

    public SupplierDTO(int supplierId, String name, String bankNumber, String compNumber, String payment, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.bankNumber = bankNumber;
        this.compNumber = compNumber;
        this.payment = payment;
        this.address = address;
        this.supplierDAO = new SupplierDAO();
    }

    public boolean insert() {
        return supplierDAO.insert(this);
    }

    public boolean delete() {
        return supplierDAO.delete(this);
    }

    public void setName(String name) {
        supplierDAO.updateSupplierName(supplierId, name);
        this.name = name;
    }

    public void setBankNumber(String bankNumber) {
        supplierDAO.updateSupplierBankNumber(supplierId, bankNumber);
        this.bankNumber = bankNumber;
    }

    public void setCompNumber(String compNumber) {
        supplierDAO.updateSupplierCompNumber(supplierId, compNumber);
        this.compNumber = compNumber;
    }

    public void setPayment(String payment) {
        supplierDAO.updateSupplierPayment(supplierId, payment);
        this.payment = payment;
    }

    public void setAddress(String address) {
        supplierDAO.updateSupplierAddress(supplierId, address);
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public String getCompNumber() {
        return compNumber;
    }

    public String getPayment() {
        return payment;
    }

    public String getAddress() {
        return address;
    }

    public int getSupplierId() {
        return supplierId;
    }
}
