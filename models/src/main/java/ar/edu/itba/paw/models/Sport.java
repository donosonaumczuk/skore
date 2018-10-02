package ar.edu.itba.paw.models;


import java.util.Objects;

public class Sport {
    private String name;
    private int quantity;
    private String displayName;

    public Sport(String name, int quantity, String displayName) {
        this.name           = name;
        this.quantity       = quantity;
        this.displayName    = displayName;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Sport aSport = ((Sport) object);
        return getName().equals(aSport.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
