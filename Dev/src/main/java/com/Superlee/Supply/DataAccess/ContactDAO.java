package com.Superlee.Supply.DataAccess;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private final String tableName = "Contacts";
    private String path;
    private String connectionString;
    public ContactDAO()
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
    protected Object ConvertReaderToObject(ResultSet reader) {
        ContactDTO result = null;
        try {
            result = new ContactDTO(reader.getInt(1), reader.getInt(2), reader.getString(3), reader.getString(4));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    public boolean insert(ContactDTO contactDTO) {
        String command = "INSERT INTO " + tableName + " (ContactID, SupplierId, Name, PhoneNumber) VALUES (" + contactDTO.getContactId() + ", " + contactDTO.getSupplierId() + ", '" + contactDTO.getName() + "', '" + contactDTO.getPhoneNumber() + "');";
        try (Connection conn = connect(); Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delete(ContactDTO contactDTO) {
        String command = "DELETE FROM " + tableName + " WHERE ContactID = " + contactDTO.getContactId() + ";";
        try (Connection conn = connect(); Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public ContactDTO getSupplierContact(int supplierId) {
        String command = "SELECT * FROM " + tableName + " WHERE SupplierId = " + supplierId + ";";
        try (Connection conn = connect(); Statement s = conn.createStatement(); ResultSet reader = s.executeQuery(command)) {
            return (ContactDTO) ConvertReaderToObject(reader);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean updateContactName(int contactId, String newName) {
        String command = "UPDATE " + tableName + " SET Name = '" + newName + "' WHERE ContactID = " + contactId + ";";
        try (Connection conn = connect(); Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateContactPhoneNumber(int contactId, String newPhoneNumber) {
        String command = "UPDATE " + tableName + " SET PhoneNumber = '" + newPhoneNumber + "' WHERE ContactID = " + contactId + ";";
        try (Connection conn = connect(); Statement s = conn.createStatement()) {
            s.execute(command);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<ContactDTO> loadAllContacts() {
        List<ContactDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + ";";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add((ContactDTO) ConvertReaderToObject(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return results;
    }
}
