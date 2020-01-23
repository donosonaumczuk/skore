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
}
