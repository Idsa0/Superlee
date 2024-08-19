package com.Superlee.HR.Backend.Business;

import com.Superlee.HR.Backend.DataAccess.DTO;
import com.Superlee.HR.Backend.DataAccess.ShiftDTO;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ShiftFacade {
    private static ShiftFacade instance;
    private static final WorkerFacade workerFacade = WorkerFacade.getInstance();
    private final Roles roles = Roles.getInstance();
    private ShiftDTO dto = new ShiftDTO();
    private int nextId = 0;
    private Map<Integer, Shift> shifts;

    private ShiftFacade() {
        shifts = new HashMap<>();
    }

    public static ShiftFacade getInstance() {
        if (instance == null)
            instance = new ShiftFacade();

        return instance;
    }

    public List<WorkerToSend> getWorkersByShift(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Illegal argument");

        workerFacade.requireHRManagerOrThrow();

        if (!shifts.containsKey(id))
            throw new NoSuchElementException("Shift not found");

        return shifts.get(id).getAssignedWorkers().stream().map(workerFacade::getWorkerById).collect(Collectors.toList());
    }

    public boolean assignWorker(String workerId, int shiftId, String role) {
        Util.throwIfNullOrEmpty(workerId, role);
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");

        workerFacade.requireHRManagerOrThrow();

        if (!shifts.containsKey(shiftId))
            throw new NoSuchElementException("Shift not found");

        if (workerFacade.getWorkerById(workerId) == null)
            throw new NoSuchElementException("Worker not found");

        if (roles.getId(role) == null)
            throw new NoSuchElementException("Role not found");

        Shift shift = shifts.get(shiftId);
        // If the worker is a driver we must ensure that a storekeeper is also assigned to the shift
        if (role.equals("Driver") &&
            shift.getAssignedWorkers().stream()
                    .noneMatch(w -> workerFacade.getWorkerRoles(w).contains("Storekeeper")))
            throw new IllegalStateException("A storekeeper must be assigned to the shift before a driver can be assigned");

        if (!workerFacade.assignWorker(workerId, shiftId, role))
            throw new IllegalStateException("Unexpected error");

        if (!shift.assignWorker(workerId, roles.getId(role)))
            if (!workerFacade.unassignWorker(workerId, shiftId))
                throw new IllegalStateException("Unexpected error, reverting changes failed");
            else
                throw new IllegalStateException("Unexpected error");

        dto = new ShiftDTO(shift.getId(), shift.getBranch(),
                shift.getStartTime(), shift.getEndTime(),
                shift.getRequiredRoles(), shift.getAvailableWorkers(),
                shift.getAssignedWorkers(), shift.getWorkerRoles());
        return dto.update();
    }

    public boolean unassignWorker(String workerId, int shiftId) {
        Util.throwIfNullOrEmpty(workerId);
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");

        workerFacade.requireHRManagerOrThrow();

        if (!shifts.containsKey(shiftId))
            throw new NoSuchElementException("Shift not found");

        if (workerFacade.getWorkerById(workerId) == null)
            throw new NoSuchElementException("Worker not found");

        Shift shift = shifts.get(shiftId);
        if (!shift.getAssignedWorkers().contains(workerId))
            throw new IllegalStateException("Worker is not assigned to this shift");

        if (!shift.removeAssignedWorker(workerId))
            throw new IllegalStateException("Unexpected error");

        if (!workerFacade.unassignWorker(workerId, shiftId))
            if (!shift.removeAssignedWorker(workerId))
                throw new IllegalStateException("Unexpected error, reverting changes failed");
            else
                throw new IllegalStateException("Unexpected error");

        dto = new ShiftDTO(shift.getId(), shift.getBranch(),
                shift.getStartTime(), shift.getEndTime(),
                shift.getRequiredRoles(), shift.getAvailableWorkers(),
                shift.getAssignedWorkers(), shift.getWorkerRoles());
        return dto.update();
    }

    public List<WorkerToSend> getAssignableWorkersForShift(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Illegal argument");

        workerFacade.requireHRManagerOrThrow();

        if (!shifts.containsKey(id))
            throw new NoSuchElementException("Shift not found");


        return shifts.get(id).getAvailableWorkers().stream().map(workerFacade::getWorkerById).collect(Collectors.toList());
    }

    public ShiftToSend getShift(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Illegal argument");

        if (!shifts.containsKey(id))
            throw new NoSuchElementException("Shift not found");

        return convertToShiftToSend(shifts.get(id));
    }

    public Map<String, Integer> getShiftRequiredWorkersOfRole(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Illegal argument");

        if (!shifts.containsKey(id))
            throw new NoSuchElementException("Shift not found");

        return shifts.get(id).getRequiredRoles();
    }

    public Map<String, Integer> getWorkerRolesByShift(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Illegal argument");

        if (!shifts.containsKey(id))
            throw new NoSuchElementException("Shift not found");

        return shifts.get(id).getWorkerRoles();
    }

    public boolean setShiftRequiredWorkersOfRole(int id, String role, int amount) {
        Util.throwIfNullOrEmpty(role);
        if (id < 0 || amount < 0)
            throw new IllegalArgumentException("Illegal argument");

        workerFacade.requireHRManagerOrThrow();

        if (!shifts.containsKey(id))
            throw new NoSuchElementException("Shift not found");

        if (roles.getId(role) == null)
            throw new NoSuchElementException("Role not found");

        Shift shift = shifts.get(id);
        shift.getRequiredRoles().put(role, amount);
        dto = new ShiftDTO(shift.getId(), shift.getBranch(),
                shift.getStartTime(), shift.getEndTime(),
                shift.getRequiredRoles(), shift.getAvailableWorkers(),
                shift.getAssignedWorkers(), shift.getWorkerRoles());
        return dto.update();
    }

    public int addNewShift(String start, String end, String branch) {
        Util.throwIfNullOrEmpty(start, end, branch);

        workerFacade.requireHRManagerOrThrow();

        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime))
            throw new DateTimeException("Invalid time range");

        Shift s = new Shift(nextId++, startTime, endTime, branch);
        shifts.put(s.getId(), s);
        dto = new ShiftDTO(s.getId(), s.getBranch(), s.getStartTime(), s.getEndTime(),
                s.getRequiredRoles(), s.getAvailableWorkers(), s.getAssignedWorkers(), s.getWorkerRoles());
        dto.insert();
        return s.getId();
    }

    public boolean addAvailability(String workerId, int shiftId) {
        Util.throwIfNullOrEmpty(workerId);
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");

        workerFacade.requireLoginOrThrow(workerId);

        if (!shifts.containsKey(shiftId))
            throw new NoSuchElementException("Shift not found");

        if (workerFacade.getWorkerById(workerId) == null)
            throw new NoSuchElementException("Worker not found");

        Shift shift = shifts.get(shiftId);
        if (!shift.addAvailableWorker(workerId))
            throw new IllegalStateException("Worker is already available for this shift");

        if (!workerFacade.addAvailability(workerId, shiftId))
            if (!shift.removeAvailableWorker(workerId))
                throw new IllegalStateException("Unexpected error, reverting changes failed");
            else
                throw new IllegalStateException("Unexpected error");

        dto = new ShiftDTO(shift.getId(), shift.getBranch(),
                shift.getStartTime(), shift.getEndTime(),
                shift.getRequiredRoles(), shift.getAvailableWorkers(),
                shift.getAssignedWorkers(), shift.getWorkerRoles());
        return dto.update();
    }

    public boolean removeAvailability(String workerId, int shiftId) {
        Util.throwIfNullOrEmpty(workerId);
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");

        workerFacade.requireLoginOrThrow(workerId);

        if (!shifts.containsKey(shiftId))
            throw new NoSuchElementException("Shift not found");

        if (workerFacade.getWorkerById(workerId) == null)
            throw new NoSuchElementException("Worker not found");

        Shift shift = shifts.get(shiftId);
        if (!shift.getAvailableWorkers().contains(workerId))
            throw new IllegalStateException("You are not available for this shift");

        if (shift.getAssignedWorkers().contains(workerId))
            throw new IllegalStateException("You are already assigned to this shift, contact your manager to unassign you");

        if (!workerFacade.removeAvailability(workerId, shiftId) || !shift.removeAvailableWorker(workerId))
            throw new IllegalStateException("Unexpected error");

        dto = new ShiftDTO(shift.getId(), shift.getBranch(),
                shift.getStartTime(), shift.getEndTime(),
                shift.getRequiredRoles(), shift.getAvailableWorkers(),
                shift.getAssignedWorkers(), shift.getWorkerRoles());
        return dto.update();
    }

    public boolean loadData() {
        workerFacade.loadData();
        shifts = dto.loadAll()
                .stream()
                .map(ShiftFacade::ShiftDTOtoShift)
                .collect(Collectors.toMap(Shift::getId, w -> w));
        nextId = shifts.values().stream().mapToInt(Shift::getId).max().orElse(0) + 1;
        return true;
    }

    private static Shift ShiftDTOtoShift(ShiftDTO sDTO) {
        return new Shift(
                sDTO.getId(),
                LocalDateTime.parse(sDTO.getStartTime().toString()),
                LocalDateTime.parse(sDTO.getEndTime().toString()),
                sDTO.getRequiredRoles(),
                sDTO.getAvailableWorkers(),
                sDTO.getAssignedWorkers(),
                sDTO.getWorkerRoles(),
                sDTO.getBranch()
        );
    }

    private static ShiftToSend convertToShiftToSend(Shift shift) {
        return new ShiftToSend(shift.getId(), shift.getStartTime().toString(), shift.getEndTime().toString(), shift.getBranch());
    }

    public List<ShiftToSend> getWorkerHistory(String id) {
        Util.throwIfNullOrEmpty(id);

        // Maybe check if the worker is logged in?

        return shifts.values()
                .stream()
                .filter(s -> s.getAssignedWorkers().contains(id))
                .map(ShiftFacade::convertToShiftToSend)
                .collect(Collectors.toList());
    }

    public List<ShiftToSend> getWorkerHistory(String id, String from, String to) {
        List<ShiftToSend> list = getWorkerHistory(id);

        // Maybe check if the worker is logged in?

        if (!Util.isValidDateTime(from, to))
            throw new IllegalArgumentException("Invalid date format");

        return list.stream().
                filter(s -> LocalDateTime.parse(s.endTime()).isBefore(LocalDateTime.parse(to))
                            && LocalDateTime.parse(s.startTime()).isAfter(LocalDateTime.parse(from)))
                .toList();
    }

    public List<ShiftToSend> getShiftsByBranchAndDate(String from, String to, String branchName) {
        Util.throwIfNullOrEmpty(from, to, branchName);
        if (!Util.isValidDateTime(from, to))
            throw new IllegalArgumentException("Illegal argument");

        return shifts
                .values()
                .stream()
                .filter(s -> s.getEndTime().isBefore(LocalDateTime.parse(to))
                             && s.getStartTime().isAfter(LocalDateTime.parse(from))
                             && s.getBranch().equals(branchName))
                .map(ShiftFacade::convertToShiftToSend)
                .collect(Collectors.toList());
    }


    /*
     * ============================================================================================
     * Testing methods
     * DO NOT USE IN PRODUCTION!
     * ============================================================================================
     */


    /**
     * Load test data into the DTO
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public List<ShiftToSend> getAllShifts() {
        return shifts.values().stream().map(ShiftFacade::convertToShiftToSend).collect(Collectors.toList());
    }

    /**
     * Reset the shifts map.
     * DEBUGGING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void reset(int safetyCode) {
        if (safetyCode != 0xC0FFEE)
            System.exit(-1);
        dto = new ShiftDTO();
        nextId = 0;
        shifts.clear();
        workerFacade.reset(safetyCode);
    }

    /**
     * Load test data into the DTO
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public ShiftFacade setTestMode(boolean testMode) {
        dto.setTestMode(testMode);
        return this;
    }

    /**
     * Clear the data in the DTO and the shifts map.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void clearData() {
        dto.deleteAll();
        shifts.clear();
    }

    /**
     * Get the DTO.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public DTO getDTO() {
        return dto;
    }

    /**
     * Set the DTO.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void setDTO(ShiftDTO shiftDTO) {
        this.dto = shiftDTO;
    }
}
