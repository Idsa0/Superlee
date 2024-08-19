package com.Superlee.Supply.DataAccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierAgreementDAO {
    private final String tableName = "SupplierAgreement";
    private String path;
    private String connectionString;
    public SupplierAgreementDAO()
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

    protected Object ConvertReaderToObjectProduct(ResultSet reader) {
        SupplierAgreementDTO result = null;
        try {
            result = new SupplierAgreementDTO(reader.getInt(1), reader.getInt(2), reader.getDouble(3), reader.getString(4));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    protected Object ConvertReaderToObjectDiscount(ResultSet reader) {
        SupplierAgreementDTO result = null;
        try {
            result = new SupplierAgreementDTO(reader.getInt(1), reader.getInt(2), reader.getInt(3), reader.getDouble(4));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    public boolean insertSupplierProduct(SupplierAgreementDTO supplierAgreementDTO) {
        String command = "INSERT INTO " + tableName + " (SupplierId, CatalogNumber, Price, Name) VALUES (" + supplierAgreementDTO.getSupplierId() + ", " + supplierAgreementDTO.getCatalogNumber() + ", " + supplierAgreementDTO.getPrice() + ", '" + supplierAgreementDTO.getName() + "');";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean insertProductDiscountAccordingToAmount(SupplierAgreementDTO supplierAgreementDTO) {
        String command = "INSERT INTO " + "SupplierItemsDiscounts" + " (SupplierId, CatalogNumber, Amount, Discounts) VALUES (" + supplierAgreementDTO.getSupplierId() + ", " + supplierAgreementDTO.getCatalogNumber() + ", " + supplierAgreementDTO.getAmount() + ", " + supplierAgreementDTO.getDiscounts() + ");";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delete(SupplierAgreementDTO supplierAgreementDTO) {
        String command = "DELETE FROM " + tableName + " WHERE SupplierId = " + supplierAgreementDTO.getSupplierId() + " AND CatalogNumber = " + supplierAgreementDTO.getCatalogNumber() + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void updateProductPrice(int supplierId, int catalogNumber, double price) {
        String command = "UPDATE " + tableName + " SET Price = " + price + " WHERE SupplierId = " + supplierId + " AND CatalogNumber = " + catalogNumber + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateProductName(int supplierId, int catalogNumber, String name) {
        String command = "UPDATE " + tableName + " SET Name = '" + name + "' WHERE SupplierId = " + supplierId + " AND CatalogNumber = " + catalogNumber + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateProductAmount(int supplierId, int catalogNumber, int amount) {
        String command = "UPDATE " + "SupplierItemsDiscounts" + " SET Amount = " + amount + " WHERE SupplierId = " + supplierId + " AND CatalogNumber = " + catalogNumber + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateProductDiscounts(int supplierId, int catalogNumber, double discounts) {
        String command = "UPDATE " + "SupplierItemsDiscounts" + " SET Discounts = " + discounts + " WHERE SupplierId = " + supplierId + " AND CatalogNumber = " + catalogNumber + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public SupplierAgreementDTO getSupplierAgreement(int supplierId) {
        String command = "SELECT * FROM " + "SupplierItemsDiscounts" + " WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement(); ResultSet reader = s.executeQuery(command)) {
            return (SupplierAgreementDTO) ConvertReaderToObjectProduct(reader);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Map<Integer, Double> selectDiscountsOfItem(int supplierId, int catalogNumber) {
        String command = "SELECT * FROM " + "SupplierItemsDiscounts" + " WHERE SupplierId = " + supplierId + " AND CatalogNumber = " + catalogNumber + ";";
        Map<Integer, Double> result = new HashMap<>();
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement(); ResultSet reader = s.executeQuery(command)) {
            while (reader.next()) {
                result.put(reader.getInt(3), reader.getDouble(4));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    public boolean deleteProductDiscountAccordingToAmount(int supplierId, int catalogNumber) {
        String command = "DELETE FROM " + "SupplierItemsDiscounts" + " WHERE SupplierId = " + supplierId + " AND CatalogNumber = " + catalogNumber + ";";
        try (Connection conn = connect(); java.sql.Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<SupplierAgreementDTO> loadAllSupplierAgreements() {
        List<SupplierAgreementDTO> results = new java.util.ArrayList<>();
        String sql = "SELECT * FROM " + tableName + ";";
        try(Connection conn = this.connect();
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add((SupplierAgreementDTO) ConvertReaderToObjectProduct(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    public List<SupplierAgreementDTO> loadAllSupplierAgreementsDiscounts() {
        List<SupplierAgreementDTO> results = new java.util.ArrayList<>();
        String sql = "SELECT * FROM " + "SupplierItemsDiscounts" + ";";
        try(Connection conn = this.connect();
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add((SupplierAgreementDTO) ConvertReaderToObjectDiscount(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }
    // LoadSupplierProduct
}
