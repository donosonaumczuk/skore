package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

public interface EmailService {
    public void sendConfirmAccount(User user, String url);


}
