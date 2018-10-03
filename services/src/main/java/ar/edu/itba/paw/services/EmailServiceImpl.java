package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Async
    @Override
    public void sendConfirmAccount(PremiumUser user, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirm Account");
        message.setText("Dear "+user.getFirstName()+" "+user.getLastName()+": \n" +
                        "\t Plese click in the following url to confirm your account: \n" +
                        "\t"+url);
        emailSender.send(message);
    }


    @Async
    @Override
    public void sendConfirmMatch(User user, Game game, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirm Match Assistance");
        message.setText("Dear "+user.getFirstName()+" "+user.getLastName()+": \n" +
                "\t To confirm your assistance to the match: " + game.getTitle() + "\n" +
                "\t starting at: " + game.getStartTime() + "\n" +
                "\t ending at: " + game.getFinishTime() + "\n" +
                "click the following link: \n" +
                "\t" + url);
        emailSender.send(message);
    }

    @Async
    @Override
    public void sendCancelMatch(User user, Game game, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Cancel Match Assistance");
        message.setText("Dear "+user.getFirstName()+" "+user.getLastName()+": \n" +
                "\t To cancel your assistance to the match: " + game.getTitle() + "\n" +
                "\t starting at: " + game.getStartTime() + "\n" +
                "\t ending at: " + game.getFinishTime() + "\n" +
                "click the following link: \n" +
                "\t" + url);
        emailSender.send(message);
    }

}

