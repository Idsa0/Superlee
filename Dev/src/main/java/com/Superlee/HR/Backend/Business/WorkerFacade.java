package com.Superlee.HR.Backend.Business;

import com.Superlee.HR.Backend.DataAccess.WorkerDTO;

import java.util.*;
import java.util.stream.Collectors;

public class WorkerFacade {
    private static WorkerFacade instance;

    private Map<String, Worker> workers;
    private final Roles roles = Roles.getInstance();
    private WorkerDTO dto = new WorkerDTO();
    private static Worker loggedInWorker;

    private WorkerFacade() {
        workers = new HashMap<>();
    }

    public static WorkerFacade getInstance() {
        if (instance == null)
            instance = new WorkerFacade();
        return instance;
    }

    public List<WorkerToSend> getAllWorkers() {
        requireHRManagerOrThrow();
        return workers.values().stream().map(WorkerFacade::convertToWorkerToSend).collect(Collectors.toList());
    }

    public List<WorkerToSend> getWorkersByRole(String role) {
        Util.throwIfNullOrEmpty(role);

        requireHRManagerOrThrow();

        if (roles.getId(role) == null)
            throw new NoSuchElementException("Role not found");

        return workers.values().stream()
                .filter(worker -> worker.getRoles().stream().anyMatch(r -> r.equals(roles.getId(role))))
                .map(WorkerFacade::convertToWorkerToSend)
                .collect(Collectors.toList());
    }

    public List<WorkerToSend> getWorkersByName(String firstname, String surname) {
        Util.throwIfNullOrEmpty(firstname, surname);

        requireHRManagerOrThrow();

        return workers.values().stream()
                .filter(worker -> worker.getFirstName().equalsIgnoreCase(firstname) && worker.getSurname().equalsIgnoreCase(surname))
                .map(WorkerFacade::convertToWorkerToSend)
                .collect(Collectors.toList());
    }

    public WorkerToSend getWorkerById(String id) {
        Util.throwIfNullOrEmpty(id);

        Worker worker = workers.get(id);
        if (worker == null)
            throw new NoSuchElementException("Worker not found");

        return convertToWorkerToSend(worker);
    }

    public boolean assignWorker(String workerId, int shiftId, String role) {
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");
        Util.throwIfNullOrEmpty(workerId, role);

        requireHRManagerOrThrow();

        Worker worker = workers.get(workerId);
        if (worker == null)
            throw new NoSuchElementException("Worker not found");

        if (!worker.hasRole(roles.getId(role)))
            throw new IllegalStateException("Worker does not have the required role");

        if (worker.isAssigned(shiftId))
            throw new IllegalStateException("Worker is already assigned to this shift");

        if (!worker.isAvailable(shiftId))
            throw new IllegalStateException("Worker is not available for this shift");

        worker.assign(shiftId);
        dto = new WorkerDTO(worker.getId(), worker.getFirstName(), worker.getSurname(), worker.getEmail(),
                worker.getPhone(), worker.getPassword(), worker.getBankDetails(), worker.getSalary(),
                worker.getRoles(), worker.getShifts(), worker.getAvailability(), worker.getStartDate(),
                worker.getContract(), worker.getBranch());
        return dto.update();
    }

    public boolean unassignWorker(String workerId, int shiftId) {
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");
        Util.throwIfNullOrEmpty(workerId);

        requireHRManagerOrThrow();

        Worker worker = workers.get(workerId);
        if (worker == null)
            throw new NoSuchElementException("Worker not found");

        if (!worker.isAssigned(shiftId))
            throw new IllegalStateException("Worker is not assigned to this shift");

        worker.unassign(shiftId);
        dto = new WorkerDTO(worker.getId(), worker.getFirstName(), worker.getSurname(), worker.getEmail(),
                worker.getPhone(), worker.getPassword(), worker.getBankDetails(), worker.getSalary(),
                worker.getRoles(), worker.getShifts(), worker.getAvailability(), worker.getStartDate(),
                worker.getContract(), worker.getBranch());
        return dto.update();
    }

    public List<Integer> getWorkerShifts(String id) {
        Util.throwIfNullOrEmpty(id);
        if (workers.get(id) == null)
            throw new IllegalArgumentException("Worker dose not exists");

        return workers.get(id).getShifts();
    }

    public List<Integer> getWorkerAvailability(String id) {
        Util.throwIfNullOrEmpty(id);
        if (workers.get(id) == null)
            throw new IllegalArgumentException("Worker dose not exists");

        return workers.get(id).getAvailability();
    }

    public String getWorkerBankDetails(String id) {
        Util.throwIfNullOrEmpty(id);
        if (workers.get(id) == null)
            throw new IllegalArgumentException("Worker dose not exists");

        return workers.get(id).getBankDetails();
    }

