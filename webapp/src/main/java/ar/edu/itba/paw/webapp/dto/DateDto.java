package ar.edu.itba.paw.webapp.dto;

import java.time.LocalDate;

public class DateDto {

    private int year;
    private int monthNumber;
    private int dayOfMonth;

    public DateDto() {

    }

    private DateDto(LocalDate date) {
        this.year        = date.getYear();
        this.monthNumber = date.getMonthValue();
        this.dayOfMonth  = date.getDayOfMonth();
    }

    public static DateDto from(LocalDate date) {
        return new DateDto(date);
    }

    public int getYear() {
        return year;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}
