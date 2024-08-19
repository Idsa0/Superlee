package com.Superlee.Supply.Business;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SupplierAgreement {

    // <catalogNumber, SupplierProduct>
    public final Map<Integer, SupplierProduct> products;
    private int supplierId;
    private static final int PRODUCT_NOT_EXISTS = 0;

    public SupplierAgreement(SupplierAgreement agreement) {
        this.products = new ConcurrentHashMap<>();
        for(SupplierProduct supplierProduct : agreement.getProducts().values())
            products.put(supplierProduct.getCatalogNumber(), new SupplierProduct(supplierProduct));
    }

    public SupplierAgreement(int supplierId) {
        this.supplierId = supplierId;
        this.products = new ConcurrentHashMap<>();
    }

    public Map<Integer, SupplierProduct> getProducts() {
        return products;
    }

    public void addProduct(SupplierProduct supplierProduct) {
        products.put(supplierProduct.getCatalogNumber(), supplierProduct);
    }

    public void removeProduct(int catalogNumber) {
        if(checkProductExists(catalogNumber)) {
            products.remove(catalogNumber);
        }
    }

    public double getProductPrice(int catalogNumber) {
        if(checkProductExists(catalogNumber))
            return products.get(catalogNumber).getPrice();
        return PRODUCT_NOT_EXISTS;
    }

    private boolean checkProductExists(int catalogNumber) {
        return products.containsKey(catalogNumber);
    }

    public Map<Integer, Double> getProductDiscounts(int catalogNumber) {
        if(checkProductExists(catalogNumber))
            return products.get(catalogNumber).getProductDiscounts();
        return null;
    }

    public boolean checkProductDiscountAccordingToAmount(int catalogNumber, int amount) {
        return checkProductExists(catalogNumber) && products.get(catalogNumber).isAmountExists(amount);
    }

    public boolean equals(SupplierAgreement other){
        return other.getProducts().equals(products);
    }

    public SupplierProduct getProduct(int catalogNumber) {
        if(checkProductExists(catalogNumber)) {
            return products.get(catalogNumber);
        }
        return null;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public double getProductPriceAccordingToAmount(int catalogNumber, int amount) {
        if (checkProductExists(catalogNumber)) {
            double price = getProductPrice(catalogNumber);
            price -= getDiscountForOrder(catalogNumber, amount) * (price / 100.0);
            return price;
        }
        return PRODUCT_NOT_EXISTS;
    }

    public double getDiscountForOrder(int catalogNumber, int amount) {
        if(checkProductExists(catalogNumber))
            return products.get(catalogNumber).getDiscountForOrder(amount);
        return PRODUCT_NOT_EXISTS;
    }

    public void updateProductPrice(int catalogNumber, double price){
        if(price <= 0)
            throw new IllegalArgumentException("Invalid price!");
        if (checkProductExists(catalogNumber))
            products.get(catalogNumber).setPrice(price);
    }

    public void addProductDiscountAccordingToAmount(int catalogNumber, int amount, double discount) {
        if (checkProductExists(catalogNumber))
            products.get(catalogNumber).addProductDiscountAccordingToAmount(amount, discount);
    }

    public void updateProductDiscountAccordingToAmount(int catalogNumber, int amount, double newDiscount) {
        if (checkProductExists(catalogNumber))
            products.get(catalogNumber).updateProductDiscountAccordingToAmount(amount, newDiscount);
    }
    
    public void removeProductDiscountAccordingToAmount(int catalogNumber, int amount) {
        if (checkProductExists(catalogNumber))
            products.get(catalogNumber).removeProductDiscountAccordingToAmount(amount);
    }

    public String getProductName(int catalogNumber) {
        if(checkProductExists(catalogNumber))
            return products.get(catalogNumber).getName();
        return null;
    }

    public void updateProductName(int catalogNumber, String newName) {
        if(checkProductExists(catalogNumber))
            products.get(catalogNumber).setName(newName);
    }

    // HW2
    public double getProductPriceByName(String name, int amount){
        SupplierProduct sp;

        for (Map.Entry<Integer,SupplierProduct> product : products.entrySet()){
            sp = product.getValue();
            if (sp.getName().equals(name)) {
                return getProductPriceAccordingToAmount(product.getKey(), amount);
            }
        }
        return PRODUCT_NOT_EXISTS;
    }

    // HW2
    public int getProductIdByName(String name){
        SupplierProduct sp;

        for (Map.Entry<Integer,SupplierProduct> product : products.entrySet()){
            sp = product.getValue();
            if (sp.getName().equals(name)) {
                return product.getKey();
            }
        }
        return -1;
    }
}
