package com.liemily.realtimestocktradingsimulator.web.service;

import com.liemily.realtimestocktradingsimulator.web.controller.RegisterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final String webBaseUrl;

    @Autowired
    public EmailService(Session emailSession,
                        @Value("${web.url.base}") String webBaseUrl) {
        this.emailSession = emailSession;
        this.webBaseUrl = webBaseUrl;
    }

    public void email(String to, String subject, String contents) throws MessagingException {
        MimeMessage msg = new MimeMessage(emailSession);
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(contents);
        Transport.send(msg);
    }

    public void emailConfirmation(String email, String token) throws MessagingException {
        final String subject = "Real Time Stock Trading Simulator - Email Confirmation";
        final String contents = "Please click here to confirm your e-mail address: " + webBaseUrl + "/" + RegisterController.getRegisterPage() + "/token/" + token;
        email(email, subject, contents);
    }
}
