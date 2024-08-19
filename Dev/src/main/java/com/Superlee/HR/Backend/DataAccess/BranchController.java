package com.Superlee.HR.Backend.DataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BranchController extends Controller<BranchDTO> {
    BranchController(BranchDTO dto) {
        super(dto);
    }

    @Override
    public boolean insert() {
        try {
            connect();
            String insertSQL = "INSERT INTO Branches(name, address, manager) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getAddress());
            pstmt.setString(3, dto.getManager());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public boolean update() {
        try {
            connect();
            String updateSQL = "UPDATE Branches SET address = ?, manager = ? WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setString(1, dto.getAddress());
            pstmt.setString(2, dto.getManager());
            pstmt.setString(3, dto.getName());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public boolean delete() {
        //throw new UnsupportedOperationException("Deleting branches is not supported.");
        try {
            connect();
            String deleteSQL = "DELETE FROM Branches WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
            pstmt.setString(1, dto.getName());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public List<BranchDTO> loadAll() {
        try {
            connect();
            String selectSQL = "SELECT * FROM Branches";
            PreparedStatement pstmt = conn.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
            List<BranchDTO> branches = new ArrayList<>();
            while (rs.next())
                branches.add(new BranchDTO(rs.getString("name"), rs.getString("address"), rs.getString("manager")));
            return branches;
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
    }
}
