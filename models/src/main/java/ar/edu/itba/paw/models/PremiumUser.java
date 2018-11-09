package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

//@Entity
//@Table(name = "accounts")
public class PremiumUser extends User{

//    @Id
  //  @Column(name = "username", length = 100, nullable = false)
    private String userName;

    //@Column(name = "cellphone", length = 100)
    private String cellphone;

    //@Column
    private LocalDate birthday;

    //@Embedded
    private Place home;

    //@Column
    private int reputation;

    //@Column(length = 100, nullable = false, unique = true)
    private String email;

    //@Column(length = 100, nullable = false)
    private String password;

    //@Column(length = 100, nullable = false)
    private String code;

   // @ManyToMany(fetch = FetchType.LAZY)
    private List<PremiumUser> friends;

   // @ManyToMany(fetch = FetchType.LAZY)
    private List<Notification> notifications;

    //@OneToMany(fetch = FetchType.EAGER)
    private List<Sport> likes;

   // @OneToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public PremiumUser(String firstName, String lastName, String email, long userId,
                       String userName, String cellphone, LocalDate birthday,
                       Place home, int reputation, String password, String code) {
        super(firstName, lastName, email, userId);

        this.userName       = userName;
        this.cellphone      = cellphone;
        this.birthday       = birthday;
        this.home           = home;
        this.reputation     = reputation;
        this.password       = password;
        this.email          = email;
        this.code           = code;
        this.friends        = new LinkedList<>();
        this.notifications  = new ArrayList<>();
        this.likes          = new ArrayList<>();
        this.roles          = new HashSet<>();
    }

    public PremiumUser(String firstName, String lastName, String email, long userId, String userName) {
        super(firstName, lastName, email, userId);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(final String cellphone) {
        this.cellphone = cellphone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

    public Place getHome() {
        return home;
    }

    public void setHome(final Place home) {
        this.home = home;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(final int reputation) {
        this.reputation = reputation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PremiumUser> getFriends() {
        return friends;
    }

    public void setFriends(final List<PremiumUser> friends) {
        this.friends = friends;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(final List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Sport> getLikes() {
        return likes;
    }

    public void setLikes(final List<Sport> likes) {
        this.likes = likes;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> newRoles) {

        this.roles.clear();
        this.roles.addAll(newRoles);
    }
    public void addRole(final Role newRole) {
        roles.add(newRole);
    }

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        PremiumUser aPremiumUser = ((PremiumUser) object);
        return getUserName().equals(aPremiumUser.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
