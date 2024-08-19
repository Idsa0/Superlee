package com.Superlee.HR.Backend.Business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class Worker {
    private final String id;
    private String firstname;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private String bankDetails;
    private int salary;
    private List<Integer> roles;
    private List<Integer> shifts;
    private List<Integer> availability;
    private final LocalDateTime startDate;
    private String contract;
    private String branch;

    Worker(String id, String firstname, String surname) {
        this(id, firstname, surname, "", "", id, "", 0, new ArrayList<>(), LocalDateTime.now(), "", "");
    }

    Worker(String id, String firstname, String surname, String email, String phone,
           String password, String bankDetails, int salary,
           List<Integer> roles, LocalDateTime startDate, String contract, String branch) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.bankDetails = bankDetails;
        this.salary = salary;
        this.roles = roles != null ? roles : new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.availability = new ArrayList<>();
        this.startDate = startDate;
        this.contract = contract;
        this.branch = branch;
    }

    Worker(String id, String firstname, String surname, String email, String phone,
           String password, String bankDetails, int salary,
           List<Integer> roles, List<Integer> shifts, List<Integer> availability, LocalDateTime startDate, String contract, String branch) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.bankDetails = bankDetails;
        this.salary = salary;
        this.roles = roles != null ? roles : new ArrayList<>();
        this.shifts = shifts != null ? shifts : new ArrayList<>();
        this.availability = availability != null ? availability : new ArrayList<>();
        this.startDate = startDate;
        this.contract = contract;
        this.branch = branch;
    }


    boolean assign(Integer shiftId) {
        if (shiftId != null && !shifts.contains(shiftId))
            return shifts.add(shiftId);
        return false;
    }

    boolean unassign(Integer shiftId) {
        if (shiftId != null)
            return shifts.remove(shiftId);
        return false;
    }

    boolean hasRole(int role) {
        return roles.contains(role);
    }

    boolean isAvailable(Integer shiftId) {
        return availability.contains(shiftId);
    }

    boolean addAvailability(Integer shiftId) {
        if (shiftId != null && !availability.contains(shiftId))
            return availability.add(shiftId);
        return false;
    }

    boolean removeAvailability(Integer shiftId) {
        if (shiftId != null)
            return availability.remove(shiftId);
        return false;
    }

    boolean isAssigned(Integer shiftId) {
        return shifts.contains(shiftId);
    }

    // Getters and setters

    String getId() {
        return id;
    }

    String getFirstName() {
        return firstname;
    }

    void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    String getSurname() {
        return surname;
    }

    void setSurname(String surname) {
        this.surname = surname;
    }

    String getFullName() {
        return firstname + " " + surname;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPhone() {
        return phone;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    String getBankDetails() {
        return bankDetails;
    }

    void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    int getSalary() {
        return salary;
    }

    void setSalary(int salary) {
        this.salary = salary;
    }

    List<Integer> getRoles() {
        return roles;
    }

    void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    List<Integer> getShifts() {
        return shifts;
    }

    void setShifts(List<Integer> shifts) {
        this.shifts = shifts;
    }

    List<Integer> getAvailability() {
        return availability;
    }

    void setAvailability(List<Integer> availability) {
        this.availability = availability;
    }

    LocalDateTime getStartDate() {
        return startDate;
    }

    String getContract() {
        return contract;
    }

    void setContract(String contract) {
        this.contract = contract;
    }

    void addRole(int role) {
        roles.add(role);
    }

    void setBranch(String branch) {
        this.branch = branch;
    }

    String getBranch() {
        return branch;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Worker w && w.getId().equals(this.getId());
    }
}
