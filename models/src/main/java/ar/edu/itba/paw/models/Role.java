package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Target;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {
    @Column(name = "rolename", length = 100)
    private String name;

    @Id
    @Column(name = "roleid")
    private int roleId;

    public Role(){

    }

    public Role(String name, int roleId) {
        this.name       = name;
        this.roleId   = roleId;
    }

    public String getName() {
        return name;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        Role aRole = ((Role) object);
        return getRoleId() == aRole.getRoleId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }
}

