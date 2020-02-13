package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.UserSort;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class PremiumUserHibernateDaoTest implements Serializable{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PremiumUserHibernateDao premiumUserDao;

    private PremiumUser notInsertedUser;

    private PremiumUser user1;

    private PremiumUser user2;

    private PremiumUser user3;

    private PremiumUser user4;

    private List<PremiumUser> users;

    public PremiumUserHibernateDaoTest() {
        users = new LinkedList<>();
        List<Sport> likes1 = new LinkedList<>();
        likes1.add(new Sport("baloncesto", 5, "Baloncesto", null));
        likes1.add(new Sport("futbol", 11, "Football", null));
        List<Sport> likes2 = new LinkedList<>();
        likes2.add(new Sport("tennis", 1, "Tennis", null));
        notInsertedUser = new PremiumUser("firstName", "lastName", "email",  "userName",
                "cellphone", LocalDate.parse("1994-12-26"), new Place("country", "state",
                "city", "street"), 10, "password", "code", null);
        user1 = new PremiumUser("Agustin", "Dammiano", "adammiano@itba.edu.ar",
                "damminao98", "22443311", LocalDate.parse("1998-06-05"), new Place("Argentina",
                "Buenos Aires", "La Plata", "La plata 123"), 100, "agustin123",
                "code", null);
        user1.setLikes(likes1);
        user2 = new PremiumUser("Agustin", "Izaguirre", "aIzaguierre@itba.edu.ar",
                "jayjay", "97536012", LocalDate.parse("1994-12-26"), new Place("Chile",
                "Santiago", "Santiago de Chile", "Santiago 123"), 2, "chileno123",
                "code", null);
        user2.setLikes(likes2);
        user3 = new PremiumUser("Alan", "Donoso Naumczuk", "aDonosoNaumczuk@itba.edu.ar",
                "Doni", "64469319", LocalDate.parse("1996-10-08"), new Place("Argentina",
                "Buenos Aires", "La Plata", "La plata 123"), 68, "alan123",
                "code", null);
        user4 = new PremiumUser("Juan", "Domingo Peron", "perono@gmail.edu.ar",
                "ElGeneral", "22228888", LocalDate.parse("1895-10-08"), new Place("Argentina",
                "Buenos Aires", "Lobos", "Lobos"), 10000, "peron123",
                "code", null);
        user4.setLikes(likes1);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        List<PremiumUser> friends1 = new ArrayList<>();
        friends1.add(user2);
        friends1.add(user3);
        friends1.add(user4);
        List<PremiumUser> friends2 = new ArrayList<>();
        friends2.add(user1);
        friends2.add(user3);
        List<PremiumUser> friends3 = new ArrayList<>();
        friends3.add(user1);
        friends3.add(user2);
        List<PremiumUser> friends4 = new ArrayList<>();
        friends4.add(user1);
        user1.setFriends(friends1);
        user2.setFriends(friends2);
        user3.setFriends(friends3);
        user4.setFriends(friends4);
    }

    @Before
    public void initializeDatabase() {
        for (PremiumUser premiumUser: users) {
            em.persist(premiumUser.getUser());
            for (Sport sport: premiumUser.getLikes()) {
                em.persist(sport);
            }
            em.persist(premiumUser);
        }
        em.flush();
    }

    @After
    public void removeAllData() {
        em.createNativeQuery("delete from users");
        em.createNativeQuery("delete from accounts");
        //em.flush();
    }


    @Test
    public void testFindUserByUserName() {

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.findByUserName(user1.getUserName());
        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().equals(user1));
    }

    @Test
    public void testFindUserByEmail() {

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.findByEmail(user1.getUser().getEmail());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().equals(user1));
    }

    @Test
    public void testCreatePremiumUser() throws IOException {

        //set up
        Role role = new Role("ROLE_USER",0);
        em.persist(role);

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.create(notInsertedUser.getUser().getFirstName(),
                notInsertedUser.getUser().getLastName(), "newEmail", "newUserName",
                notInsertedUser.getCellphone(), LocalDate.parse("1994-12-26"), notInsertedUser.getHome().getCountry(),
                notInsertedUser.getHome().getState(), notInsertedUser.getHome().getCity(),
                notInsertedUser.getHome().getStreet(), notInsertedUser.getReputation(), notInsertedUser.getPassword(),
                null);

        //postconditions
        PremiumUser user = em.find(PremiumUser.class, "newUserName");
        Assert.assertNotNull(user);
        Assert.assertTrue(!user.equals(notInsertedUser));
    }

    @Test
    public void testRemove() {

        //exercise class
        boolean returnedValue = premiumUserDao.remove(user1.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertNull(em.find(PremiumUser.class, user1.getUserName()));

    }

    @Test
    public void testRemoveNonExistenUser() {
        //exercise class
        boolean returnedValue = premiumUserDao.remove(notInsertedUser.getUserName());

        //postconditions
        Assert.assertTrue(!returnedValue);

    }

    @Test
    public void testReadImage() {
        //exercise class
        Optional<byte[]> image = premiumUserDao.readImage(user1.getUserName());

        //postconditions
        Assert.assertTrue(!image.isPresent());

    }

    @Test
    public void testupdateUserInfo() throws IOException {
        //exercise class
        premiumUserDao.updateUserInfo("newFirstName", user1.getUser().getLastName(),
                "newEmail", user1.getUserName(), user1.getCellphone(),
                user1.getBirthday(), user1.getHome().getCountry(),
                user1.getHome().getState(), user1.getHome().getCity(),
                user1.getHome().getStreet(), user1.getReputation(),
                "newPassword",null, user1.getUserName());
        PremiumUser returnedUser = em.find(PremiumUser.class, user1.getUserName());

        //postconditions
        Assert.assertNotNull(returnedUser);
        Assert.assertTrue(returnedUser.getUser().getFirstName().equals("newFirstName"));
        Assert.assertTrue(returnedUser.getPassword().equals("newPassword"));
        //Assert.assertTrue(returnedUser.getEmail().equals("newEmail"));

    }

    @Test
    public void testEnableUser() {

        //exercise class
        premiumUserDao.enableUser(user1.getUserName(), user1.getCode());

        //postcondition
        PremiumUser premiumUser = em.find(PremiumUser.class, user1.getUserName());
        Assert.assertTrue(premiumUser.getEnabled());

    }

    @Test
    public void testAddRoles() {
        //set up
        Role role = new Role("ROLE_USER", 1);
        em.persist(role);

        //exercise the class
        boolean returnedValue = premiumUserDao.addRole(user1.getUserName(),
                role.getRoleId());
        PremiumUser user = em.find(PremiumUser.class, user1.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertEquals(1, user.getRoles().size());
        Assert.assertTrue(user.getRoles().contains(role));
    }


    @Test
    public void testRemoveRoles() {
        //set up
        Role role = new Role("ROLE_USER", 1);
        notInsertedUser.getRoles().add(role);
        em.persist(role);
        em.persist(notInsertedUser);

        //exercise the class
        boolean returnedValue = premiumUserDao.removeRole(notInsertedUser.getUserName(),
                role.getRoleId());
        PremiumUser user = em.find(PremiumUser.class, notInsertedUser.getUserName());

        //postconditions
        Assert.assertTrue(returnedValue);
        Assert.assertEquals(0, user.getRoles().size());
        Assert.assertTrue(!user.getRoles().contains(role));
    }

    @Test
    public void testGetRoles() {
        //set up
        Role role = new Role("ROLE_USER", 1);
        notInsertedUser.getRoles().add(role);
        em.persist(role);
        em.persist(notInsertedUser);

        //exercise the class
        Set<Role> roles = premiumUserDao.getRoles(notInsertedUser.getUserName());

        //postconditions
        Assert.assertEquals(1, roles.size());
        Assert.assertTrue(roles.contains(role));
    }

    @Test
    public void testGetLikes() {
        //set up
        Sport sport = new Sport("Padel", 1, "Padel", null);
        em.persist(sport);
        notInsertedUser.getLikes().add(sport);
        em.persist(notInsertedUser);

        //exercise the class
        List<Sport> sports = premiumUserDao.getSports(notInsertedUser.getUserName());

        //postconditions
        Assert.assertEquals(1, sports.size());
        Assert.assertTrue(sports.contains(sport));
    }

    @Test
    public void testAddLikes() {
        //set up
        Sport sport = new Sport("Padel", 1, "Padel", null);
        em.persist(sport);
        em.persist(notInsertedUser);

        //exercise the class
        boolean returnedValue = premiumUserDao.addSport(notInsertedUser.getUserName(), sport.getName());

        //postconditions
        PremiumUser user = em.find(PremiumUser.class, notInsertedUser.getUserName());
        Assert.assertTrue(returnedValue);
        Assert.assertTrue(user.getLikes().contains(sport));
        Assert.assertEquals(1, user.getLikes().size());

    }

    @Test
    public void testRemoveLikes() {
        //set up
        Sport sport = new Sport("Padel", 1, "Padel", null);
        em.persist(sport);
        notInsertedUser.getLikes().add(sport);
        em.persist(notInsertedUser);

        //exercise the class
        boolean returnedValue = premiumUserDao.removeSport(notInsertedUser.getUserName(), sport.getName());

        //postconditions
        PremiumUser user = em.find(PremiumUser.class, notInsertedUser.getUserName());
        Assert.assertTrue(returnedValue);
        Assert.assertTrue(!user.getLikes().contains(sport));
        Assert.assertEquals(0, user.getLikes().size());
    }

    @Test
    public void testFindUserById() {
        //set up
        em.persist(notInsertedUser);

        //exercise class
        Optional<PremiumUser> returnedValue = premiumUserDao.findById(notInsertedUser.getUser().getUserId());

        //postconditions
        Assert.assertTrue(returnedValue.isPresent());
        Assert.assertTrue(returnedValue.get().equals(notInsertedUser));
    }

    @Test
    public void testFindListOfUserWithoutFilter() {

        //exercise class
        List<PremiumUser> usersReturn = premiumUserDao.findUsers(null, null, null,
                null, null, null, null, null);

        //postconditions
        Assert.assertEquals(users.size(), usersReturn.size());
        Assert.assertTrue(usersReturn.contains(users.get(0)));
        Assert.assertTrue(usersReturn.contains(users.get(1)));
        Assert.assertTrue(usersReturn.contains(users.get(2)));
        Assert.assertTrue(usersReturn.contains(users.get(3)));
    }

    @Test
    public void testFindListOfUserWithFilterByFriendsAndSort() {
        //set up
        List<String> usernamesFriends = new ArrayList<>();
        usernamesFriends.add(user4.getUserName());

        //exercise class
        List<PremiumUser> usersReturn = premiumUserDao.findUsers(null, null, usernamesFriends,
                null, null, null, null, new UserSort("reputation asc"));

        //postconditions
        Assert.assertEquals(1, usersReturn.size());
        Assert.assertEquals(user1, usersReturn.get(0));
    }

    @Test
    public void testFindListOfUserWithFilterBySportAndSort() {
        //set up
        List<String> likedSports = new ArrayList<>();
        likedSports.add(user1.getLikes().get(0).getName());

        //exercise class
        List<PremiumUser> usersReturn = premiumUserDao.findUsers(null, likedSports, null,
                null, null, null, null, new UserSort("reputation asc"));

        //postconditions
        Assert.assertEquals(2, usersReturn.size());
        Assert.assertEquals(user1, usersReturn.get(0));
        Assert.assertEquals(user4, usersReturn.get(1));
    }

    @Test
    public void testFindListOfUserWithFilterByUsernameAndSort() {
        //set up
        List<String> usernames = new ArrayList<>();
        usernames.add(user1.getUserName());
        usernames.add(user4.getUserName());

        //exercise class
        List<PremiumUser> usersReturn = premiumUserDao.findUsers(usernames, null, null,
                null, null, null, null, new UserSort("reputation asc"));

        //postconditions
        Assert.assertEquals(2, usersReturn.size());
        Assert.assertEquals(user1, usersReturn.get(0));
        Assert.assertEquals(user4, usersReturn.get(1));
    }

    @Test
    public void testFindListOfUserWithFilterByReputationAndSort() {

        //exercise class
        List<PremiumUser> usersReturn = premiumUserDao.findUsers(null, null, null,
                30, 200, null, null, new UserSort("reputation desc"));

        //postconditions
        Assert.assertEquals(2, usersReturn.size());
        Assert.assertEquals(user1, usersReturn.get(0));
        Assert.assertEquals(user3, usersReturn.get(1));
    }
}

