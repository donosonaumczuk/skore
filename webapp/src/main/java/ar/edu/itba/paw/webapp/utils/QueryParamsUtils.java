package ar.edu.itba.paw.webapp.utils;

import java.time.LocalDateTime;

public final class QueryParamsUtils {

    private QueryParamsUtils() {
        /* Utility class */
    }

    public static Integer positiveIntegerOrElse(String s, Integer i) {
        Integer integer;
        try {
            integer = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            integer = i;
        }
        return integer;
    }

    public static Integer positiveIntegerOrNull(String s) {
        return positiveIntegerOrElse(s, null);
    }

    private static Boolean booleanOrElse(String s, Boolean o) {
        Boolean bool = o;
        if (s != null) {
            if (s.equals("true")) {
                bool = true;
            } else if (s.equals("false")) {
                bool = false;
            }
        }
        return bool;
    }

    public static Boolean booleanOrNull(String s) {
        return booleanOrElse(s, null);
    }

    public static LocalDateTime localDateTimeOrElse(String s, LocalDateTime l) {
        if (s == null || !s.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
            return l;
        }
        return LocalDateTime.parse(s);
    }

    public static LocalDateTime localDateTimeOrNull(String s) {
        return localDateTimeOrElse(s, null);
    }
}
