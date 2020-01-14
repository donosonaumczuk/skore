package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Place;

public class PlaceDto {

    private String country;
    private String state;
    private String city;
    private String street;

    public PlaceDto() {

    }

    public PlaceDto(Place place) {
        this.country    = place.getCountry();
        this.state      = place.getState();
        this.city       = place.getCity();
        this.street     = place.getStreet();
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setStreet(final String street) {
        this.street = street;
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
