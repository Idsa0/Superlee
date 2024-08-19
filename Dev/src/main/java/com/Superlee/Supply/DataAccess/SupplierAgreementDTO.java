package com.Superlee.Supply.DataAccess;

public class SupplierAgreementDTO {
    private final String SupplierIdColumnName="SupplierId";
    private final String CatalogNumberColumnName="CatalogNumber";
    private final String PriceColumnName="Price";
    private final String NameColumnName="Name";
    private final String AmountColumnName="Amount";
    private final String DiscountsColumnName="Discounts";
    private SupplierAgreementDAO supplierAgreementDAO;
    private int supplierId;
    private int catalogNumber;
    private double price;
    private String name;
    private int amount;
    private double discounts;
    public SupplierAgreementDTO(int supplierId, int catalogNumber, double price, String name) {
        this.supplierId = supplierId;
        this.catalogNumber = catalogNumber;
        this.price = price;
        this.name = name;
        this.supplierAgreementDAO = new SupplierAgreementDAO();
    }
    public SupplierAgreementDTO(int supplierId, int catalogNumber, int amount, double discounts) {
        this.supplierId = supplierId;
        this.catalogNumber = catalogNumber;
        this.amount = amount;
        this.discounts = discounts;
        this.supplierAgreementDAO = new SupplierAgreementDAO();
    }
    public boolean insertSupplierItem() {
        return supplierAgreementDAO.insertSupplierProduct(this);
    }
    public boolean insertSupplierProductAccordingTOAmount() {
        return supplierAgreementDAO.insertProductDiscountAccordingToAmount(this);
    }
    public boolean delete() {
        return supplierAgreementDAO.delete(this);
    }

    public boolean deleteProductDiscountAccordingToAmount(int supplierId, int catalogNumber) {
        return supplierAgreementDAO.deleteProductDiscountAccordingToAmount(supplierId, catalogNumber);
    }
    public void setPrice(double price) {
        supplierAgreementDAO.updateProductPrice(supplierId, catalogNumber, price);
        this.price = price;
    }
    public void setName(String name) {
        supplierAgreementDAO.updateProductName(supplierId, catalogNumber, name);
        this.name = name;
    }
    public void setAmount(int amount) {
        supplierAgreementDAO.updateProductAmount(supplierId, catalogNumber, amount);
        this.amount = amount;
    }
    public void setDiscounts(double discounts) {
        supplierAgreementDAO.updateProductDiscounts(supplierId, catalogNumber, discounts);
        this.discounts = discounts;
    }
    public int getSupplierId() {
        return supplierId;
    }
    public int getCatalogNumber() {
        return catalogNumber;
    }
    public double getPrice() {
        return price;
    }
    public String getName() {
        return name;
    }
    public int getAmount() {
        return amount;
    }
    public double getDiscounts() {
        return discounts;
    }
}
