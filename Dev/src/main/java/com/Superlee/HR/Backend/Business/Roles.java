package com.Superlee.HR.Backend.Business;

import com.Superlee.HR.Backend.DataAccess.RolesDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class Roles {
    public final Map<String, Integer> DEFAULT_SHIFT_ROLES = new HashMap<>() {{
        put("Manager", 1); // Always require one manager
    }};

    private static Roles instance;
    private RolesDTO dto = new RolesDTO();
    private Map<String, Integer> roles = new HashMap<>();

    private Roles() {
        loadRoles();
    }

    static Roles getInstance() {
        if (instance == null)
            instance = new Roles();
        return instance;
    }

    String getName(int value) {
        for (var entry : roles.entrySet())
            if (entry.getValue().equals(value))
                return entry.getKey();
        return null;
    }

    Integer getId(String name) {
        return roles.get(name);
    }

    void loadRoles() {
        roles = dto.loadAll().stream()
                .collect(HashMap::new, (map, role) -> map.put(role.getName(), role.getValue()), HashMap::putAll);
    }

    void addNewRole(String name) {
        if (roles.containsKey(name))
            throw new IllegalArgumentException("Role already exists");

        int size = roles.size();
        roles.put(name, size + 1);
        dto = new RolesDTO(size + 1, name);
        dto.insert();
    }

    Set<String> getAllRoleNames() {
        return roles.keySet();
    }
}
