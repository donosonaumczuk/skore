package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    public void sendConfirmAccount(User user, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirm Account");
        message.setText("Dear "+user.getFirstName()+" "+user.getLastName()+": \n" +
                        "\t Plese click in the following url to confirm your account: \n" +
                        "\t"+url);
        emailSender.send(message);
    }
}

