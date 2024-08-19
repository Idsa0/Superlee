package com.Superlee.HR.Backend.DataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolesController extends Controller<RolesDTO> {
    public RolesController(RolesDTO dto) {
        super(dto);
    }

    @Override
    public boolean insert() {
        try {
            connect();
            String insertSQL = "INSERT INTO Roles(value, name) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setInt(1, dto.getValue());
            pstmt.setString(2, dto.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public boolean update() {
        throw new UnsupportedOperationException("Updating roles is not supported.");
    }

    @Override
    public boolean delete() {
        //throw new UnsupportedOperationException("Deleting roles is not supported.");
        try {
            connect();
            String deleteSQL = "DELETE FROM Roles WHERE value = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
            pstmt.setInt(1, dto.getValue());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public List<RolesDTO> loadAll() {
        try {
            connect();
            String selectSQL = "SELECT * FROM Roles";
            PreparedStatement pstmt = conn.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
            List<RolesDTO> roles = new ArrayList<>();
            while (rs.next())
                roles.add(new RolesDTO(rs.getInt("value"), rs.getString("name")));
            return roles;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
    }
}
