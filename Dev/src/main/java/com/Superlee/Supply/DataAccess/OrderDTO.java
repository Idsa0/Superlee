package com.Superlee.Supply.DataAccess;

import java.util.*;

public class OrderDTO {

    private final String orderIdColumnName="OrderId";
    private final String supplierIdColumnName="SupplierId";
    private final String dayColumnName="Day";
    private final String ShipmentDateColumnName="ShipmentDate";

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private int orderId;
    private int supplierId;
    private int day;
    private Date shipmentDate;
    private Map<Integer, Integer> products;



    public OrderDTO(int orderId, int supplierId, int day, Date shipmentDate, Map<Integer, Integer> products) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.day = day;
        this.shipmentDate = shipmentDate;
        this.products = products;
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
    }

    // Items
    public boolean insertItem(int catalogNumber, int amount) {
        OrderItemDTO orderItemDTO = new OrderItemDTO(orderId, catalogNumber, amount);
        return orderItemDAO.insert(orderItemDTO);
    }

    public boolean deleteItem(int catalogNumber) {
        return orderItemDAO.delete(orderId, catalogNumber);
    }

    public void loadAllItems(){
        List<OrderItemDTO> items = orderItemDAO.getOrderItems(orderId);
        for (OrderItemDTO orderItemDTO : items){
            products.put(orderItemDTO.getCatalogNumber(), orderItemDTO.getAmount());
        }
    }

    // Order
    public boolean insert(){

        boolean flag = orderDAO.insert(this);
        if (flag){
            for (Map.Entry<Integer,Integer> product : products.entrySet()){
                insertItem(product.getKey(), product.getValue());
            }
        }

        return true;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getDay() {
        return day;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }
}
