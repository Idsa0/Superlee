package com.Superlee.HR.Backend.DataAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkerController extends Controller<WorkerDTO> {
    WorkerController(WorkerDTO dto) {
        super(dto);
    }

    @Override
    boolean insert() {
        try {
            connect();
            String insertSQL =
                    "INSERT INTO Workers(id, firstname, surname, email, phone, password, bankDetails, salary, startDate, contract, branch)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getFirstname());
            pstmt.setString(3, dto.getSurname());
            pstmt.setString(4, dto.getEmail());
            pstmt.setString(5, dto.getPhone());
            pstmt.setString(6, dto.getPassword());
            pstmt.setString(7, dto.getBankDetails());
            pstmt.setInt(8, dto.getSalary());
            pstmt.setObject(9, dto.getStartDate());
            pstmt.setString(10, dto.getContract());
            pstmt.setString(11, dto.getBranch());
            pstmt.executeUpdate();

            insertRoles();
            insertShifts();
            insertAvailability();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    boolean update() {
        try {
            connect();
            String updateSQL =
                    "UPDATE Workers SET firstname = ?, surname = ?, email = ?, phone = ?, password = ?, bankDetails = ?, salary = ?, startDate = ?, contract = ?, branch = ?"
                    + " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setString(1, dto.getFirstname());
            pstmt.setString(2, dto.getSurname());
            pstmt.setString(3, dto.getEmail());
            pstmt.setString(4, dto.getPhone());
            pstmt.setString(5, dto.getPassword());
            pstmt.setString(6, dto.getBankDetails());
            pstmt.setInt(7, dto.getSalary());
            pstmt.setObject(8, dto.getStartDate());
            pstmt.setString(9, dto.getContract());
            pstmt.setString(10, dto.getBranch());
            pstmt.setString(11, dto.getId());
            pstmt.executeUpdate();

            deleteRoles();
            deleteShifts();
            deleteAvailability();

            insertRoles();
            insertShifts();
            insertAvailability();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    boolean delete() {
        //throw new UnsupportedOperationException("Deleting workers is not supported.");
        try {
            connect();
            String deleteSQL = "DELETE FROM Workers WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
            pstmt.setString(1, dto.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    List<WorkerDTO> loadAll() {
        try {
            connect();
            String selectSQL = "SELECT * FROM Workers";
            PreparedStatement pstmt = conn.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery();
            List<WorkerDTO> workers = new ArrayList<>();
            while (rs.next()) {
                WorkerDTO worker = new WorkerDTO();
                worker.setId(rs.getString("id"));
                worker.setFirstname(rs.getString("firstname"));
                worker.setSurname(rs.getString("surname"));
                worker.setEmail(rs.getString("email"));
                worker.setPhone(rs.getString("phone"));
                worker.setPassword(rs.getString("password"));
                worker.setBankDetails(rs.getString("bankDetails"));
                worker.setSalary(rs.getInt("salary"));
                worker.setStartDate(LocalDateTime.parse(rs.getString("startDate"))); // TODO verify
                worker.setContract(rs.getString("contract"));
                worker.setBranch(rs.getString("branch"));
                workers.add(worker);
            }

            for (WorkerDTO worker : workers) {
                String selectRolesSQL = "SELECT role FROM WorkerRoles WHERE workerId = ?";
                PreparedStatement pstmtRoles = conn.prepareStatement(selectRolesSQL);
                pstmtRoles.setString(1, worker.getId());
                ResultSet rsRoles = pstmtRoles.executeQuery();
                List<Integer> roles = new ArrayList<>();
                while (rsRoles.next())
                    roles.add(rsRoles.getInt("role"));
                worker.setRoles(roles);

                String selectShiftsSQL = "SELECT shiftId FROM WorkerShifts WHERE workerId = ?";
                PreparedStatement pstmtShifts = conn.prepareStatement(selectShiftsSQL);
                pstmtShifts.setString(1, worker.getId());
                ResultSet rsShifts = pstmtShifts.executeQuery();
                List<Integer> shifts = new ArrayList<>();
                while (rsShifts.next())
                    shifts.add(rsShifts.getInt("shiftId"));
                worker.setShifts(shifts);

                String selectAvailabilitySQL = "SELECT shiftId FROM WorkerAvailability WHERE workerId = ?";
                PreparedStatement pstmtAvailability = conn.prepareStatement(selectAvailabilitySQL);
                pstmtAvailability.setString(1, worker.getId());
                ResultSet rsAvailability = pstmtAvailability.executeQuery();
                List<Integer> availability = new ArrayList<>();
                while (rsAvailability.next())
                    availability.add(rsAvailability.getInt("shiftId"));
                worker.setAvailability(availability);
            }
            return workers;
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            closeConnection();
        }
    }

    private void insertRoles() throws SQLException {
        List<Integer> roles = dto.getRoles();
        String insertRoleSQL;
        PreparedStatement pstmtRole;
        for (int role : roles) {
            insertRoleSQL = "INSERT INTO WorkerRoles(workerId, role) VALUES (?, ?)";
            pstmtRole = conn.prepareStatement(insertRoleSQL);
            pstmtRole.setString(1, dto.getId());
            pstmtRole.setInt(2, role);
            pstmtRole.executeUpdate();
        }
    }

    private void insertShifts() throws SQLException {
        List<Integer> shifts = dto.getShifts();
        String insertShiftSQL;
        PreparedStatement pstmtShift;
        for (int shift : shifts) {
            insertShiftSQL = "INSERT INTO WorkerShifts(workerId, shiftId) VALUES (?, ?)";
            pstmtShift = conn.prepareStatement(insertShiftSQL);
            pstmtShift.setString(1, dto.getId());
            pstmtShift.setInt(2, shift);
            pstmtShift.executeUpdate();
        }
    }

    private void insertAvailability() throws SQLException {
        List<Integer> availability = dto.getAvailability();
        String insertAvailabilitySQL;
        PreparedStatement pstmtAvailability;
        for (int shift : availability) {
            insertAvailabilitySQL = "INSERT INTO WorkerAvailability(workerId, shiftId) VALUES (?, ?)";
            pstmtAvailability = conn.prepareStatement(insertAvailabilitySQL);
            pstmtAvailability.setString(1, dto.getId());
            pstmtAvailability.setInt(2, shift);
            pstmtAvailability.executeUpdate();
        }
    }

    private void deleteRoles() throws SQLException {
        String deleteRolesSQL = "DELETE FROM WorkerRoles WHERE workerId = ?";
        PreparedStatement pstmtDeleteRoles = conn.prepareStatement(deleteRolesSQL);
        pstmtDeleteRoles.setString(1, dto.getId());
        pstmtDeleteRoles.executeUpdate();
    }

    private void deleteShifts() throws SQLException {
        String deleteShiftsSQL = "DELETE FROM WorkerShifts WHERE workerId = ?";
        PreparedStatement pstmtDeleteShifts = conn.prepareStatement(deleteShiftsSQL);
        pstmtDeleteShifts.setString(1, dto.getId());
        pstmtDeleteShifts.executeUpdate();
    }

    private void deleteAvailability() throws SQLException {
        String deleteAvailabilitySQL = "DELETE FROM WorkerAvailability WHERE workerId = ?";
        PreparedStatement pstmtDeleteAvailability = conn.prepareStatement(deleteAvailabilitySQL);
        pstmtDeleteAvailability.setString(1, dto.getId());
        pstmtDeleteAvailability.executeUpdate();
    }
}
