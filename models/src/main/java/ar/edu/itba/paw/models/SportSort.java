package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.List;

public class SportSort extends Sort {

    static private List<String> queryFields = Arrays.asList("s.playerQuantity", "s.displayName");
    static private List<String> validFields = Arrays.asList("quantity", "name");

    public SportSort(String string) {
        super(string, validFields, queryFields);
    }
}
