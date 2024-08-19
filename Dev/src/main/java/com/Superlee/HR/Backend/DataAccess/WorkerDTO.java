package com.Superlee.HR.Backend.DataAccess;

import java.time.LocalDateTime;
import java.util.List;

public class WorkerDTO extends DTO {
    private String id;
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
    private LocalDateTime startDate;
    private String contract;
    private String branch;

    public WorkerDTO() {
        this.controller = new WorkerController(this).setTestMode(testMode);
    }

    public WorkerDTO(String id, String firstname, String surname, String email, String phone,
                     String password, String bankDetails, int salary,
                     List<Integer> roles, List<Integer> shifts, List<Integer> availability,
                     LocalDateTime startDate, String contract, String branch) {
        this();
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.bankDetails = bankDetails;
        this.salary = salary;
        this.roles = roles != null ? roles : List.of();
        this.shifts = shifts != null ? shifts : List.of();
        this.availability = availability != null ? availability : List.of();
        this.startDate = startDate;
        this.contract = contract;
        this.branch = branch;
    }

    public WorkerDTO(WorkerDTO other) {
        this();
        this.id = other.id;
        this.firstname = other.firstname;
        this.surname = other.surname;
        this.email = other.email;
        this.phone = other.phone;
        this.password = other.password;
        this.bankDetails = other.bankDetails;
        this.salary = other.salary;
        this.roles = other.roles;
        this.shifts = other.shifts;
        this.availability = other.availability;
        this.startDate = other.startDate;
        this.contract = other.contract;
        this.branch = other.branch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public List<Integer> getShifts() {
        return shifts;
    }

    public void setShifts(List<Integer> shifts) {
        this.shifts = shifts;
    }

    public List<Integer> getAvailability() {
        return availability;
    }

    public void setAvailability(List<Integer> availability) {
        this.availability = availability;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public List<WorkerDTO> loadAll() {
        return ((WorkerController) controller).loadAll();
    }
}
