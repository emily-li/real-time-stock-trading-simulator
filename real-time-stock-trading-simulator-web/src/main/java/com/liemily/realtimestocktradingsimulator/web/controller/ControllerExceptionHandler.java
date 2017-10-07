package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.realtimestocktradingsimulator.web.service.EmailService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

/**
 * Created by Emily Li on 04/10/2017.
 */
@ControllerAdvice
@Lazy
public class ControllerExceptionHandler {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SUBJECT = "ControllerExceptionHandler caught Exception: ";
    private final EmailService emailService;
    private final String adminEmail;

    @Autowired
    public ControllerExceptionHandler(EmailService emailService,
                                      @Value("${mail.emailservice.admin.address}") String adminEmail) {
        this.emailService = emailService;
        this.adminEmail = adminEmail;
    }

    @ExceptionHandler(Exception.class)
    void handleException(Exception e) throws MessagingException {
        logger.info("Caught exception", e);

        String subject = SUBJECT + e.getCause();
        String contents = e.getStackTrace() != null ? Arrays.toString(e.getStackTrace()) : e.toString();

        logger.info(String.format("Sending email to '%s' with subject '%s' and message '%s'", adminEmail, subject, contents));
        emailService.email(adminEmail, subject, contents);

        logger.info(String.format("Sent email to '%s' with subject '%s'", adminEmail, subject));
    }
}
