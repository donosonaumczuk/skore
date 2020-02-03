package ar.edu.itba.paw.services;

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
        Optional<String> templateBodyOptional = readTemplate(confirmAccountTemplate);
        if (!templateBodyOptional.isPresent()) {
            return;
        }
        String templateBody = templateBodyOptional.get();
        Formatter formatter = new Formatter(body);
        String[] emailConfirmAccountClickTo = messageSource.getMessage("emailConfirmAccountClickTo",
                null, locale).split("\\{0}");
        Object[] objects = {user.getUser().getFirstName()};
        formatter.format(templateBody, messageSource.getMessage("emailConfirmAccountGreeting", objects, locale),
                emailConfirmAccountClickTo[0], user.getUserName(), emailConfirmAccountClickTo[1], url,
                messageSource.getMessage("emailConfirmAccountButton", null, locale));

        sendMail(messageSource.getMessage("emailConfirmAccountSubject", null, locale),
                body.toString(), user.getEmail());
    }

    @Async
    @Override
    public void sendConfirmMatch(User user, Game game, String url, Locale locale) {
        StringBuilder body = new StringBuilder();
        Optional<String> templateBodyOptional = readTemplate(confirmMatchTemplate);
        if (!templateBodyOptional.isPresent()) {
            return;
        }
        String templateBody = templateBodyOptional.get();
        Formatter formatter = new Formatter(body);
        String[] emailConfirmAssistanceClickTo = messageSource.getMessage("emailConfirmAssistanceClickTo",
                null, locale).split("\\{0}");
        Object[] objects = {user.getFirstName()};
        formatter.format(templateBody, messageSource.getMessage("emailConfirmAssistanceGreeting", objects, locale),
                emailConfirmAssistanceClickTo[0], game.getTitle(), emailConfirmAssistanceClickTo[1],
                messageSource.getMessage("emailConfirmAssistanceStart", null, locale),
                game.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")),
                messageSource.getMessage("emailConfirmAssistanceEnd", null, locale),
                game.getFinishTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")),
                messageSource.getMessage("emailConfirmAssistanceLocation", null, locale),
                game.getPlace().toString(), url,
                messageSource.getMessage("emailConfirmAssistanceButton", null, locale));

        sendMail(messageSource.getMessage("emailConfirmAssistanceSubject", null, locale),
                body.toString(), user.getEmail());
    }

    @Async
    @Override
    public void sendCancelMatch(User user, Game game, String url, Locale locale) {
        StringBuilder body = new StringBuilder();
        Optional<String> templateBodyOptional = readTemplate(cancelMatchTemplate);
        if (!templateBodyOptional.isPresent()) {
            return;
        }
        String templateBody = templateBodyOptional.get();
        Formatter formatter = new Formatter(body);
        String[] emailCancelAssistanceClickTo = messageSource.getMessage("emailCancelAssistanceClickTo",
                null, locale).split("\\{0}");
        Object[] objects = {user.getFirstName()};
        formatter.format(templateBody, messageSource.getMessage("emailCancelAssistanceGreeting", objects, locale),
                emailCancelAssistanceClickTo[0], game.getTitle(), emailCancelAssistanceClickTo[1],
                messageSource.getMessage("emailCancelAssistanceStart", null, locale),
                game.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")),
                messageSource.getMessage("emailCancelAssistanceEnd", null, locale),
                game.getFinishTime().format(DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")),
                messageSource.getMessage("emailCancelAssistanceLocation", null, locale),
                game.getPlace().toString(), url,
                messageSource.getMessage("emailCancelAssistanceButton", null, locale));

        sendMail(messageSource.getMessage("emailCancelAssistanceSubject", null, locale),
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
            LOGGER.error("error trying to send mail");//TODO maybe throw exception
        }
    }

    private Optional<String> readTemplate(Resource resource) {
        String templateBody = null;
        try {
            templateBody = new String(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (Exception e) {
            LOGGER.error("Error trying to read a mail template {}", resource.getFilename()); //TODO maybe throw exception
        }
        return Optional.ofNullable(templateBody);
    }
}
