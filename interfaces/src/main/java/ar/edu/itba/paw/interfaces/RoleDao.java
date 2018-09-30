package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Role;

import java.util.Optional;

public interface RoleDao {
    public Optional<Role> findRoleById(final int roleId);

    public Optional<Role> create(final String roleName, final int roleId);

    public boolean remove(final int roleId);

}
