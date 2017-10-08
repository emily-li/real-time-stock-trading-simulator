package com.liemily.realtimestocktradingsimulator.web.service;

import com.liemily.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Emily Li on 06/10/2017.
 */
@Service
@Lazy
public class EmailService {
    private final Session emailSession;

    @Autowired
    public EmailService(Session emailSession) {
        this.emailSession = emailSession;
    }

    public void email(String to, String subject, String contents) throws MessagingException {
        MimeMessage msg = new MimeMessage(emailSession);
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(contents);
        Transport.send(msg);
    }

    public void emailConfirmation(User user) throws MessagingException {
        email(user.getEmail(), "Real Time Stock Trading Simulator - Email Confirmation", "Please click here to confirm your e-mail address: ");
    }
}
