package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class SportListDto {

    private final List<SportDto> sports;

    public SportListDto(List<SportDto> sports) {
        this.sports = sports;
    }

    public static SportListDto from(List<SportDto> sports) {
        return new SportListDto(sports);
    }

    public List<SportDto> getSports() {
        return sports;
    }
}
