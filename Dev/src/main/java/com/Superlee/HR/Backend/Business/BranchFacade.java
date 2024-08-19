package com.Superlee.HR.Backend.Business;

import com.Superlee.HR.Backend.DataAccess.BranchDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BranchFacade {
    private static BranchFacade instance;

    private BranchDTO dto = new BranchDTO();
    private static final WorkerFacade workerFacade = WorkerFacade.getInstance();
    private static final Roles roles = Roles.getInstance();
    private Map<String, Branch> branches;

    private BranchFacade() {
        branches = new HashMap<>();
    }

    public static BranchFacade getInstance() {
        if (instance == null)
            instance = new BranchFacade();

        return instance;
    }

    public boolean addBranch(String name, String address, String manager) {
        Util.throwIfNullOrEmpty(name, address, manager);

        workerFacade.requireHRManagerOrThrow();

        WorkerToSend w = workerFacade.getWorkerById(manager);
        if (w == null)
            throw new NoSuchElementException("Manager does not exist");

        if (!w.roles().contains(roles.getId("Manager")))
            throw new IllegalArgumentException("Worker is not a manager");

        Branch branch = new Branch(name, address, manager);
        if (branches.containsKey(name))
            throw new IllegalArgumentException("Branch already exists");

        branches.put(name, branch);
        dto = new BranchDTO(name, address, manager);
        return dto.insert();
    }

    public BranchToSend getBranch(String name) {
        Util.throwIfNullOrEmpty(name);

        workerFacade.requireHRManagerOrThrow();

        if (!branches.containsKey(name))
            throw new NoSuchElementException("Branch not found");

        Branch branch = branches.get(name);
        return convertToBranchToSend(branch);
    }

    public boolean updateManager(String name, String manager) {
        Util.throwIfNullOrEmpty(name, manager);

        workerFacade.requireHRManagerOrThrow();

        WorkerToSend w = workerFacade.getWorkerById(manager);
        if (w == null)
            throw new NoSuchElementException("Manager does not exist");

        if (!w.roles().contains(roles.getId("Manager")))
            throw new IllegalArgumentException("Worker is not a manager");

        if (!branches.containsKey(name))
            throw new NoSuchElementException("Branch not found");

        if (branches.get(name).getManager().equals(manager))
            throw new IllegalArgumentException("Manager is already the manager of this branch");

        Branch b = branches.get(name);
        b.setManager(manager);
        dto = new BranchDTO(name, b.getAddress(), manager);
        return dto.update();
    }

    public List<BranchToSend> getAllBranches() {
        workerFacade.requireHRManagerOrThrow();
        return branches.values().stream().map(this::convertToBranchToSend).collect(Collectors.toList());
    }

    public boolean updateWorkerMainBranch(String id, String branch) {
        Util.throwIfNullOrEmpty(id, branch);

        if (!workerFacade.isLoggedInHRManager() && !workerFacade.isLoggedIn(id)) // customer question - who can do this?
            throw new UnpermittedOperationException("Operation requires login");

        if (!branches.containsKey(branch))
            throw new NoSuchElementException("Branch not found");

        return workerFacade.updateWorkerMainBranch(id, branch);
        // DTO will be updated in WorkerFacade
    }

    public boolean loadData() {
        branches = dto.loadAll()
                .stream()
                .map(branch -> new Branch(branch.getName(), branch.getAddress(), branch.getManager()))
                .collect(Collectors.toMap(Branch::getName, branch -> branch));
//        branches = BranchDTO
//                .getBranches()
//                .stream()
//                .map(branch -> new Branch(branch.getName(), branch.getAddress(), branch.getManager()))
//                .collect(Collectors.toMap(Branch::getName, branch -> branch)); TODO remove
        return true;
    }

    private BranchToSend convertToBranchToSend(Branch branch) {
        return new BranchToSend(branch.getName(), branch.getAddress(), branch.getManager());
    }


    /**
     * ============================================================================================
     * Testing methods
     * DO NOT USE IN PRODUCTION!
     * ============================================================================================
     */


    /**
     * Reset the branches map.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void reset(int safetyCode) {
        if (safetyCode != 0xC0FFEE)
            System.exit(-1);

        dto = new BranchDTO();
        workerFacade.reset(safetyCode);
        branches.clear();
    }

    /**
     * Set the test mode of the DTO.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public BranchFacade setTestMode(boolean testMode) {
        dto.setTestMode(testMode);
        return this;
    }

    /**
     * Clear the data in the DTO and the branches map.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void clearData() {
        dto.deleteAll();
        branches.clear();
    }

    /**
     * Get the DTO.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public BranchDTO getDTO() {
        return dto;
    }

    /**
     * Set the DTO.
     * TESTING PURPOSES ONLY
     * DO NOT USE IN PRODUCTION
     */
    public void setDTO(BranchDTO dto) {
        this.dto = dto;
    }
}
