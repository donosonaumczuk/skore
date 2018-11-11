package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.models.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class RoleHibernateDao implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    public Optional<Role> findRoleById(final int roleId) {
           final Role returnedRole = em.find(Role.class, roleId);

           if(returnedRole != null) {
               return Optional.of(returnedRole);
           }
           else {
               return Optional.empty();
           }
    }

    public Optional<Role> create(final String roleName, final int roleId) {
        final Role newRole = new Role(roleName, roleId);

        if(findRoleById(roleId).isPresent()) {
            return Optional.empty();
        }

        em.persist(newRole);
        return Optional.of(newRole);
    }

    public boolean remove(final int roleId) {
        Optional<Role> role = findRoleById(roleId);

        if(role.isPresent()) {
            em.remove(role.get());
            return true;
        }
        return false;
    }


}
