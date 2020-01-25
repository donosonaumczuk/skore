package ar.edu.itba.paw.webapp.dto;

import java.time.LocalTime;

public class TimeDto {

    private int hour;
    private int minute;
    private int second;

    public TimeDto() {

    }

    private TimeDto(LocalTime time) {
        this.hour   = time.getHour();
        this.minute = time.getMinute();
        this.second = time.getSecond();
    }

    public static TimeDto from(LocalTime time) {
        return new TimeDto(time);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
