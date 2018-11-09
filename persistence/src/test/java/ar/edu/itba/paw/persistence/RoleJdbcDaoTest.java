//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.models.Role;
//import ar.edu.itba.paw.models.User;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//import java.util.Optional;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//@Sql("classpath:schema.sql")
//public class RoleJdbcDaoTest {
//    private static final String USER_ROLE_NAME = "ROLE_USER";
//    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
//    private static final int USER_ROLE_ID = 0;
//    private static final int ADMIN_ROLE_ID = 1;
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private RoleJdbcDao roleDao;
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(dataSource);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
//    }
//
//    @Test
//    public void testCreate() {
//        //exercise class
//        final Role role = roleDao.create(USER_ROLE_NAME, USER_ROLE_ID).get();
//
//        //postconditions
//        Assert.assertNotNull(role);
//        Assert.assertEquals(USER_ROLE_ID, role.getRoleId());
//        Assert.assertEquals(USER_ROLE_NAME, role.getName());
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "roles"));
//    }
//
//    private void insertUser(final int roleId) {
//        jdbcTemplate.execute("INSERT INTO roles (roleName, roleId)" +
//                " VALUES ('" + USER_ROLE_NAME + "' , '" + USER_ROLE_ID + "');");
//    }
//
//    @Test
//    public void testFindRoleByIdWithExistantId() {
//        //set up
//        insertUser(USER_ROLE_ID);
//
//        //exercise class
//        final Optional<Role> returnedRole = roleDao.findRoleById(USER_ROLE_ID);
//
//        //postconditions
//        Assert.assertTrue(returnedRole.isPresent());
//        final Role user = returnedRole.get();
//        Assert.assertEquals(USER_ROLE_ID, user.getRoleId());
//    }
//
//    @Test
//    public void testFindIdWithNonExistantId() {
//        //exercise class
//        final Optional<Role> returnedRole = roleDao.findRoleById(ADMIN_ROLE_ID);
//
//        //postconditions
//        Assert.assertTrue(!returnedRole.isPresent());
//    }
//
//    @Test
//    public void testRemoveNonExistantRole(){
//        //exercise class
//        boolean returnValue = roleDao.remove(ADMIN_ROLE_ID);
//
//        //postconditions
//        Assert.assertEquals(false, returnValue);
//    }
//
//    @Test
//    public void testRemoveExistantUser(){
//        //set up
//        insertUser(USER_ROLE_ID);
//
//        //exercise class
//        boolean returnValue = roleDao.remove(USER_ROLE_ID);
//
//        //postconditions
//        Assert.assertEquals(true, returnValue);
//        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "roles"));
//    }
//
//}
