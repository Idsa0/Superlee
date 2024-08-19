package com.Superlee.Supply.Business;

import com.Superlee.Supply.DataAccess.*;

import java.util.*;

public class OrderFacade {

    private Map<Integer, Order> orders;
    private int id;
    private final SupplierFacade sf;

    public OrderFacade(SupplierFacade sf){
        this.orders = new HashMap<>();
        this.id = 0;
        this.sf = sf;
    }

    public void addGeneralOrder(Map<Integer, Integer> products, Date shipmentDate, int supplierId){
        SupplierAgreement sa = sf.getSupplierAgreement(supplierId);
        Order order = new Order(this.id, products, shipmentDate, supplierId, sa, -1);
        orders.put(id, order);
        OrderDTO orderDTO = new OrderDTO(order.getOrderId(), order.getSupplierId(), order.getDay(), order.getShipmentDate(), order.getProducts());
        orderDTO.insert();
        id++;
    }

    public void addRepOrder(Map<Integer, Integer> products, Date shipmentDate, int supplierId, int day){
        SupplierAgreement sa = sf.getSupplierAgreement(supplierId);
        Order order = new Order(this.id, products, shipmentDate, supplierId, sa, day);
        orders.put(id, order);
        OrderDTO orderDTO = new OrderDTO(order.getOrderId(), order.getSupplierId(), order.getDay(), order.getShipmentDate(), order.getProducts());
        orderDTO.insert();
        id++;
    }

    public void updateOrders(){
        Map<Integer, Order> copyOrders = new HashMap<>(orders);
        for (Order order : copyOrders.values()){
            if (order.getDay() == -1 && order.getShipmentDate().before(new Date())){
                removeOrder(order.getOrderId());//Updates in the database as well
            }
        }
    }

    public void removeOrder(int orderId){
        orders.remove(orderId);
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.delete(orderId);
    }

    public void updateShipmentDate(int orderId, Date shipmentDate){
        Order order = orders.get(orderId);
        order.setShipmentDate(shipmentDate);
    }

    public void addProduct(int orderId, int catalogNumber, int amount){
        Order order = orders.get(orderId);
        order.addProduct(catalogNumber, amount);
        OrderDTO orderDTO = new OrderDTO(order.getOrderId(), order.getSupplierId(), order.getDay(), order.getShipmentDate(), order.getProducts());
        orderDTO.insertItem(catalogNumber, amount);
    }

    // HW2
    public void addProductByName(String name, int amount, Date shipmentDate){
        int[] bestOption = sf.pickCheapestOption(name, amount);
        int catalogNumber = bestOption[0];
        int supplierId = bestOption[1];
        //System.out.println(catalogNumber);
        int orderId = this.id;
        Map<Integer, Integer> orderProducts = new HashMap<>();
        orderProducts.put(catalogNumber, amount);
        addGeneralOrder(orderProducts, shipmentDate, supplierId);
    }

    public void removeProduct(int orderId, int catalogNumber){
        Order order = orders.get(orderId);
        order.removeProduct(catalogNumber);
        OrderDTO orderDTO = new OrderDTO(order.getOrderId(), order.getSupplierId(), order.getDay(), order.getShipmentDate(), order.getProducts());
        orderDTO.deleteItem(catalogNumber);
    }

    public Order getOrder(int orderId){
        return orders.get(orderId);
    }

    public List<Order> getAllOrders(){
        List<Order> ordersList = new ArrayList<>();
        for (Order order : orders.values()){
            ordersList.add(order);
        }
        return ordersList;
    }

    public double getOrderPrice(int orderId){
        Order order = orders.get(orderId);
        return order.getOrderPrice();
    }

    public void loadAllOrders(){
        OrderDAO orderDAO = new OrderDAO();
        List<OrderDTO> orderDTOS = orderDAO.loadAllOrders();
        for (OrderDTO orderDTO : orderDTOS){
            orderDTO.loadAllItems();
            Map<Integer, Integer> products = orderDTO.getProducts();
            int sid = orderDTO.getSupplierId();
            SupplierAgreement supplierAgreement = sf.getSupplierAgreement(sid);
            Order order = new Order(orderDTO.getOrderId(), products, orderDTO.getShipmentDate(), sid, supplierAgreement,orderDTO.getDay());
            this.orders.put(orderDTO.getOrderId(), order);
            this.id++;
        }

    }

    public String toString(int orderId){
        Order order = orders.get(orderId);
        return order.toString();
    }


}
