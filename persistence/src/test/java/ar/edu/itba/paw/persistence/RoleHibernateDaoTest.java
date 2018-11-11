package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.models.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class RoleHibernateDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RoleHibernateDao roleDao;


    private static final String adminRole = "ROLE_ADMIN";
    private static final int adminRoleId =  1;
    private static final String userRole = "ROLE_USER";
    private static final int userRoleId =  1;



    public RoleHibernateDaoTest() {


    }

    @Test
    public void testFindRoleById() {
        //set up
        Role newRole = new Role(adminRole, adminRoleId);
        em.persist(newRole);

        //exercise class
        Optional<Role> returnedRole = roleDao.findRoleById(adminRoleId);

        //postconditions
        Assert.assertTrue(returnedRole.isPresent());
        Assert.assertEquals(returnedRole.get().getName(), adminRole);
        Assert.assertEquals(returnedRole.get().getRoleId(), adminRoleId);
    }

    @Test
    public void testFindNonExistantRoleById() {

        //exercise class
        Optional<Role> returnedRole = roleDao.findRoleById(adminRoleId);

        //postconditions
        Assert.assertTrue(!returnedRole.isPresent());
    }

    @Test
    public void testCreateRoleById() {

        //exercise class
        Optional<Role> insertedRole = roleDao.create(userRole, userRoleId);

        //postconditions
        Assert.assertTrue(insertedRole.isPresent());

        Role returnedRole = em.find(Role.class, userRoleId);
        Assert.assertEquals(returnedRole.getName(), insertedRole.get().getName());
        Assert.assertEquals(returnedRole.getRoleId(), insertedRole.get().getRoleId());
    }

    @Test
    public void testCreateExistantRoleById() {
        //set up
        Role newRole = new Role(userRole, userRoleId);

        //exercise class
        em.persist(newRole);
        Optional<Role> insertedRole = roleDao.create(userRole, userRoleId);

        //postconditions
        Assert.assertTrue(!insertedRole.isPresent());
    }

    @Test
    public void testRemoveRoleById() {
        //set up
        Role newRole = new Role(userRole, userRoleId);
        em.persist(newRole);

        //exercise class
        Boolean returnedValue = roleDao.remove(newRole.getRoleId());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertNull(em.find(Role.class, newRole.getRoleId()));
    }


}
