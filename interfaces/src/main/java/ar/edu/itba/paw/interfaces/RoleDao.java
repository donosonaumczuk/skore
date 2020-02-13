package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Role;

import java.util.Optional;

public interface RoleDao {

    Optional<Role> findRoleById(final int roleId);

    Optional<Role> create(final String roleName, final int roleId);

    boolean remove(final int roleId);
}
