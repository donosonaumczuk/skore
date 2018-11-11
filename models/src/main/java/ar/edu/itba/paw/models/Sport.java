package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "sports")
public class Sport {

    @Id
    @Column(length = 100)
    private String sportName;

    @Column
    private int playerQuantity;

    @Column(length = 100)
    private String displayName;

    @Column
    private byte[] imageSport;

    /* package */public Sport() {
        // For Hibernate
    }

    public Sport(String name, int quantity, String displayName, byte[] image) {
        this.sportName      = name;
        this.playerQuantity = quantity;
        this.displayName    = displayName;
        this.imageSport     = image;
    }

    public void setName(String name) {
        this.sportName = name;
    }

    public String getName() {
        return sportName;
    }

    public void setQuantity(int quantity) {
        this.playerQuantity = quantity;
    }

    public int getQuantity() {
        return playerQuantity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public byte[] getImage() {
        return imageSport;
    }

    public void setImage(byte[] image) {
        this.imageSport = image;
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
        return Objects.hash(sportName);
    }
}
