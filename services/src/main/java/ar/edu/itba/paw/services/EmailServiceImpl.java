package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    @Autowired
    private MessageSource messageSource;

    @Autowired
    public JavaMailSender emailSender;

    /* Production */
    private static String URL_PREFIX = "http://pawserver.it.itba.edu.ar/paw-2018b-04/";

    /* Local */
//    private static String URL_PREFIX = "http://localhost:8080/";

    @Async
    @Override
    public void sendConfirmAccount(PremiumUser user, String url, Locale locale) {
        final String body = "" +
                "<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailConfirmAccountGreeting", null, locale) + user.getUser().getFirstName() + "!</div>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailConfirmAccountClickTo", null, locale) + " <span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + user.getUserName() + "</span>.</div>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</table>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td width=\"110\"/>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<a class=\"button\" href=\"" + URL_PREFIX + url + "\" style=\"border-radius: 4px;padding: 8px 12px;cursor: pointer;text-decoration: none;background-color: transparent;color: green;font-family: sans-serif;border-style: solid;border-width: 1px;border-color: green\">" + messageSource.getMessage("emailConfirmAccountButton", null, locale) + "</a>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</table>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "</table>\n";
        try {

            MimeMessage message = emailSender.createMimeMessage();

            message.setSubject(messageSource.getMessage("emailConfirmAccountSubject", null, locale));
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
    public void sendConfirmMatch(User user, Game game, String url, Locale locale) {
        final String body = "" +
                "\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailConfirmAssistanceGreeting", null, locale) + " " + user.getFirstName() + "!</div>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailConfirmAssistanceClickTo", null, locale) + " <span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + game.getTitle() + "</span>.</div>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td width=\"90\"/>\n" +
                "\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\"><span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailConfirmAssistanceStart", null, locale) + ":</span> " + game.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")) + "</div>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td width=\"90\"/>\n" +
                "\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\"><span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailConfirmAssistanceEnd", null, locale) + ":</span> " + game.getFinishTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")) + "</div>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t<td width=\"90\"/>\n" +
                "\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\"><span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailConfirmAssistanceLocation", null, locale) + ":</span> " + game.getPlace().toString() + "</div>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</td>\n" +
                "\n" +
                "\t\t</tr>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td width=\"110\"/>\n" +
                "\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t<a class=\"button\" href=\"" + URL_PREFIX + url + "\" style=\"border-radius: 4px;padding: 8px 12px;cursor: pointer;text-decoration: none;background-color: transparent;color: green;font-family: sans-serif;border-style: solid;border-width: 1px;border-color: green\">" + messageSource.getMessage("emailConfirmAssistanceButton", null, locale) + "</a>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t</table>\n";
        try {
            MimeMessage message = emailSender.createMimeMessage();

            message.setSubject(messageSource.getMessage("emailConfirmAssistanceSubject", null, locale));
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
    public void sendCancelMatch(User user, Game game, String url, Locale locale) {
        String body = "" +
                "<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailCancelAssistanceGreeting", null, locale) + " " + user.getFirstName() + "!</div>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailCancelAssistanceClickTo", null, locale) + " <span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + game.getTitle() + "</span>.</div>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</table>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td width=\"90\"/>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\"><span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailCancelAssistanceStart", null, locale) + ": </span>" + game.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")) + "</div>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td width=\"90\"/>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\"><span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailCancelAssistanceEnd", null, locale) + ": </span>" + game.getFinishTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")) + "</div>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<td width=\"90\"/>\n" +
                "\t\t\t\t<td>\n" +
                "\t\t\t\t\t<div class=\"text\" style=\"font-family: sans-serif;font-size: 18px\"><span style=\"font-weight: bold;font-family: sans-serif;font-size: 18px\">" + messageSource.getMessage("emailCancelAssistanceLocation", null, locale) + ": </span>" + game.getPlace().toString() + "</div>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</table>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>\n" +
                "\t\t\t<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td width=\"110\"/>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<a class=\"button\" href=\"" + URL_PREFIX + url + "\" style=\"border-radius: 4px;padding: 8px 12px;cursor: pointer;text-decoration: none;background-color: transparent;color: darkred;font-family: sans-serif;border-style: solid;border-width: 1px;border-color: darkred\">" + messageSource.getMessage("emailCancelAssistanceButton", null, locale) + "</a>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</table>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "</table>\n";
        try {
            MimeMessage message = emailSender.createMimeMessage();

            message.setSubject(messageSource.getMessage("emailCancelAssistanceSubject", null, locale));
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
