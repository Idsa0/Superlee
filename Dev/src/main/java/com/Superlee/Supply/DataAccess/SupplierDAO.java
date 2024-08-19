package com.Superlee.Supply.DataAccess;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    private final String tableName = "Suppliers";
    private String path;
    private String connectionString;
    public SupplierDAO()
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
        SupplierDTO result = null;
        try {
            result = new SupplierDTO(reader.getInt(1), reader.getString(2), reader.getString(3), reader.getString(4), reader.getString(5), reader.getString(6));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public boolean insert(SupplierDTO supplierDTO) {
        String command = "INSERT INTO " + tableName + " (SupplierId, Name, BankNumber, CompNumber, PaymentMethod, Address) VALUES (" + supplierDTO.getSupplierId() + ", '" + supplierDTO.getName() + "', '" + supplierDTO.getBankNumber() + "', '" + supplierDTO.getCompNumber() + "', '" + supplierDTO.getPayment() + "', '" + supplierDTO.getAddress() + "');";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delete(SupplierDTO supplierDTO) {
        String command = "DELETE FROM " + tableName + " WHERE SupplierId = " + supplierDTO.getSupplierId() + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void updateSupplierName(int supplierId, String name) {
        String command = "UPDATE " + tableName + " SET Name = '" + name + "' WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSupplierBankNumber(int supplierId, String bankNumber) {
        String command = "UPDATE " + tableName + " SET BankNumber = '" + bankNumber + "' WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSupplierCompNumber(int supplierId, String compNumber) {
        String command = "UPDATE " + tableName + " SET CompNumber = '" + compNumber + "' WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSupplierPayment(int supplierId, String payment) {
        String command = "UPDATE " + tableName + " SET PaymentMethod = '" + payment + "' WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSupplierAddress(int supplierId, String address) {
        String command = "UPDATE " + tableName + " SET Address = '" + address + "' WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public SupplierDTO getSupplier(int supplierId) {
        String command = "SELECT * FROM " + tableName + " WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement(); ResultSet reader = s.executeQuery(command)) {
            return (SupplierDTO) ConvertReaderToObject(reader);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<SupplierDTO> loadAllSuppliers() {
        List<SupplierDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + ";";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add((SupplierDTO) ConvertReaderToObject(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return results;
    }
}
