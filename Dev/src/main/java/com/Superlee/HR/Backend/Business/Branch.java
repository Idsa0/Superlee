package com.Superlee.HR.Backend.Business;

public class Branch {
    private String name;
    private String address;
    private String manager;

    public Branch(String name, String address, String manager) {
        this.name = name;
        this.address = address;
        this.manager = manager;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
