package com.Superlee.HR.Backend.Service;

import com.Superlee.HR.Backend.Business.UnpermittedOperationException;
import com.Superlee.HR.Backend.DataAccess.DataAccessException;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

public class HRService {
    private static HRService instance;
    private final ShiftService ss;
    private final WorkerService ws;
    private final BranchService bs;

    private HRService() {
        ss = ShiftService.getInstance();
        ws = WorkerService.getInstance();
        bs = BranchService.getInstance();
    }

    public static HRService getInstance() {
        if (instance == null)
            instance = new HRService();

        return instance;
    }

    /**
     * Get a list of all workers
     *
     * @return a list of all workers
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     */
    public String getAllWorkers() {
        return ws.getAllWorkers();
    }

    /**
     * Get a list of all workers with the specified role
     *
     * @param role the role to filter by
     * @return a list of all workers with the specified role
     * @throws IllegalArgumentException      if the role is null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the role is not found
     */
    public String getWorkersByRole(String role) {
        return ws.getWorkersByRole(role);
    }

    /**
     * Get all workers with the specified name
     *
     * @param firstname the first name of the worker
     * @param surname   the surname of the worker
     * @return all workers with the specified name
     * @throws IllegalArgumentException      if the first name or surname is null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     */
    public String getWorkersByName(String firstname, String surname) {
        return ws.getWorkersByName(firstname, surname);
    }

    /**
     * Get a worker with the specified id
     *
     * @param id the id of the worker
     * @return the worker with the specified id
     * @throws IllegalArgumentException if the id is null or empty
     * @throws NoSuchElementException   if the worker is not found
     */
    public String getWorkerById(String id) {
        return ws.getWorkerById(id);
    }

    /**
     * Add a new worker to the system
     *
     * @param id        the id of the worker
     * @param firstname the first name of the worker
     * @param surname   the surname of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException if the id, first name or surname is invalid or if the worker already exists
     */
    public String addNewWorker(String id, String firstname, String surname) {
        return ws.addNewWorker(id, firstname, surname);
    }

    /**
     * Get a list of all workers assigned to the specified shift
     *
     * @param id the id of the shift
     * @return a list of all workers assigned to the specified shift
     * @throws IllegalArgumentException if the id is negative
     * @throws NoSuchElementException   if the shift is not found
     */
    public String getWorkersByShift(int id) {
        return ss.getWorkersByShift(id);
    }

    /**
     * Assign a worker to a shift
     *
     * @param workerId the id of the worker
     * @param shiftId  the id of the shift
     * @param role     the role of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the worker id or role is null or empty or if the shift id is negative
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the shift, worker or role is not found
     * @throws IllegalStateException         if the worker is already assigned to the shift,
     *                                       if the worker is not available for the shift or
     *                                       if the worker does not have the required role
     *                                       if the worker is a driver and there is no storekeeper assigned to the shift
     */
    public String assignWorker(String workerId, int shiftId, String role) {
        return ss.assignWorker(workerId, shiftId, role);
    }

    /**
     * Unassign a worker from a shift
     *
     * @param workerId the id of the worker
     * @param shiftId  the id of the shift
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the worker id is null or empty or if the shift id is negative
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the shift or worker is not found
     * @throws IllegalStateException         if the worker is not assigned to the shift
     * @implNote If the worker is a storekeeper and there is a driver assigned to the shift,
     * we will not unassign the driver but will unassign the storekeeper
     */
    public String unassignWorker(String workerId, int shiftId) {
        return ss.unassignWorker(workerId, shiftId);
    }

    /**
     * Get the shift history of a worker
     *
     * @param Id the id of the worker
     * @return the shift history of a worker
     * @throws IllegalArgumentException if the id is null or empty
     * @throws NoSuchElementException   if the worker is not found
     */
    public String getWorkerHistory(String Id) {
        return ss.getWorkerHistory(Id);
    }

    /**
     * Get the shift history of a worker between two dates
     *
     * @param Id   the id of the worker
     * @param from the start date
     * @param to   the end date
     * @return the shift history of a worker between two dates
     * @throws IllegalArgumentException if the id is null or empty or if the date is invalid
     * @throws NoSuchElementException   if the worker is not found
     */
    public String getWorkerHistory(String Id, String from, String to) {
        return ss.getWorkerHistory(Id, from, to);
    }

    /**
     * Get a list of all shifts by a given branch and date
     *
     * @param branchName the name of the branch
     * @param from       the start date
     * @param to         the end date
     * @return a list of all shifts by the given branch and date
     * @throws IllegalArgumentException if the branch name is null or empty or if the date is invalid
     */
    public String getShiftsByBranchAndDate(String from, String to, String branchName) {
        return ss.getShiftsByBranchAndDate(from, to, branchName);
    }

