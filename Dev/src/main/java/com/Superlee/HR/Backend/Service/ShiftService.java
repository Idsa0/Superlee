package com.Superlee.HR.Backend.Service;

import com.Superlee.HR.Backend.Business.ShiftFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ShiftService {
    private static ShiftService instance;
    private final ShiftFacade sf;
    private final Gson gson;

    private ShiftService() {
        sf = ShiftFacade.getInstance();
        gson = new GsonBuilder().serializeNulls().create();
    }

    public static ShiftService getInstance() {
        if (instance == null)
            instance = new ShiftService();

        return instance;
    }

    public String getWorkersByShift(int id) {
        try {
            return gson.toJson(new Response(sf.getWorkersByShift(id)));
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String assignWorker(String workerId, int shiftId, String role) {
        try {
            sf.assignWorker(workerId, shiftId, role);
            return gson.toJson(new Response());
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String unassignWorker(String workerId, int shiftId) {
        try {
            sf.unassignWorker(workerId, shiftId);
            return gson.toJson(new Response());
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String getAssignableWorkersForShift(int id) {
        try {
            return gson.toJson(new Response(sf.getAssignableWorkersForShift(id)));
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String getShift(int id) {
        try {
            return gson.toJson(new Response(sf.getShift(id)));
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String setShiftRequiredWorkersOfRole(int id, String role, int amount) {
        try {
            sf.setShiftRequiredWorkersOfRole(id, role, amount);
            return gson.toJson(new Response());
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String addNewShift(String start, String end, String branch) {
        try {
            return gson.toJson(new Response(sf.addNewShift(start, end, branch)));
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String addAvailability(String workerId, int shiftId) {
        try {
            sf.addAvailability(workerId, shiftId);
            return gson.toJson(new Response());
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String loadData() {
        try {
            sf.loadData();
            return gson.toJson(new Response());
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String removeAvailability(String workerId, int shiftId) {
        try {
            sf.removeAvailability(workerId, shiftId);
            return gson.toJson(new Response());
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String getWorkerHistory(String id) {
        try {
            return gson.toJson(new Response(sf.getWorkerHistory(id)));
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String getWorkerHistory(String id, String from, String to) {
        try {
            return gson.toJson(new Response(sf.getWorkerHistory(id, from, to)));
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }

    public String getShiftsByBranchAndDate(String from, String to, String branchName) {
        try {
            return gson.toJson(new Response(sf.getShiftsByBranchAndDate(from, to, branchName)));
        } catch (Exception ex) {
            return gson.toJson(new Response(ex.getMessage()));
        }
    }
}
