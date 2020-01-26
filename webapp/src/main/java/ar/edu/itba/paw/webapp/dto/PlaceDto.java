package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Place;

public class PlaceDto {

    private String country;
    private String state;
    private String city;
    private String street;

    private PlaceDto() {
        /* Required by JSON object mapper */
    }

    private PlaceDto(Place place) {
        this.country    = place.getCountry();
        this.state      = place.getState();
        this.city       = place.getCity();
        this.street     = place.getStreet();
    }

    public static PlaceDto from(Place place) {
        return new PlaceDto(place);
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getCountry() {
        return country;
    }
}
