package com.Superlee.HR.Backend.DataAccess;

import java.util.List;

public class RolesDTO extends DTO {
    private int value;
    private String name;

    public RolesDTO() {
        this.controller = new RolesController(this).setTestMode(testMode);
    }

    public RolesDTO(int value, String name) {
        this();
        this.value = value;
        this.name = name;
    }

    public RolesDTO(RolesDTO other) {
        this();
        this.value = other.getValue();
        this.name = other.getName();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<RolesDTO> loadAll() {
        return ((RolesController) controller).loadAll();
    }
}
