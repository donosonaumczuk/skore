package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.List;

public class UserSort extends Sort {

    static private List<String> queryFields = Arrays.asList("u.reputation", "u.username");//TODO: add winrate
    static private List<String> validFields = Arrays.asList("reputation", "username");

    public UserSort(String string) {
        super(string, validFields, queryFields);
    }
}
