package ar.edu.itba.paw.models;

public class GameKey {

    private static final int URL_DATE_LENGTH = 12;
    private static final int MIN_TEAM_NAME1_LENGTH = 1;
    private static final int MIN_LENGTH = URL_DATE_LENGTH * 2 + MIN_TEAM_NAME1_LENGTH;

    private final String startTime;
    private final String teamName1;
    private final String finishTime;

    public GameKey(String keyString) {
        int length = keyString.length();

        if (keyString.length() < MIN_LENGTH) {
            this.startTime  = null;
            this.teamName1  = null;
            this.finishTime = null;
        }
        else {
            this.startTime  = keyDateToKeyDate(keyString.substring(0, URL_DATE_LENGTH));
            this.teamName1  = keyString.substring(URL_DATE_LENGTH, length - URL_DATE_LENGTH);
            this.finishTime = keyDateToKeyDate(keyString.substring(length - URL_DATE_LENGTH));
        }
    }

    private String keyDateToKeyDate(final String date) {
        StringBuilder formattedDate = new StringBuilder(date);

        formattedDate = formattedDate.insert(4, "-");
        formattedDate = formattedDate.insert(7, "-");
        formattedDate = formattedDate.insert(10, "T");
        formattedDate = formattedDate.insert(13, ":");
        formattedDate = formattedDate.insert(16, ":00");

        return formattedDate.toString();
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTeamName1() {
        return teamName1;
    }

    public String getFinishTime() {
        return finishTime;
    }
}
