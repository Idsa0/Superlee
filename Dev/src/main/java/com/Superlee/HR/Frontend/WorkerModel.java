package com.Superlee.HR.Frontend;

import java.util.List;

public record WorkerModel(String id, String firstname, String surname, String email, String phone, int salary,
                          List<Integer> roles,
                          String startDate, String contract, String branch) {
    @Override
    public String toString() {
        return "Worker " + id + " " + firstname + " " + surname;
    }

    public String fullDetails() {
        return "Worker " + id + " " + firstname + " " + surname + " " + email + " " + phone + " " + salary + " " + roles + " " + startDate + " " + contract + " " + branch;
    }
}
