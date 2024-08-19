package com.Superlee.HR.Backend.Business;

import java.time.LocalDateTime;

public class Util {
    private static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; // TODO check if this is correct
    private static final String idRegex = "[0-9]+";

    public static boolean isNullOrEmpty(String... strings) {
        for (String s : strings)
            if (s == null || s.isEmpty())
                return true;

        return false;
    }

    public static void throwIfNullOrEmpty(String... strings) {
        if (isNullOrEmpty(strings))
            throw new IllegalArgumentException("Illegal argument");
    }

    public static boolean isValidEmail(String email) {
        return email.matches(emailRegex);
    }

    public static boolean isValidId(String id) {
        return id.matches(idRegex);
    }

    public static boolean isValidDateTime(String... strings) {
        for (String s : strings)
            try {
                // TODO check if this is correct - specifically for empty or null strings
                LocalDateTime.parse(s);
            } catch (Exception e) {
                return false;
            }

        return true;
    }
}
