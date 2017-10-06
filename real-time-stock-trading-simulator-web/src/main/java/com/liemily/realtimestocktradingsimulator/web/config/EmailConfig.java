package com.liemily.realtimestocktradingsimulator.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Created by Emily Li on 06/10/2017.
 */
@Configuration
@Lazy
public class EmailConfig {
    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.smtp.port}")
    private String port;
    @Value("${mail.smtp.auth}")
    private String auth;
    @Value("${mail.smtp.starttls.enable}")
    private String startTls;
    @Value("${mail.emailservice.from.address}")
    private String senderAddress;
    @Value("${mail.emailservice.from.pwd}")
    private String senderPwd;

    @Bean
    public Session emailSession() {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", startTls);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderAddress, senderPwd);
            }
        };
        return Session.getDefaultInstance(props, authenticator);
    }
}
