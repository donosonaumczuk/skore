package ar.edu.itba.paw.webapp.utils;

public class QueryParamsUtils {

    public static Integer positiveIntegerOrElse(String s, Integer i) {
        Integer integer;
        try {
            integer = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
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
}
