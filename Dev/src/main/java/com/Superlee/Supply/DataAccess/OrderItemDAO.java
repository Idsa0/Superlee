package com.Superlee.Supply.DataAccess;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    private final String tableName = "OrderItems";
    private String path;
    private String connectionString;
    public OrderItemDAO()
    {
        connect();
    }

    protected Connection connect() {
        //path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
        connectionString = "jdbc:sqlite:src/main/resources/SUPPLY.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionString);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public String getTableName() {
        return tableName;
    }

    protected Object ConvertReaderToObject(ResultSet reader)
    {
        OrderItemDTO result = null;
        try {
            result = new OrderItemDTO(reader.getInt(1), reader.getInt(2), reader.getInt(3));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public boolean insert(OrderItemDTO orderItemDTO) {
        String command = "INSERT INTO " + tableName + " (OrderId, CatalogNumber, Amount) VALUES (" + orderItemDTO.getOrderId() + ", " + orderItemDTO.getCatalogNumber() + ", " + orderItemDTO.getAmount() + ");";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delete(int orderId, int catalogNumber) {
        String command = "DELETE FROM " + tableName + " WHERE CatalogNumber = " + catalogNumber + " AND OrderId = " + orderId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<OrderItemDTO> getOrderItems(int orderId) {
        List<OrderItemDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE OrderId = " + orderId + ";";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add((OrderItemDTO) ConvertReaderToObject(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return results;
    }
}
