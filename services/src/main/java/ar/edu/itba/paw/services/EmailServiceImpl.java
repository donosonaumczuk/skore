package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.net.URL;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    @Autowired
    public JavaMailSender emailSender;

    private static String URL_PREFIX = "http://pawserver.it.itba.edu.ar/paw-2018b-04/";
    //private static String URL_PREFIX = "http://localhost:8080/";

    @Async
    @Override
    public void sendConfirmAccount(PremiumUser user, String url) {
        final String body = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ": </p>" +
                "<p>\t Plese click in the following url to confirm your account:</p>" +
                "<p>\t <a href='" + URL_PREFIX + url + "'>" + URL_PREFIX + url + "</a></p>";
        try {

            MimeMessage message = emailSender.createMimeMessage();

            message.setSubject("Confirm Account");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setText(body, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("error trying to send mail");
        }
    }


    @Async
    @Override
    public void sendConfirmMatch(User user, Game game, String url) {
        final String body = "<p>Dear "+user.getFirstName()+" "+user.getLastName()+":</p>" +
                "<p> To confirm your assistance to the match: " + game.getTitle() + "</p>" +
                "<p> starting at: " + game.getStartTime() + "</p>" +
                "<p> ending at: " + game.getFinishTime() + "</p>" +
                "<p>click the following link: </p>" +
                "<p>\t<a href='" + URL_PREFIX + url + "'>" + URL_PREFIX + url + "</a></p>";
        try {
            MimeMessage message = emailSender.createMimeMessage();

            message.setSubject("Confirm Match Assistance");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setText(body, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("error trying to send mail");
        }
    }

    @Async
    @Override
    public void sendCancelMatch(User user, Game game, String url) {
        String body = "<p>Dear "+user.getFirstName()+" "+user.getLastName()+":</p>" +
                "<p>To cancel your assistance to the match: " + game.getTitle() + "</p>" +
                "<p> starting at: " + game.getStartTime() + "</p>" +
                "<p> ending at: " + game.getFinishTime() + "</p>" +
                "<p>click the following link:</p>" +
                "<p>\t<a href='" + URL_PREFIX + url + "'>" + URL_PREFIX + url + "</a></p>";
        try {
            MimeMessage message = emailSender.createMimeMessage();

            message.setSubject("Cancel Match Assistance");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setText(body, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("error trying to send mail");
        }
    }

}

