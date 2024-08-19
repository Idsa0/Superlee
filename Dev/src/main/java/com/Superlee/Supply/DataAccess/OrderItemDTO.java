package com.Superlee.Supply.DataAccess;

public class OrderItemDTO {

    private int catalogNumber;
    private int orderId;
    private int amount;
    private OrderItemDAO orderItemDAO;

    public OrderItemDTO(int orderId, int catalogNumber, int amount) {
        this.orderId = orderId;
        this.catalogNumber = catalogNumber;
        this.amount = amount;
        this.orderItemDAO = new OrderItemDAO();
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getAmount() {
        return amount;
    }
}
