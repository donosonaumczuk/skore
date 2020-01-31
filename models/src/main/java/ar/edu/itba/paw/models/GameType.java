package ar.edu.itba.paw.models;

public enum GameType {

    GROUP("Group"),
    INDIVIDUAL("Individual"),
    COMPETITIVE("Competitive"),
    FRIENDLY("Friendly");

    private String string;

    GameType(String string) {
        this.string = string;
    }

    public String toString() {
        return string;
    }
}