    public boolean addNewWorker(String id, String firstname, String surname) {
        Util.throwIfNullOrEmpty(id, firstname, surname);

        requireHRManagerOrThrow();

        if (workers.get(id) != null)
            throw new IllegalArgumentException("Worker already exists");

        if (!Util.isValidId(id))
            throw new IllegalArgumentException("Invalid id");

        if (firstname.length() <= 1 || surname.length() <= 1)
            throw new IllegalArgumentException("Invalid name");

        Worker worker = new Worker(id, firstname, surname);
        workers.put(worker.getId(), worker);
        dto = new WorkerDTO(worker.getId(), worker.getFirstName(), worker.getSurname(), worker.getEmail(),
                worker.getPhone(), worker.getId(), worker.getBankDetails(), worker.getSalary(),
                worker.getRoles(), worker.getShifts(), worker.getAvailability(), worker.getStartDate(),
                worker.getContract(), worker.getBranch());
        return dto.insert();
    }

    public boolean addWorkerRole(String id, String role) {
        Util.throwIfNullOrEmpty(id, role);

        requireHRManagerOrThrow();

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        if (roles.getId(role) == null)
            throw new NoSuchElementException("Role not found");

        if (w.hasRole(roles.getId(role)))
            throw new IllegalStateException("Worker already has this role");

        w.addRole(roles.getId(role));
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public boolean addAvailability(String workerId, int shiftId) {
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");
        Util.throwIfNullOrEmpty(workerId);

        requireLoginOrThrow(workerId);

        Worker worker = workers.get(workerId);

        if (worker == null)
            throw new NoSuchElementException("Worker not found");

        if (worker.isAvailable(shiftId))
            throw new IllegalStateException("Worker is already available for this shift");

        worker.addAvailability(shiftId);
        dto = new WorkerDTO(worker.getId(), worker.getFirstName(), worker.getSurname(), worker.getEmail(),
                worker.getPhone(), worker.getPassword(), worker.getBankDetails(), worker.getSalary(),
                worker.getRoles(), worker.getShifts(), worker.getAvailability(), worker.getStartDate(),
                worker.getContract(), worker.getBranch());
        return dto.update();
    }

    public boolean removeAvailability(String workerId, int shiftId) {
        if (shiftId < 0)
            throw new IllegalArgumentException("Illegal argument");
        Util.throwIfNullOrEmpty(workerId);

        requireLoginOrThrow(workerId);

        Worker worker = workers.get(workerId);
        if (worker == null)
            throw new NoSuchElementException("Worker not found");

        if (!worker.isAvailable(shiftId))
            throw new IllegalStateException("Worker is not available for this shift");

        worker.removeAvailability(shiftId);
        dto = new WorkerDTO(worker.getId(), worker.getFirstName(), worker.getSurname(), worker.getEmail(),
                worker.getPhone(), worker.getPassword(), worker.getBankDetails(), worker.getSalary(),
                worker.getRoles(), worker.getShifts(), worker.getAvailability(), worker.getStartDate(),
                worker.getContract(), worker.getBranch());
        return dto.update();
    }

    public boolean updateWorkerEmail(String id, String email) {
        Util.throwIfNullOrEmpty(id, email);

        requireLoginOrThrow(id);

        if (!Util.isValidEmail(email))
            throw new IllegalArgumentException("Invalid email");

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        w.setEmail(email);
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public boolean updateWorkerPhone(String id, String phone) {
        Util.throwIfNullOrEmpty(id, phone);

        requireLoginOrThrow(id);

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        w.setPhone(phone);
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public boolean updateWorkerSalary(String id, int salary) {
        if (salary < 0)
            throw new IllegalArgumentException("Illegal argument");
        Util.throwIfNullOrEmpty(id);

        requireHRManagerOrThrow();

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        w.setSalary(salary);
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public boolean updateWorkerContractDetails(String id, String contractDetails) {
        Util.throwIfNullOrEmpty(id, contractDetails);

        if (!isLoggedInHRManager() && !isLoggedIn(id)) // customer question - who can do this?
            throw new UnpermittedOperationException("Operation requires login");

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        w.setContract(contractDetails);
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public boolean updateWorkerBankDetails(String id, String bankDetails) {
        Util.throwIfNullOrEmpty(id, bankDetails);

        if (!isLoggedInHRManager() && !isLoggedIn(id)) // customer question - who can do this?
            throw new UnpermittedOperationException("Operation requires login");

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        w.setBankDetails(bankDetails);
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public boolean updateWorkerPassword(String id, String password) {
        Util.throwIfNullOrEmpty(id, password);

        requireLoginOrThrow(id);

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        w.setPassword(password);
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public boolean updateWorkerMainBranch(String id, String branch) {
        Util.throwIfNullOrEmpty(id, branch);

        if (!isLoggedInHRManager() && !isLoggedIn(id)) // customer question - who can do this?
            throw new UnpermittedOperationException("Operation requires login");

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        if (w.getBranch().equals(branch))
            throw new IllegalStateException("Worker is already assigned to this branch");

        w.setBranch(branch);
        dto = new WorkerDTO(w.getId(), w.getFirstName(), w.getSurname(), w.getEmail(),
                w.getPhone(), w.getPassword(), w.getBankDetails(), w.getSalary(),
                w.getRoles(), w.getShifts(), w.getAvailability(), w.getStartDate(),
                w.getContract(), w.getBranch());
        return dto.update();
    }

    public String getRoleName(int value) {
        return roles.getName(value);
    }

    public int getRoleId(String name) {
        return roles.getId(name);
    }

    public WorkerToSend login(String id, String password) {
        Util.throwIfNullOrEmpty(id, password);

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        if (w.equals(loggedInWorker)) // we can also check that no one else is logged in
            throw new IllegalStateException("Worker already logged in");

        if (!w.getPassword().equals(password))
            throw new IllegalArgumentException("Invalid password");

        loggedInWorker = w;

        return convertToWorkerToSend(w);
    }

    public boolean logout(String id) {
        Util.throwIfNullOrEmpty(id);
        requireLoginOrThrow(id);
        loggedInWorker = null;
        return true;
    }

    public boolean isLoggedIn(String id) {
        return loggedInWorker != null && loggedInWorker.getId().equals(id);
    }

    public void requireLoginOrThrow(String id) {
        if (!isLoggedIn(id))
            throw new UnpermittedOperationException("Operation requires login");
    }

    public boolean isLoggedInHRManager() {
        return loggedInWorker != null && loggedInWorker.hasRole(0);
    }

    public void requireHRManagerOrThrow() {
        if (!isLoggedInHRManager())
            throw new UnpermittedOperationException("Operation requires HR manager");
    }

    private static WorkerToSend convertToWorkerToSend(Worker worker) {
        return new WorkerToSend(
                worker.getId(), worker.getFirstName(), worker.getSurname(), worker.getEmail(),
                worker.getPhone(), worker.getSalary(), worker.getRoles(),
                worker.getStartDate().toString(), worker.getContract(), worker.getBranch());
    }

    public boolean loadData() {
        workers = dto.loadAll()
                .stream()
                .map(WorkerFacade::WorkerDTOtoWorker)
                .collect(Collectors.toMap(Worker::getId, w -> w));
        return true;
    }

    private static Worker WorkerDTOtoWorker(WorkerDTO wDTO) {
        return new Worker(
                wDTO.getId(),
                wDTO.getFirstname(),
                wDTO.getSurname(),
                wDTO.getEmail(),
                wDTO.getPhone(),
                wDTO.getPassword(),
                wDTO.getBankDetails(),
                wDTO.getSalary(),
                wDTO.getRoles(),
                wDTO.getShifts(),
                wDTO.getAvailability(),
                wDTO.getStartDate(),
                wDTO.getContract(),
                wDTO.getBranch()
        );
    }

    public boolean removeWorkerRole(String id, String role) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<String> getWorkerRoles(String id) {
        Util.throwIfNullOrEmpty(id);

        requireHRManagerOrThrow(); // should we allow workers to get their own roles?

        Worker w = workers.get(id);
        if (w == null)
            throw new NoSuchElementException("Worker not found");

        return w.getRoles().stream().map(roles::getName).collect(Collectors.toList());
    }

    public Set<String> getAllRoles() {
        requireHRManagerOrThrow();
        return roles.getAllRoleNames();
    }

    public boolean addNewRole(String role) {
        Util.throwIfNullOrEmpty(role);

        requireHRManagerOrThrow();

        if (roles.getId(role) != null)
            throw new IllegalArgumentException("Role already exists");

        roles.addNewRole(role);
        return true;
    }

    /*
     * ============================================================================================
     * Testing methods
     * DO NOT USE IN PRODUCTION!
     * ============================================================================================
     */

    /**
     * Reset the workers map.
     * DEBUGGING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void reset(int safetyCode) {
        if (safetyCode != 0xC0FFEE)
            System.exit(-1);

        dto = new WorkerDTO();
        loggedInWorker = null;

        workers.clear();
    }

    /**
     * Fake login for testing purposes only.
     * DO NOT USE IN PRODUCTION
     */
    public void fakeLogin(boolean isHRManager, String id) {
        loggedInWorker = fakeCreateWorker(isHRManager, id);
    }

    /**
     * Fake create worker for testing purposes only.
     * DO NOT USE IN PRODUCTION
     */
    public Worker fakeCreateWorker(boolean isHRManager, String id) {
        Worker w = new Worker(id, "Super", "Man");
        w.addRole(roles.getId(isHRManager ? "HRManager" : "Cashier"));
        workers.put(w.getId(), w);
        return w;
    }

    /**
     * Fake logout for testing purposes only.
     * DO NOT USE IN PRODUCTION
     */
    public void fakeLogout() {
        loggedInWorker = null;
    }

    /**
     * Fake add worker for testing purposes only.
     * DO NOT USE IN PRODUCTION
     */
    public WorkerFacade setTestMode(boolean testMode) {
        dto.setTestMode(testMode);
        return this;
    }

    /**
     * Clear the data in the DTO and the workers map.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void clearData() {
        dto.deleteAll();
        workers.clear();
    }

    /**
     * Get the DTO.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public WorkerDTO getDTO() {
        return dto;
    }

    /**
     * Set the DTO.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void setDTO(WorkerDTO dto) {
        this.dto = dto;
    }
}
