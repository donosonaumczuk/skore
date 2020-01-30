package ar.edu.itba.paw.models;

import java.time.LocalDateTime;

public class GameKey {

    private static final int URL_DATE_LENGTH = 12;
    private static final int MIN_TEAM_NAME1_LENGTH = 1;
    private static final int MIN_LENGTH = URL_DATE_LENGTH * 2 + MIN_TEAM_NAME1_LENGTH;

    private final LocalDateTime startTime;
    private final String teamName1;
    private final LocalDateTime finishTime;

    public GameKey(String keyString) {
        if (keyString == null) {
            throw new IllegalArgumentException("Match key must not be null");
        }
        int length = keyString.length();

        if (keyString.length() < MIN_LENGTH) {
            this.startTime  = null;
            this.teamName1  = null;
            this.finishTime = null;
        }
        else {
            this.startTime  = LocalDateTime.parse(keyDateToKeyDate(keyString.substring(0, URL_DATE_LENGTH)));
            this.teamName1  = keyString.substring(URL_DATE_LENGTH, length - URL_DATE_LENGTH);
            this.finishTime = LocalDateTime.parse(keyDateToKeyDate(keyString.substring(length - URL_DATE_LENGTH)));
        }
    }

    public GameKey(LocalDateTime startTime, String teamName1, LocalDateTime finishTime) {
        this.startTime  = startTime;
        this.teamName1  = teamName1;
        this.finishTime = finishTime;
    }

    public String toString() {
        return dateToString(startTime) + teamName1 + dateToString(finishTime);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getTeamName1() {
        return teamName1;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    private String dateToString(LocalDateTime localDateTime) {
        return new StringBuilder().append(localDateTime.getYear())
                .append(localDateTime.getMonthValue()).append(localDateTime.getDayOfMonth())
                .append(localDateTime.getHour()).append(localDateTime.getMinute()).toString();
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
}
