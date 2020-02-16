package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailNotSentException;
import ar.edu.itba.paw.exceptions.EmailTemplateFileException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final String I18N_TEMPLATE_SEQUENCE = "\\{0}";
    private static final String DATE_FORMAT            = "HH:mm MM/dd/yyyy";
    private static final String EMAIL                  = "email";
    private static final String SUBJECT                = "Subject";
    private static final String BUTTON                 = "Button";
    private static final String GREETING               = "Greeting";
    private static final String CLICK_TO               = "ClickTo";
    private static final String LOCATION               = "Location";
    private static final String END                    = "End";
    private static final String START                  = "Start";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender emailSender;

    private static Resource confirmAccountTemplate = new ClassPathResource("confirmAccount.html");
    private static Resource confirmMatchTemplate   = new ClassPathResource("confirmMatch.html");
    private static Resource cancelMatchTemplate    = new ClassPathResource("cancelMatch.html");

    @Async
    @Override
    public void sendConfirmAccount(final PremiumUser user, final String url, final Locale locale) {
        sendUserMail(user, url, locale, "ConfirmAccount", confirmAccountTemplate);
    }

    @Async
    @Override
    public void sendConfirmMatch(final User user, final Game game, final String url, final Locale locale) {
        sendMatchMail(user, game, url, locale, "ConfirmAssistance", confirmMatchTemplate);
    }

    @Async
    @Override
    public void sendCancelMatch(final User user, final Game game, final String url, final Locale locale) {
        sendMatchMail(user, game, url, locale, "CancelAssistance", cancelMatchTemplate);
    }

    @Async
    @Override
    public void sendResetPassword(final PremiumUser user, final String url, final Locale locale) {
        sendUserMail(user, url, locale, "ResetPassword", confirmAccountTemplate);
    }

    private void sendMatchMail(final User user, final Game game, String url, final Locale locale,
                               final String type, final Resource resource) {
        StringBuilder body = new StringBuilder();
        String templateBody = readTemplate(resource);
        Formatter formatter = new Formatter(body);
        String[] emailCancelAssistanceClickTo = messageSource.getMessage(EMAIL + type + CLICK_TO,
                null, locale).split(I18N_TEMPLATE_SEQUENCE);
        Object[] objects = {user.getFirstName()};
        formatter.format(templateBody, messageSource.getMessage(EMAIL + type + GREETING, objects, locale),
                emailCancelAssistanceClickTo[0], game.getTitle(), emailCancelAssistanceClickTo[1],
                messageSource.getMessage(EMAIL + type + START, null, locale),
                game.getStartTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                messageSource.getMessage(EMAIL + type + END, null, locale),
                game.getFinishTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                messageSource.getMessage(EMAIL + type + LOCATION, null, locale),
                game.getPlace().toString(), url,
                messageSource.getMessage(EMAIL + type + BUTTON, null, locale));

        sendMail(messageSource.getMessage(EMAIL + type + SUBJECT, null, locale),
                body.toString(), user.getEmail());
    }

    private void sendUserMail(final PremiumUser user, final String url, final Locale locale,
                              final String type, final Resource resource) {
        StringBuilder body = new StringBuilder();
        String templateBody = readTemplate(resource);
        Formatter formatter = new Formatter(body);
        String[] emailConfirmAccountClickTo = messageSource.getMessage(EMAIL + type + CLICK_TO,
                null, locale).split(I18N_TEMPLATE_SEQUENCE);
        Object[] objects = {user.getUser().getFirstName()};
        formatter.format(templateBody, messageSource.getMessage(EMAIL + type + GREETING, objects, locale),
                emailConfirmAccountClickTo[0], user.getUserName(), emailConfirmAccountClickTo[1], url,
                messageSource.getMessage(EMAIL + type + BUTTON, null, locale));

        sendMail(messageSource.getMessage(EMAIL + type + SUBJECT, null, locale),
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
            throw new EmailNotSentException("Error trying to send mail");
        }
    }

    private String readTemplate(Resource resource) {
        String templateBody;
        try {
            templateBody = new String(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (Exception e) {
            LOGGER.error("Error trying to read a mail template {}", resource.getFilename());
            throw new EmailTemplateFileException("Error trying to read a mail template " + resource.getFilename());
        }
        return templateBody;
    }
}
