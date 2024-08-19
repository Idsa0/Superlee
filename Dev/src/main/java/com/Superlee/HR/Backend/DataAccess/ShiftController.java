package com.Superlee.HR.Backend.DataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShiftController extends Controller<ShiftDTO> {
    ShiftController(ShiftDTO dto) {
        super(dto);
    }

    @Override
    public boolean insert() {
        try {
            connect();
            String insertSQL =
                    "INSERT INTO Shifts(id, startTime, endTime, branch) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setInt(1, dto.getId());
            pstmt.setObject(2, dto.getStartTime());
            pstmt.setObject(3, dto.getEndTime());
            pstmt.setString(4, dto.getBranch());
            pstmt.executeUpdate();

            insertRequiredRoles();
            insertAvailableWorkers();
            insertAssignedWorkers();
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
            String updateSQL =
                    "UPDATE Shifts SET startTime = ?, endTime = ?, branch = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setObject(1, dto.getStartTime());
            pstmt.setObject(2, dto.getEndTime());
            pstmt.setString(3, dto.getBranch());
            pstmt.setInt(4, dto.getId());
            pstmt.executeUpdate();

            deleteRequiredRoles();
            deleteAvailableWorkers();
            deleteAssignedWorkers();

            insertRequiredRoles();
            insertAvailableWorkers();
            insertAssignedWorkers();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }

        return true;
    }

    @Override
    public boolean delete() {
        //throw new UnsupportedOperationException("Deleting shifts is not supported.");
        try {
            connect();
            String deleteSQL = "DELETE FROM Shifts WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
            pstmt.setInt(1, dto.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public List<ShiftDTO> loadAll() {
        try {
            connect();
            String selectSQL = "SELECT * FROM Shifts";
            PreparedStatement pstmt = conn.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
            List<ShiftDTO> shifts = new ArrayList<>();
            while (rs.next()) {
                ShiftDTO shift = new ShiftDTO();
                shift.setId(rs.getInt("id"));
                shift.setStartTime(LocalDateTime.parse(rs.getString("startTime")));
                shift.setEndTime(LocalDateTime.parse(rs.getString("endTime")));
                shift.setBranch(rs.getString("branch"));
                shifts.add(shift);
            }

            for (ShiftDTO shift : shifts) {
                selectSQL = "SELECT * FROM ShiftRoles WHERE shiftId = ?";
                pstmt = conn.prepareStatement(selectSQL);
                pstmt.setInt(1, shift.getId());
                rs = pstmt.executeQuery();
                Map<String, Integer> requiredRoles = shift.getRequiredRoles();
                while (rs.next())
                    requiredRoles.put(rs.getString("role"), rs.getInt("requiredCount"));
                shift.setRequiredRoles(requiredRoles);

                selectSQL = "SELECT * FROM ShiftAvailableWorkers WHERE shiftId = ?";
                pstmt = conn.prepareStatement(selectSQL);
                pstmt.setInt(1, shift.getId());
                rs = pstmt.executeQuery();
                List<String> availableWorkers = shift.getAvailableWorkers();
                while (rs.next())
                    availableWorkers.add(rs.getString("workerId"));
                shift.setAvailableWorkers(availableWorkers);

                selectSQL = "SELECT * FROM ShiftAssignedWorkers WHERE shiftId = ?";
                pstmt = conn.prepareStatement(selectSQL);
                pstmt.setInt(1, shift.getId());
                rs = pstmt.executeQuery();
                //List<String> assignedWorkers = shift.getAssignedWorkers();
                Map<String, Integer> assignedWorkersRoles = shift.getWorkerRoles();
                while (rs.next())
                    assignedWorkersRoles.put(rs.getString("workerId"), rs.getInt("role"));
                shift.setWorkerRoles(assignedWorkersRoles);
                shift.setAssignedWorkers(assignedWorkersRoles.keySet().stream().toList());

            }
            return shifts;
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
    }

    private void insertRequiredRoles() throws SQLException {
        String insertSQL;
        PreparedStatement pstmt;
        for (String role : dto.getRequiredRoles().keySet()) {
            insertSQL = "INSERT INTO ShiftRoles(shiftId, role, requiredCount) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(insertSQL);
            pstmt.setInt(1, dto.getId());
            pstmt.setString(2, role);
            pstmt.setInt(3, dto.getRequiredRoles().get(role));
            pstmt.executeUpdate();
        }
    }

    private void insertAvailableWorkers() throws SQLException {
        String insertSQL;
        PreparedStatement pstmt;
        for (String worker : dto.getAvailableWorkers()) {
            insertSQL = "INSERT INTO ShiftAvailableWorkers(shiftId, workerId) VALUES (?, ?)";
            pstmt = conn.prepareStatement(insertSQL);
            pstmt.setInt(1, dto.getId());
            pstmt.setString(2, worker);
            pstmt.executeUpdate();
        }
    }

    private void insertAssignedWorkers() throws SQLException {
        String insertSQL;
        PreparedStatement pstmt;
        for (String worker : dto.getWorkerRoles().keySet()) {
            insertSQL = "INSERT INTO ShiftAssignedWorkers(shiftId, workerId, role) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(insertSQL);
            pstmt.setInt(1, dto.getId());
            pstmt.setString(2, worker);
            pstmt.setString(3, dto.getWorkerRoles().get(worker).toString());
            pstmt.executeUpdate();
        }
    }

    private void deleteRequiredRoles() throws SQLException {
        String deleteSQL = "DELETE FROM ShiftRoles WHERE shiftId = ?";
        PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
        pstmt.setInt(1, dto.getId());
        pstmt.executeUpdate();
    }

    private void deleteAvailableWorkers() throws SQLException {
        String deleteSQL = "DELETE FROM ShiftAvailableWorkers WHERE shiftId = ?";
        PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
        pstmt.setInt(1, dto.getId());
        pstmt.executeUpdate();
    }

    private void deleteAssignedWorkers() throws SQLException {
        String deleteSQL = "DELETE FROM ShiftAssignedWorkers WHERE shiftId = ?";
        PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
        pstmt.setInt(1, dto.getId());
        pstmt.executeUpdate();
    }
}