    /**
     * Get all workers assignable to the specified shift
     *
     * @param id the id of the shift
     * @return all workers assignable to the specified shift
     * @throws IllegalArgumentException      if the id is negative
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the shift is not found
     */
    public String getAssignableWorkersForShift(int id) {
        return ss.getAssignableWorkersForShift(id);
    }

    /**
     * Get a shift with the specified id
     *
     * @param id the id of the shift
     * @return the shift with the specified id
     * @throws IllegalArgumentException if the id is negative
     * @throws NoSuchElementException   if the shift is not found
     */
    public String getShift(int id) {
        return ss.getShift(id);
    }

    /**
     * Set the amount of workers with the specified role needed for a shift
     *
     * @param id     the id of the shift
     * @param role   the role of the worker
     * @param amount the amount of workers needed
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the role is null or empty or if the amount or id is negative
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the shift or role is not found
     */
    public String setShiftRequiredWorkersOfRole(int id, String role, int amount) {
        return ss.setShiftRequiredWorkersOfRole(id, role, amount);
    }

    /**
     * Add a new shift to the system
     *
     * @param start  the start time of the shift
     * @param end    the end time of the shift
     * @param branch the branch of the shift
     * @return the id of the new shift
     * @throws IllegalArgumentException      if the start or end time is invalid
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws DateTimeException             if the start time is equal to or after the end time
     */
    public String addNewShift(String start, String end, String branch) {
        return ss.addNewShift(start, end, branch);
    }

    /**
     * Add an availability for a worker to a shift
     *
     * @param workerId the id of the worker
     * @param shiftId  the id of the shift
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the worker id is null or empty or if the shift id is negative
     * @throws UnpermittedOperationException if the logged-in user is not the worker with the specified id
     * @throws NoSuchElementException        if the worker or shift is not found
     * @throws IllegalStateException         if the worker is already available for the shift
     */
    public String addAvailability(String workerId, int shiftId) {
        return ss.addAvailability(workerId, shiftId);
    }

    /**
     * Remove an availability for a worker from a shift
     *
     * @param workerId the id of the worker
     * @param shiftId  the id of the shift
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the worker id is null or empty or if the shift id is negative
     * @throws UnpermittedOperationException if the logged-in user is not the worker with the specified id
     * @throws NoSuchElementException        if the worker or shift is not found
     * @throws IllegalStateException         if the worker is not available for the shift
     */
    public String removeAvailability(String workerId, int shiftId) {
        return ss.removeAvailability(workerId, shiftId);
    }

    /**
     * Get a list of all branches
     *
     * @return a list of all branches
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     */
    public String getAllBranches() {
        return bs.getAllBranches();
    }

    /**
     * Get a branch with the specified name
     *
     * @param name the name of the branch
     * @return the branch with the specified name
     * @throws IllegalArgumentException      if the name is null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the branch is not found
     */
    public String getBranch(String name) {
        return bs.getBranch(name);
    }

    /**
     * Add a new branch to the system
     *
     * @param name      the name of the branch
     * @param address   the address of the branch
     * @param managerId the id of the manager
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the arguments are invalid
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the manager is not found
     * @throws IllegalStateException         if the branch already exists or if the worker is not a manager
     */
    public String addNewBranch(String name, String address, String managerId) {
        return bs.addNewBranch(name, address, managerId);
    }

    /**
     * Updates the manager of a branch
     *
     * @param branchName the name of the branch
     * @param managerId  the id of the manager
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the arguments are invalid
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the branch or manager is not found
     * @throws IllegalStateException         if the worker is not a manager or if the manager is already the manager of the branch
     */
    public String updateBranchManager(String branchName, String managerId) {
        return bs.updateBranchManager(branchName, managerId);
    }

    /**
     * Updates the email of a worker
     *
     * @param id    the id of the worker
     * @param email the new email of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or email is null or empty or if the email is invalid
     * @throws UnpermittedOperationException if the logged-in user is not the worker with the specified id
     * @throws NoSuchElementException        if the worker is not found
     */
    public String updateWorkerEmail(String id, String email) {
        return ws.updateWorkerEmail(id, email);
    }

    /**
     * Updates the phone number of a worker
     *
     * @param id    the id of the worker
     * @param phone the new phone number of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or phone number is null or empty
     * @throws UnpermittedOperationException if the logged-in user is not the worker with the specified id
     * @throws NoSuchElementException        if the worker is not found
     */
    public String updateWorkerPhone(String id, String phone) {
        return ws.updateWorkerPhone(id, phone);
    }

