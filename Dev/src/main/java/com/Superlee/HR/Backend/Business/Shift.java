package com.Superlee.HR.Backend.Business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Shift {
    private final static Roles roles = Roles.getInstance();

    private final int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Map<String, Integer> requiredRoles;
    private List<String> availableWorkers;
    private List<String> assignedWorkers;
    private final Map<String, Integer> workerRoles;
    private String branch;

    public Shift(int id, LocalDateTime startTime, LocalDateTime endTime, String branch) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredRoles = new HashMap<>(roles.DEFAULT_SHIFT_ROLES);
        this.availableWorkers = new ArrayList<>();
        this.assignedWorkers = new ArrayList<>();
        this.workerRoles = new HashMap<>();
        this.branch = branch;
    }

    public Shift(int id, LocalDateTime startTime, LocalDateTime endTime, Map<String, Integer> requiredRoles, List<String> availableWorkers, List<String> assignedWorkers, Map<String, Integer> workerRoles, String branch) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredRoles = requiredRoles != null ? requiredRoles : new HashMap<>(roles.DEFAULT_SHIFT_ROLES);
        this.availableWorkers = availableWorkers != null ? availableWorkers : new ArrayList<>();
        this.assignedWorkers = assignedWorkers != null ? assignedWorkers : new ArrayList<>();
        this.workerRoles = workerRoles != null ? workerRoles : new HashMap<>();
        this.branch = branch;
    }

    public boolean addAvailableWorker(String worker) {
        if (!availableWorkers.contains(worker))
            return availableWorkers.add(worker);
        return false;
    }

    public boolean removeAvailableWorker(String worker) {
        return availableWorkers.remove(worker);
    }

    public boolean assignWorker(String worker, Integer role) {
        if (availableWorkers.contains(worker) && !assignedWorkers.contains(worker)) {
            workerRoles.put(worker, role);
            return assignedWorkers.add(worker);
        }
        return false;
    }

    public boolean removeAssignedWorker(String worker) {
        if (workerRoles.remove(worker) != null) {
            assignedWorkers = workerRoles.keySet().stream().toList();
            return true;
        }
        return false;
    }

    // TODO check if this is correct and if it is needed
    public boolean isFullyStaffed() {
        return requiredRoles.values().stream().allMatch((count) -> assignedWorkers.stream().filter((worker) -> workerRoles.get(worker).equals(count)).count() == count);
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public Map<String, Integer> getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(Map<String, Integer> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public List<String> getAvailableWorkers() {
        return availableWorkers;
    }

    public void setAvailableWorkers(List<String> availableWorkers) {
        this.availableWorkers = availableWorkers;
    }

    public List<String> getAssignedWorkers() {
        return assignedWorkers;
    }

    public void setAssignedWorkers(List<String> assignedWorkers) {
        this.assignedWorkers = assignedWorkers;
    }

    public Map<String, Integer> getWorkerRoles() {
        return workerRoles;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String name) {
        this.branch = name;
    }
}
