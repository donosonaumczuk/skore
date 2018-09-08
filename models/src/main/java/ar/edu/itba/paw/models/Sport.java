package ar.edu.itba.paw.models;


public class Sport {
    private String name;
    private int quantity;

    public Sport(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