    /**
     * Updates the password of a worker
     *
     * @param id       the id of the worker
     * @param password the new password of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or password is null or empty
     * @throws UnpermittedOperationException if the logged-in user is not the worker with the specified id
     * @throws NoSuchElementException        if the worker is not found
     */
    public String updateWorkerPassword(String id, String password) {
        return ws.updateWorkerPassword(id, password);
    }

    /**
     * Updates the bank details of a worker
     *
     * @param id          the id of the worker
     * @param bankDetails the new bank details of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or bank details are null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager and not the worker with the specified id
     * @throws NoSuchElementException        if the worker is not found
     */
    public String updateWorkerBankDetails(String id, String bankDetails) {
        return ws.updateWorkerBankDetails(id, bankDetails);
    }

    /**
     * Updates the salary of a worker
     *
     * @param id     the id of the worker
     * @param salary the new salary of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the salary is negative or if the id is null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the worker is not found
     */
    public String updateWorkerSalary(String id, int salary) {
        return ws.updateWorkerSalary(id, salary);
    }

    /**
     * Updates the contract details of a worker
     *
     * @param id              the id of the worker
     * @param contractDetails the new contract details of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or contract details are null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager and not the worker with the specified id
     * @throws NoSuchElementException        if the worker is not found
     */
    public String updateWorkerContractDetails(String id, String contractDetails) {
        return ws.updateWorkerContractDetails(id, contractDetails);
    }

    /**
     * Updates the main branch of a worker
     *
     * @param id     the id of the worker
     * @param branch the new main branch of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or branch is null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager and not the worker with the specified id
     * @throws NoSuchElementException        if the worker or branch is not found
     * @throws IllegalStateException         if the worker is already assigned to the branch
     */
    public String updateWorkerMainBranch(String id, String branch) {
        return bs.updateWorkerMainBranch(id, branch);
    }

    /**
     * Log into the system
     *
     * @param id       the id of the worker
     * @param password the password of the worker
     * @return the worker's details if successful
     * @throws IllegalArgumentException if the id or password is null or empty or if the password is incorrect
     * @throws NoSuchElementException   if the worker is not found
     * @throws IllegalStateException    if the worker is already logged in
     */
    public String login(String id, String password) {
        return ws.login(id, password);
    }

    /**
     * Log out of the system
     *
     * @param id the id of the worker
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id is null or empty
     * @throws UnpermittedOperationException if the user is not logged in as the worker with the specified id
     */
    public String logout(String id) {
        return ws.logout(id);
    }

    /**
     * Add a new role to a worker
     *
     * @param id   the id of the worker
     * @param role the role to add
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or role is null or empty or if the role already exists
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the worker is not found or if the role does not exist
     * @throws IllegalStateException         if the worker already has the role
     */
    public String addWorkerRole(String id, String role) {
        return ws.addWorkerRole(id, role);
    }

    /**
     * Remove a role from a worker
     *
     * @param id   the id of the worker
     * @param role the role to remove
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the id or role is null or empty or if the role does not exist
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     * @throws NoSuchElementException        if the worker is not found or if the role does not exist
     * @throws IllegalStateException         if the worker does not have the role
     * @deprecated NOT IMPLEMENTED
     */
    public String removeWorkerRole(String id, String role) {
        return ws.removeWorkerRole(id, role);
    }

    /**
     * Get all roles of a worker
     *
     * @param id the id of the worker
     * @return a set of all roles of the worker
     * @throws IllegalArgumentException if the id is null or empty
     * @throws NoSuchElementException   if the worker is not found
     */
    public String getWorkerRoles(String id) {
        return ws.getWorkerRoles(id);
    }

    /**
     * Get all roles
     *
     * @return a set of all roles
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     */
    public String getAllRoles() {
        return ws.getAllRoles();
    }

    /**
     * Add a new role to the system
     *
     * @param role the role to add
     * @return an empty response if successful
     * @throws IllegalArgumentException      if the role is null or empty or if the role already exists
     * @throws UnpermittedOperationException if the user is not logged in as the HR manager
     */
    public String addNewRole(String role) {
        return ws.addNewRole(role);
    }

    /**
     * Load the data from the database
     *
     * @return an empty response if successful
     * @throws DataAccessException if the data cannot be loaded
     */
    public String loadData() {
        String response = ss.loadData();
        if (!response.equals(Response.emptyResponseString))
            return response;
        response = ws.loadData();
        if (!response.equals(Response.emptyResponseString))
            return response;
        response = bs.loadData();
        return response;
    }
}
