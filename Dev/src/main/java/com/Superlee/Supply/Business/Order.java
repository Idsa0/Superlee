package com.Superlee.Supply.Business;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private final int orderId;
    // <catalogNumber, amount>
    private Map<Integer, Integer> products;
    private Date shipmentDate;
    private final int supplierId;
    private SupplierAgreement supplierAgreement;
    private int day;
    private String[] convertDay = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


    public Order(int orderId, Map<Integer, Integer> products, Date shipmentDate, int supplierId, SupplierAgreement supplierAgreement, int day) {
        this.orderId = orderId;
        this.products = products;
        this.shipmentDate = shipmentDate;
        this.supplierId = supplierId;
        this.supplierAgreement = supplierAgreement;
        this.day = day;
    }



    public int getOrderId() {
        return orderId;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public void addProduct(int catalogNumber, int amount){
        products.put(catalogNumber, amount);
    }

    public void removeProduct(int catalogNumber){
        products.remove(catalogNumber);
    }

    public double getOrderPrice(){
        double sum = 0;
        for (Map.Entry<Integer,Integer> product : products.entrySet()){
            sum += supplierAgreement.getProductPriceAccordingToAmount(product.getKey(), product.getValue()) * product.getValue();
        }
        return sum;
    }

    public int getDay() {
        return day;
    }

    public String toString(){
        String str = "Order id: " + this.orderId + "\n";
        str += "Shipment date: " + this.shipmentDate + "\n";
        str += "Supplier id: " + this.supplierId + "\n";
        if (day >= 0)
            str += "Day: " + convertDay[this.day] + "\n";
        str+= "Products:" + "\n";
        for (Map.Entry<Integer,Integer> product : products.entrySet()){
            str += "Catalog Number: " + product.getKey() + ", Amount: " + product.getValue() + "\n";
        }

        return str;
    }
}
