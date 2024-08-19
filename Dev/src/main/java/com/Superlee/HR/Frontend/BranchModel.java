package com.Superlee.HR.Frontend;

public record BranchModel(String name, String address, String manager) {
    @Override
    public String toString() {
        return "Branch " + name + " at " + address + " managed by " + manager;
    }
}
