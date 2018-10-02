package ar.edu.itba.paw.models;

import java.util.Objects;

public class Place {
    private String country;
    private String state;
    private String city;
    private String street;

    public Place(String country, String state, String city, String street) {
        this.country    = country;
        this.state      = state;
        this.city       = city;
        this.street     = street;
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

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Place aPlace = ((Place) object);
        return getCountry().equals(aPlace.getCountry())
                && getCity().equals(aPlace.getCity())
                && getState().equals(aPlace.getState())
                && getStreet().equals(aPlace.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, state, city, street);
    }
}
