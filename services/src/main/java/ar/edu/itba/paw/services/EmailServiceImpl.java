package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Locale;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final String EMAIL_CONFIRM_ACCOUNT_CLICK_TO = "emailConfirmAccountClickTo";
    private static final String EMAIL_CONFIRM_ACCOUNT_GREETING = "emailConfirmAccountGreeting";
    private static final String EMAIL_CONFIRM_ACCOUNT_BUTTON = "emailConfirmAccountButton";
    private static final String EMAIL_CONFIRM_ACCOUNT_SUBJECT = "emailConfirmAccountSubject";
    private static final String EMAIL_CONFIRM_ASSISTANCE_CLICK_TO = "emailConfirmAssistanceClickTo";
    private static final String EMAIL_CONFIRM_ASSISTANCE_GREETING = "emailConfirmAssistanceGreeting";
    private static final String EMAIL_CONFIRM_ASSISTANCE_START = "emailConfirmAssistanceStart";
    private static final String EMAIL_CONFIRM_ASSISTANCE_END = "emailConfirmAssistanceEnd";
    private static final String EMAIL_CONFIRM_ASSISTANCE_LOCATION = "emailConfirmAssistanceLocation";
    private static final String EMAIL_CONFIRM_ASSISTANCE_BUTTON = "emailConfirmAssistanceButton";
    private static final String EMAIL_CONFIRM_ASSISTANCE_SUBJECT = "emailConfirmAssistanceSubject";
    private static final String EMAIL_CANCEL_ASSISTANCE_CLICK_TO = "emailCancelAssistanceClickTo";
    private static final String EMAIL_CANCEL_ASSISTANCE_GREETING = "emailCancelAssistanceGreeting";
    private static final String EMAIL_CANCEL_ASSISTANCE_START = "emailCancelAssistanceStart";
    private static final String EMAIL_CANCEL_ASSISTANCE_END = "emailCancelAssistanceEnd";
    private static final String EMAIL_CANCEL_ASSISTANCE_LOCATION = "emailCancelAssistanceLocation";
    private static final String EMAIL_CANCEL_ASSISTANCE_BUTTON = "emailCancelAssistanceButton";
    private static final String EMAIL_CANCEL_ASSISTANCE_SUBJECT = "emailCancelAssistanceSubject";
    private static final String DATE_FORMAT = "HH:mm MM/dd/yyyy";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender emailSender;

    private static Resource confirmAccountTemplate = new ClassPathResource("confirmAccount.html");
    private static Resource confirmMatchTemplate   = new ClassPathResource("confirmMatch.html");
    private static Resource cancelMatchTemplate    = new ClassPathResource("cancelMatch.html");

    @Async
    @Override
    public void sendConfirmAccount(PremiumUser user, String url, Locale locale) {
        StringBuilder body = new StringBuilder();
        String templateBody = readTemplate(confirmAccountTemplate);
        Formatter formatter = new Formatter(body);
        String[] emailConfirmAccountClickTo = messageSource.getMessage(EMAIL_CONFIRM_ACCOUNT_CLICK_TO,
                null, locale).split("\\{0}");
        Object[] objects = {user.getUser().getFirstName()};
        formatter.format(templateBody, messageSource.getMessage(EMAIL_CONFIRM_ACCOUNT_GREETING, objects, locale),
                emailConfirmAccountClickTo[0], user.getUserName(), emailConfirmAccountClickTo[1], url,
                messageSource.getMessage(EMAIL_CONFIRM_ACCOUNT_BUTTON, null, locale));

        sendMail(messageSource.getMessage(EMAIL_CONFIRM_ACCOUNT_SUBJECT, null, locale),
                body.toString(), user.getEmail());
    }

    @Async
    @Override
    public void sendConfirmMatch(User user, Game game, String url, Locale locale) {
        StringBuilder body = new StringBuilder();
        String templateBody = readTemplate(confirmMatchTemplate);
        Formatter formatter = new Formatter(body);
        String[] emailConfirmAssistanceClickTo = messageSource.getMessage(EMAIL_CONFIRM_ASSISTANCE_CLICK_TO,
                null, locale).split("\\{0}");
        Object[] objects = {user.getFirstName()};
        formatter.format(templateBody, messageSource.getMessage(EMAIL_CONFIRM_ASSISTANCE_GREETING, objects, locale),
                emailConfirmAssistanceClickTo[0], game.getTitle(), emailConfirmAssistanceClickTo[1],
                messageSource.getMessage(EMAIL_CONFIRM_ASSISTANCE_START, null, locale),
                game.getStartTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                messageSource.getMessage(EMAIL_CONFIRM_ASSISTANCE_END, null, locale),
                game.getFinishTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                messageSource.getMessage(EMAIL_CONFIRM_ASSISTANCE_LOCATION, null, locale),
                game.getPlace().toString(), url,
                messageSource.getMessage(EMAIL_CONFIRM_ASSISTANCE_BUTTON, null, locale));

        sendMail(messageSource.getMessage(EMAIL_CONFIRM_ASSISTANCE_SUBJECT, null, locale),
                body.toString(), user.getEmail());
    }

    @Async
    @Override
    public void sendCancelMatch(User user, Game game, String url, Locale locale) {
        StringBuilder body = new StringBuilder();
        String templateBody = readTemplate(cancelMatchTemplate);
        Formatter formatter = new Formatter(body);
        String[] emailCancelAssistanceClickTo = messageSource.getMessage(EMAIL_CANCEL_ASSISTANCE_CLICK_TO,
                null, locale).split("\\{0}");
        Object[] objects = {user.getFirstName()};
        formatter.format(templateBody, messageSource.getMessage(EMAIL_CANCEL_ASSISTANCE_GREETING, objects, locale),
                emailCancelAssistanceClickTo[0], game.getTitle(), emailCancelAssistanceClickTo[1],
                messageSource.getMessage(EMAIL_CANCEL_ASSISTANCE_START, null, locale),
                game.getStartTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                messageSource.getMessage(EMAIL_CANCEL_ASSISTANCE_END, null, locale),
                game.getFinishTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                messageSource.getMessage(EMAIL_CANCEL_ASSISTANCE_LOCATION, null, locale),
                game.getPlace().toString(), url,
                messageSource.getMessage(EMAIL_CANCEL_ASSISTANCE_BUTTON, null, locale));

        sendMail(messageSource.getMessage(EMAIL_CANCEL_ASSISTANCE_SUBJECT, null, locale),
                body.toString(), user.getEmail());
    }

    private void sendMail(String subject, String body, String email) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setText(body, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("Error trying to send mail");
            throw new EmailException("Error trying to send mail");
        }
    }

    private String readTemplate(Resource resource) {
        String templateBody;
        try {
            templateBody = new String(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (Exception e) {
            LOGGER.error("Error trying to read a mail template {}", resource.getFilename());
            throw new EmailException("Error trying to read a mail template " + resource.getFilename());
        }
        return templateBody;
    }
}
