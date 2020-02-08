package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.List;

public class QueryList {

    private List<String> queryValues;

    public QueryList(String string) {
        String[] values = string.split(",");
        queryValues = Arrays.asList(values);
    }

    public QueryList(List<String> values) {
        queryValues = values;
    }

    public List<String> getQueryValues() {
        return queryValues;
    }
}
