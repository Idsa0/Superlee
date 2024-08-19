package com.Superlee.HR.Frontend;

public record ShiftModel(int id, String startTime, String endTime, String branch) {
    @Override
    public String toString() {
        return "Shift " + id + " from " + startTime + " to " + endTime;
    }
}
