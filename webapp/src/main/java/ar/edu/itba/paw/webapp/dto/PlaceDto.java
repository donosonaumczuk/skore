package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Place;

import java.util.Optional;

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

    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    public Optional<String> getStreet() {
        return Optional.ofNullable(street);
    }

    public Optional<String> getCountry() {
        return Optional.ofNullable(country);
    }
}
