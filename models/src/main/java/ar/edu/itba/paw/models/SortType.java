package ar.edu.itba.paw.models;

import java.util.Optional;

public enum SortType {

    ASCENDANT("asc"),
    DESCENDANT("desc");

    private String string;

    SortType(String string) {
        this.string = string;
    }

    public static Optional<SortType> from(String string) {
        if(string.equals(ASCENDANT.toString().toLowerCase())) {
            return Optional.ofNullable(ASCENDANT);
        }
        else if (string.equals(DESCENDANT.toString().toLowerCase())) {
            return Optional.ofNullable(DESCENDANT);
        }
        return Optional.empty();
    }

    public String toString() {
        return string;
    }
}
