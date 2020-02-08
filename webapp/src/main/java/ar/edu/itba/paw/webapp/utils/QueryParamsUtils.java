package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.QueryList;

import java.time.LocalDateTime;
import java.util.List;

public class QueryParamsUtils {

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

    public static List<String> getQueryListOrNull(QueryList list) {
        return list != null ? list.getQueryValues() : null;
    }
}
