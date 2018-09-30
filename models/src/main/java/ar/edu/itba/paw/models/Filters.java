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
        if(value != null && column != null &&
            (value.getClass() != String.class || ((String)value).compareTo("")!=0)) {
            min.put(column, value);
        }
    }

    public void addMaxFilter(String column, Object value) {
        if(value != null && column != null &&
            (value.getClass() != String.class || ((String)value).compareTo("")!=0)) {
            max.put(column, value);
        }
    }

    public void addSameFilter(String column, List<String> values) {
        if(values != null && values.size() !=0 && column != null) {
            same.put(column, values);
        }
    }

    public void addMinHavingFilter(String column, Object value) {
        if(value != null && column != null) {
            minHaving.put(column, value);
        }
    }

    public void addMaxHavingFilter(String column, Object value) {
        if(value != null && column != null) {
            maxHaving.put(column, value);
        }
    }

    public String generateQueryWhereMin(final List<Object> list) {
        String query = "";
        boolean isFirst = true;
        for(String key: min.keySet()) {
            list.add(min.get(key));
            query = (isFirst)? query: query + " AND";
            query = query + " ? <= " + key;
            isFirst = false;
        }
        return query;
    }

    public String generateQueryWhereMax(final List<Object> list) {
        String query = "";
        boolean isFirst = true;
        for(String key: max.keySet()) {
            list.add(max.get(key));
            query = (isFirst)? query: query + " AND";
            query = query + " " + key + " <= ?";
            isFirst = false;
        }
        return query;
    }

    public String generateQueryWhereSame(final List<Object> list) {
        String query = "";
        boolean isFirst = true;
        for (String key: same.keySet()) {
            Iterator iterator = same.get(key).iterator();
            list.add("%"+iterator.next()+"%");
            query = (isFirst)? query: query + " AND";
            query = query + " (" + key + " LIKE ?";

            while(iterator.hasNext()) {
                list.add("%"+iterator.next()+"%");
                query = query + " OR " + key + " LIKE ?";
            }
            query = query + ")";
            isFirst = false;
        }
        return query;
    }

    public String generateQueryHavingMin(List<Object> list) {
        String query = "";
        boolean isFirst = true;
        for (String key: minHaving.keySet()) {
            list.add(minHaving.get(key));
            query = (isFirst)?" " : query + " AND ";
            query = query + "? <= " + key;
            isFirst = false;
        }
        return query;
    }

    public String generateQueryHavingMax(List<Object> list) {
        String query = "";
        boolean isFirst = true;
        for (String key: maxHaving.keySet()) {
            list.add(maxHaving.get(key));
            query = (isFirst)? " " : query + " AND ";
            query = query + key + " <= ?";
            isFirst = false;
        }
        return query;
    }

    public String generateQueryHaving(final List<Object> list) {
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

    public String generateQueryWhere(final List<Object> list) {
        String query = generateQueryWhereMin(list);
        String query2 = generateQueryWhereMax(list);
        if(!query.equals("") && !query2.equals("")) {
            query = query + " AND";
        }
        query = query + query2;
        query2 = generateQueryWhereSame(list);
        if(!query.equals("") && !query2.equals("")) {
            query = query + " AND";
        }
        query = query + query2;
        return query;
    }
}
