package ar.edu.itba.paw.models;


public class Sport {
    private String name;
    private int quantity;
    private String displayName;

    public Sport(String name, int quantity, String displayName) {
        this.name           = name;
        this.quantity       = quantity;
        this.displayName    = displayName;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Sport aSport = ((Sport) object);
        return getName().equals(aSport.getName());
    }
}
