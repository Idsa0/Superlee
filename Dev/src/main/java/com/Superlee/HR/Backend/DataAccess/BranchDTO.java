package com.Superlee.HR.Backend.DataAccess;

import java.util.List;

public class BranchDTO extends DTO {
    private String name;
    private String address;
    private String manager;

    public BranchDTO() {
        this.controller = new BranchController(this).setTestMode(testMode);
    }

    public BranchDTO(String name, String address, String manager) {
        this();
        this.name = name;
        this.address = address;
        this.manager = manager;
    }

    public BranchDTO(BranchDTO other) {
        this();
        this.name = other.name;
        this.address = other.address;
        this.manager = other.manager;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getManager() {
        return manager;
    }

    @Override
    public List<BranchDTO> loadAll() {
        return ((BranchController) controller).loadAll();
    }
}
