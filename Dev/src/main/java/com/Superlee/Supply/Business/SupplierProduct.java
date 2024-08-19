package com.Superlee.Supply.Business;

import java.util.HashMap;
import java.util.Map;

public class SupplierProduct {

    private int catalogNumber;
    private String name;
    private int supplierId;
    private double price;
    private double priceAfterDiscount;
    private int discountPrecentage;
    private int discountThreshold;
    // <amount, discount>
    private Map<Integer, Double> discountAccordingToAmount;


    public SupplierProduct(int catalogNumber, String name, int supplierId, double price, double priceAfterDiscount, int discountPrecentage, int discountThreshold) {
        this.catalogNumber = catalogNumber;
        this.name = name;
        this.supplierId = supplierId;
        this.price = price;
        this.priceAfterDiscount = priceAfterDiscount;
        this.discountPrecentage = discountPrecentage;
        this.discountThreshold = discountThreshold;
        this.discountAccordingToAmount = new HashMap<>();
        discountAccordingToAmount.put(0, 0.0);
    }
    
    public SupplierProduct(SupplierProduct supplierProduct) {
        this.catalogNumber = supplierProduct.getCatalogNumber();
        this.name = supplierProduct.getName();
        this.supplierId = supplierProduct.getSupplierId();
        this.price = supplierProduct.getPrice();
        this.priceAfterDiscount = supplierProduct.getPriceAfterDiscount();
        this.discountPrecentage = supplierProduct.getDiscountPrecentage();
        this.discountThreshold = supplierProduct.getDiscountThreshold();
        this.discountAccordingToAmount = new HashMap<>();
        discountAccordingToAmount.put(0, 0.0);
    }

    public SupplierProduct(int supplierId, int catalogNumber, double price, String name) {
        this.catalogNumber = catalogNumber;
        this.price = price;
        this.supplierId = supplierId;
        this.name = name;
        this.discountAccordingToAmount = new HashMap<>();
        this.discountAccordingToAmount.put(0, 0.0);
    }

    private int getDiscountThreshold() {
        return this.discountThreshold;   
    }

    private int getDiscountPrecentage() {
        return this.discountPrecentage;
    }

    private double getPriceAfterDiscount() {
        return this.priceAfterDiscount;
    }

    public double getPrice() {
        return this.price;
    }

    public int getSupplierId() {
        return this.supplierId;  
    }

    public String getName() {
        return this.name;
    }

    public int getCatalogNumber() {
        return this.catalogNumber;
    }

    public Map<Integer, Double> getProductDiscounts() {
        return this.discountAccordingToAmount;
    }

    public boolean isAmountExists(int amount){
        return this.discountAccordingToAmount.containsKey(amount);
    }

    public double getDiscountForOrder(int amount) {
        if(isAmountExists(amount))
            return this.discountAccordingToAmount.get(amount);
        return 0;
    }

    public void setPrice(double price2) {
        this.price = price2;    
    }

    private boolean isLegalDiscount(double discount) {
        return discount >= 0 && discount <= 100;
    }

    public void addProductDiscountAccordingToAmount(int amount, double discount) {
        if (!isLegalDiscount((int) discount))
            throw new IllegalArgumentException("Invalid discount!");
        this.discountAccordingToAmount.put(amount, discount);
    }

    public void updateProductDiscountAccordingToAmount(int amount, double newDiscount) {
        if (!isLegalDiscount(newDiscount))
            throw new IllegalArgumentException("Invalid discount!");
        discountAccordingToAmount.put(amount, newDiscount);
    }

    public void removeProductDiscountAccordingToAmount(int amount) {
        if (!isAmountExists(amount))
            throw new IllegalArgumentException("Product does not exist!");
        discountAccordingToAmount.remove(amount);
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
