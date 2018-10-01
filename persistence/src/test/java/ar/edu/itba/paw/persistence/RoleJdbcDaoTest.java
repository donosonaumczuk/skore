package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class RoleJdbcDaoTest {
    private static final String USERROLENAME = "ROLE_USER";
    private static final String ADMINROLENAME = "ROLE_ADMIN";
    private static final int USERROLEID = 0;
    private static final int ADMINROLEID = 1;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RoleJdbcDao roleDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
    }

    @Test
    public void testCreate() {
        //exercise class
        final Role role = roleDao.create(USERROLENAME, USERROLEID).get();

        //postconditions
        Assert.assertNotNull(role);
        Assert.assertEquals(USERROLEID, role.getRoleId());
        Assert.assertEquals(USERROLENAME, role.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "roles"));
    }

    private void insertUser(final int roleId) {
        jdbcTemplate.execute("INSERT INTO roles (roleName, roleId)" +
                " VALUES ('" + USERROLENAME + "' , '" + USERROLEID + "');");
    }

    @Test
    public void testFindRoleByIdWithExistantId() {
        //set up
        insertUser(USERROLEID);

        //exercise class
        final Optional<Role> returnedRole = roleDao.findRoleById(USERROLEID);

        //postconditions
        Assert.assertTrue(returnedRole.isPresent());
        final Role user = returnedRole.get();
        Assert.assertEquals(USERROLEID, user.getRoleId());
    }

    @Test
    public void testFindIdWithNonExistantId() {
        //exercise class
        final Optional<Role> returnedRole = roleDao.findRoleById(ADMINROLEID);

        //postconditions
        Assert.assertTrue(!returnedRole.isPresent());
    }

    @Test
    public void testRemoveNonExistantRole(){
        //exercise class
        boolean returnValue = roleDao.remove(ADMINROLEID);

        //postconditions
        Assert.assertEquals(false, returnValue);
    }

    @Test
    public void testRemoveExistantUser(){
        //set up
        insertUser(USERROLEID);

        //exercise class
        boolean returnValue = roleDao.remove(USERROLEID);

        //postconditions
        Assert.assertEquals(true, returnValue);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "roles"));
    }

}
