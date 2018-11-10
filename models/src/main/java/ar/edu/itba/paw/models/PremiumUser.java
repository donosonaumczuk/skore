package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "accounts")
public class PremiumUser {

    @Id
    @Column(name = "userName", length = 100)
    private String userName;

    @Column(name = "cellphone", length = 100)
    private String cellphone;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Embedded
    private Place home;

    @Column(name = "reputation")
    private int reputation;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String code;

    @Column
    private byte image[];

    @Column
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userId")
    private User user;

    //@ManyToMany(fetch = FetchType.LAZY)TODO: for next iteration
    //private List<PremiumUser> friends;

    //@ManyToMany(fetch = FetchType.LAZY)
    //private List<Notification> notifications;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "likes",
//    joinColumns = {@JoinColumn (name = "userName", referencedColumnName = "userName")},
//    inverseJoinColumns = {@JoinColumn(name = "sportName", referencedColumnName = "sportName")})
//    private List<Sport> likes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userRoles" ,
    joinColumns = {@JoinColumn(name = "username", referencedColumnName = "userName")},
            inverseJoinColumns = {@JoinColumn(name = "role")})
    private Set<Role> roles;

    public PremiumUser(String firstName, String lastName, String email,
                       String userName, String cellphone, LocalDate birthday,
                       Place home, int reputation, String password, String code,
                       byte image[]) {

        this.user           = new User(firstName, lastName, email);
        this.userName       = userName;
        this.cellphone      = cellphone;
        this.birthday       = birthday;
        this.home           = home;
        this.reputation     = reputation;
        this.password       = password;
        this.code           = code;
//        this.friends        = new LinkedList<>();
//        this.notifications  = new ArrayList<>();
    //    this.likes          = new ArrayList<>();
        this.roles          = new HashSet<>();
        enabled             = false;
        this.image          = image;
    }

    public PremiumUser(String firstName, String lastName, String email, String userName) {
        this.user     = new User(firstName, lastName, email);
        this.userName = userName;
    }


    public PremiumUser() {
        //for hibernate
    }




    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return getUser().getFirstName() + " " + getUser().getLastName();
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

//    public List<PremiumUser> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(final List<PremiumUser> friends) {
//        this.friends = friends;
//    }
//
//    public List<Notification> getNotifications() {
//        return notifications;
//    }
//
//    public void setNotifications(final List<Notification> notifications) {
//        this.notifications = notifications;
//    }
//
//    public List<Sport> getLikes() {
//        return likes;
//    }
//
//    public void setLikes(final List<Sport> likes) {
//        this.likes = likes;
//    }

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

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
