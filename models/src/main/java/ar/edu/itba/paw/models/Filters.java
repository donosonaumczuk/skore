package ar.edu.itba.paw.models;

import java.util.*;

public class Filters {
    private Map<String, Object> min;
    private Map<String, Object> max;
    private Map<String, List<String>> same;
    private Map<String, Object> minHaving;
    private Map<String, Object> maxHaving;

    public Filters() {
        min         = new HashMap<>();
        max         = new HashMap<>();
        minHaving   = new HashMap<>();
        maxHaving   = new HashMap<>();
        same        = new HashMap<>();
    }

    public void addMinFilter(String column, Object value) {
        min.put(column, value);
    }

    public void addMaxFilter(String column, Object value) {
        max.put(column, value);
    }

    public void addSameFilter(String column, List<String> values) {
        same.put(column, values);
    }

    public void addMinHavingFilter(String column, Object value) {
        minHaving.put(column, value);
    }

    public void addMaxHavingFilter(String column, Object value) {
        maxHaving.put(column, value);
    }

    public String generateQueryWhereMin(final List<Object> list) {
        Object aux;
        String query = "";
        for(String key: min.keySet()) {
            aux = min.get(key);
            if(aux != null) {
                list.add(aux);
                query = query + " AND ? <= " + key;
            }
        }
        return query;
    }

    public String generateQueryWhereMax(final List<Object> list) {
        Object aux;
        String query = "";
        for(String key: max.keySet()) {
            aux = max.get(key);
            if(aux != null) {
                list.add(aux);
                query = query + " AND " + key + " <= ?";
            }
        }
        return query;
    }

    public String generateQueryWhereSame(final List<Object> list) {
        List<String> aux;
        String query = "";
        for (String key: same.keySet()) {
            aux = same.get(key);
            if(aux.size() != 0) {
                Iterator iterator = aux.iterator();
                list.add(iterator.next());
                query = query + " AND (" + key + " = ?";

                while(iterator.hasNext()) {
                    list.add(iterator.next());
                    query = query + " OR " + key + " = ?";
                }
                query = query + ")";
            }
        }
        return query;
    }

    private String generateQueryHavingMin(List<Object> list) {
        Object aux;
        String query = "";
        boolean isFirst = true;
        for (String key: minHaving.keySet()) {
            aux = minHaving.get(key);
            if(aux != null) {
                list.add(aux);
                query = (isFirst)?" " : query + " AND ";
                query = query + "? <= " + key;
                isFirst = false;
            }
        }
        return query;
    }

    private String generateQueryHavingMax(List<Object> list) {
        Object aux;
        String query = "";
        boolean isFirst = true;
        for (String key: maxHaving.keySet()) {
            aux = maxHaving.get(key);
            if(aux != null) {
                list.add(aux);
                query = (isFirst)? " " : query + " AND ";
                query = query + key + " <= ?";
                isFirst = false;
            }
        }
        return query;
    }

    private String generateQueryHaving(final List<Object> list) {
        String query;
        if((!minHaving.isEmpty() && maxHaving.isEmpty()) ||
                (minHaving.isEmpty() && !maxHaving.isEmpty())) {
            query = " HAVING" + generateQueryHavingMin(list) + generateQueryHavingMax(list);
        } else if(!minHaving.isEmpty() && !maxHaving.isEmpty()) {
            query = " HAVING" + generateQueryHavingMin(list) + " AND" + generateQueryHavingMax(list);
        } else {
            query = "";
        }
        return query;
    }

    public String addToAListAndReturnQuery(final List<Object> list, final String groupBy) {
        String query = generateQueryWhereMin(list) + generateQueryWhereMax(list) +
                generateQueryWhereSame(list) + " " + groupBy +
                generateQueryHaving(list) + ";";
        return query;
    }
}
