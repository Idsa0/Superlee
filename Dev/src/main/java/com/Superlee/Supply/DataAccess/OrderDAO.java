package com.Superlee.Supply.DataAccess;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDAO {

    private final String tableName = "Orders";
    private String path;
    private String connectionString;
    public OrderDAO()
    {
        connect();
    }

    protected Connection connect() {
//        path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        OrderDTO result = null;
        try {
            result = new OrderDTO(reader.getInt(1), reader.getInt(2), reader.getInt(3), formatter.parse(reader.getString(4)), new HashMap<>());
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean insert(OrderDTO orderDTO) {
        String command = "INSERT INTO " + tableName + " (OrderId, SupplierId, Day, ShipmentDate) VALUES (" + orderDTO.getOrderId() + ", '" + orderDTO.getSupplierId() + "', '" + orderDTO.getDay() + "', '" + orderDTO.getShipmentDate() + "');";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delete(int orderId) {
        String command = "DELETE FROM " + tableName + " WHERE OrderId = " + orderId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<OrderDTO> loadAllOrders() {
        List<OrderDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + ";";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add((OrderDTO) ConvertReaderToObject(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return results;
    }


}
