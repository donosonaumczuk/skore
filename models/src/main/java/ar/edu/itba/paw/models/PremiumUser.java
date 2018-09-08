package ar.edu.itba.paw.models;

import org.joda.time.DateTime;

import java.util.List;

public class PremiumUser extends User{
    private String userName;
    private String cellphone;
    private DateTime birthday;
    private Place home;
    private int reputation;
    private String password;
    private List<PremiumUser> friends;
    private List<Notification> notifications;
    private List<Sport> likes;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(DateTime birthday) {
        this.birthday = birthday;
    }

    public Place getHome() {
        return home;
    }

    public void setHome(Place home) {
        this.home = home;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PremiumUser> getFriends() {
        return friends;
    }

    public void setFriends(List<PremiumUser> friends) {
        this.friends = friends;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Sport> getLikes() {
        return likes;
    }

    public void setLikes(List<Sport> likes) {
        this.likes = likes;
    }
}
