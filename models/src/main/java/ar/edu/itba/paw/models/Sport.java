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
    @Column(length = 100, name = "sportName")
    private String name;

    @Column(name = "playerQuantity")
    private int quantity;

    @Column(length = 100, name = "dispayName")
    private String displayName;

    @Column(name = "imageSport")
    private byte[] image;

    /* package */public Sport() {
        // For Hibernate
    }

    public Sport(String name, int quantity, String displayName, byte[] image) {
        this.name           = name;
        this.quantity       = quantity;
        this.displayName    = displayName;
        this.image          = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
