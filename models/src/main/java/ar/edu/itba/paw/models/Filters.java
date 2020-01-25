package ar.edu.itba.paw.models;

import java.util.LinkedList;
import java.util.List;

public class Filters {
    private StringBuilder start;
    private List<String> valueNames;
    private List<Object>  values;
    private boolean isFirst;

    static private String AND            = " AND ";
    static private String OR             = " OR ";
    static private char ESPACE           = ' ';
    static private char DOUBLE_DOT       = ':';
    static private char OPEN_PARENTHESE  = '(';
    static private char CLOSE_PARENTHESE = ')';
    static private String LOWER          = "lower";
    static private String NOT            = " NOT";
    static private String LIKE           = " LIKE ";
    static private String IN             = " IN ";
    static private String NOT_IN         = " NOT IN ";
    static private String NESTED_QUERY_1 =
            "(SELECT pu.userName " +
            "FROM Game g, Team t JOIN t.players p, PremiumUser pu " +
            "WHERE games = g AND g.primaryKey.team1.teamName = t.teamName AND pu.user.userId = p.userId)";
    static private String NESTED_QUERY_2 =
            "(SELECT pu.userName " +
            "FROM Game g, Team t JOIN t.players p, PremiumUser pu " +
            "WHERE games = g AND g.team2.teamName = t.teamName AND pu.user.userId = p.userId)";
    static private String FINISH_CONDITION_1 = "(games.result IS NOT NULL)";
    static private String FINISH_CONDITION_2 = "(games.result IS NULL)";

    public Filters(String start) {
        this.start      = new StringBuilder(start);
        this.isFirst    = true;
        this.valueNames = new LinkedList<>();
        this.values     = new LinkedList<>();
    }

    public void addFilter(String objectRepresentation, String operator, String valueName, Object value) {
        addFilter(objectRepresentation, operator, valueName, value, true);
    }

    private void addFilter(String objectRepresentation, String operator, String valueName, Object value, boolean putAnd) {
        if (value != null && (value.getClass() != String.class || ((String)value).compareTo("") != 0)) {
            valueNames.add(valueName);
            values.add(value);
            if (putAnd) {
                start = start.append(AND);
            }
            start = start.append(DOUBLE_DOT).append(valueName).append(ESPACE).append(operator).append(ESPACE)
                    .append(objectRepresentation);
        }
    }
    public void addFilter(String objectRepresentation, String operator, String valueName, List<String> values) {
        if (values != null && !values.isEmpty()) {
            start = start.append(AND).append(OPEN_PARENTHESE);
            int i = 0;
            for (String value: values) {
                if(i != 0) {
                    start = start.append(ESPACE).append(OR).append(ESPACE);
                }
                start = start.append(OPEN_PARENTHESE);
                addFilter(objectRepresentation, operator, valueName + i, value, false);
                i++;
                start = start.append(CLOSE_PARENTHESE);
            }
            start = start.append(CLOSE_PARENTHESE);
        }
    }

    private void addFilter(boolean isCaseSensitive, boolean isDifferent,
                           String objectRepresentation, int index, String valueName, String value) {
        if (value != null && !value.isEmpty()) {
            valueNames.add(valueName + index);
            if (!isCaseSensitive) {
                values.add(value);
            }
            else {
                values.add('%' + value + '%');
            }
            if (!isCaseSensitive) {
                start = start.append(LOWER).append(OPEN_PARENTHESE);
            }
            start = start.append(objectRepresentation);
            if (!isCaseSensitive) {
                start = start.append(CLOSE_PARENTHESE);
            }
            if (isDifferent) {
                start = start.append(NOT);
            }
            start = start.append(LIKE);
            if (!isCaseSensitive) {
                start = start.append(LOWER).append(OPEN_PARENTHESE);
            }
            start = start.append(DOUBLE_DOT).append(valueName).append(index);
            if (!isCaseSensitive) {
                start = start.append(CLOSE_PARENTHESE);
            }
        }
    }

    private void addFilter(boolean isInclude, String valueName, int index, String username) {
        if (username != null && !username.isEmpty()) {
            start = start.append(DOUBLE_DOT).append(valueName).append(index).append((isInclude) ? IN : NOT_IN)
                    .append(NESTED_QUERY_1).append((isInclude) ? OR : AND).append(ESPACE).append(DOUBLE_DOT)
                    .append(valueName).append(index + 1).append((isInclude) ? IN : NOT_IN).append(NESTED_QUERY_2);
            valueNames.add(valueName + index);
            values.add(username);
            valueNames.add(valueName + (index + 1));
            values.add(username);
        }
    }

    public void addListFilters(boolean isInclude, String valueName, List<String> players) {
        if(players != null && players.size() != 0) {
            start = start.append(AND).append(OPEN_PARENTHESE);

            int i = 0;
            for (String player: players) {
                if (i != 0) {
                    start = start.append((isInclude) ? OR : AND);
                }

                addFilter(isInclude, valueName, i, player);
                i = i + 2;
            }

            start = start.append(CLOSE_PARENTHESE);
        }
    }

    public void addListFilters(boolean isCaseSensitive, boolean isDifferent,
                               String objectRepresentation, String valueName, List<String> listOfValues) {
        if(listOfValues != null && listOfValues.size() != 0) {
            start = start.append(AND).append(OPEN_PARENTHESE);

            int i = 0;
            for(String o:listOfValues) {
                if(i != 0) {
                    start = start.append(OR);
                }

                addFilter(isCaseSensitive, isDifferent, objectRepresentation, i, valueName, o);
                i++;
            }

            start = start.append(CLOSE_PARENTHESE);
        }
    }

    public String toString() {
        return start.toString();
    }

    public List<String> getValueNames() {
        return valueNames;
    }

    public List<Object> getValues() {
        return values;
    }


    public void addFilterOnlyFinished(Boolean onlyWithResults) {
        if (onlyWithResults != null) {
            start = start.append(AND);
            if (onlyWithResults) {
                start = start.append(FINISH_CONDITION_1);
            }
            else {
                start = start.append(FINISH_CONDITION_2);
            }
        }
    }
}
