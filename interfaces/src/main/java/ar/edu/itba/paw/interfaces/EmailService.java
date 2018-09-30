package ar.edu.itba.paw.interfaces;

public interface EmailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
