package ar.edu.itba.paw.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PremiumUser extends User{
    private String userName;
    private String cellphone;
    private LocalDateTime birthday;
    private Place home;
    private int reputation;
    private String password;
    private List<PremiumUser> friends;
    private List<Notification> notifications;
    private List<Sport> likes;

    public PremiumUser(String firstName, String lastName, String email, long userId,
                       String userName, String cellphone, LocalDateTime birthday,
                       Place home, int reputation, String password) {
        super(firstName, lastName, email, userId);

        this.userName       = userName;
        this.cellphone      = cellphone;
        this.birthday       = birthday;
        this.home           = home;
        this.reputation     = reputation;
        this.password       = password;
        this.friends        = new LinkedList<>();
        this.notifications  = new ArrayList<>();
        this.likes          = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(final String cellphone) {
        this.cellphone = cellphone;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(final LocalDateTime birthday) {
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

    @Override
    public boolean equals(Object object) {
        if(object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        PremiumUser aPremiumUser = ((PremiumUser) object);
        return getUserName().equals(aPremiumUser.getUserName());
    }
}
