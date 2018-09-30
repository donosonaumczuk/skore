package ar.edu.itba.paw.models;

public class Role {
    private String name;
    private int roleId;

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
}

