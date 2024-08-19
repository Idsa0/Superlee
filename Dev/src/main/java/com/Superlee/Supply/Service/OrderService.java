package com.Superlee.Supply.Service;

import com.Superlee.Supply.Business.Order;
import com.Superlee.Supply.Business.OrderFacade;
import com.Superlee.Supply.Service.Responses.Response;
import com.Superlee.Supply.Service.Responses.ResponseT;
import netscape.javascript.JSObject;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class OrderService {

    private final OrderFacade orderFacade;
    private static OrderService instance;

    public OrderService(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }


    public Response loadData(){
        try {
            orderFacade.loadAllOrders();
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    // Adds a single order to the system
    public Response addGeneralOrder(Map<Integer, Integer> products, Date shipmentDate, int supplierId){
        try {
            orderFacade.addGeneralOrder(products, shipmentDate, supplierId);
            return new Response();
        } catch (Exception e) {
            return (new Response(e.getMessage()));
        }
    }

    // Adds a repeating order for a specific day of the week
    public Response addRepOrder(Map<Integer, Integer> products, Date shipmentDate, int supplierId, int day){
        try {
            orderFacade.addRepOrder(products, shipmentDate, supplierId, day);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Removes all general (non-repeating) orders that have been shipped
    public Response updateOrders(){
        try {
            orderFacade.updateOrders();
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Removes an order from the system
    public Response removeOrder(int orderId){
        try {
            orderFacade.removeOrder(orderId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Adds a product to an existing order
    public Response addProduct(int orderId, int catalogNumber, int amount){
        try {
            orderFacade.addProduct(orderId, catalogNumber, amount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // HW2 - picks the cheapest option for a given product, and creates an order with it
    public Response orderCheapestOption(String name, int amount, Date shipmentDate){
        try {
            orderFacade.addProductByName(name, amount, shipmentDate);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //Removes a product from an existing order
    public Response removeProduct(int orderId, int catalogNumber){
        try {
            orderFacade.removeProduct(orderId, catalogNumber);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    // Returns an order according to the id given
    public ResponseT<Order> getOrder(int orderId){
        try {
            Order order = orderFacade.getOrder(orderId);
            return new ResponseT<Order>(order);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    // Returns a list of all orders in the system
    public ResponseT<List<Order>> getAllOrders(){
        try {
            List<Order> orders = orderFacade.getAllOrders();
            return new ResponseT<>(orders);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    // Returns the price of an order according to the id given
    public ResponseT<Double> getOrderPrice(int orderId){
        try {
            double price = orderFacade.getOrderPrice(orderId);
            return new ResponseT<>(price);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public String toString(int orderId){
        return orderFacade.toString(orderId);
    }

    public static OrderService getInstance(OrderFacade orderFacade) {
        if (instance == null)
            instance = new OrderService(orderFacade);
        return instance;
    }
}
