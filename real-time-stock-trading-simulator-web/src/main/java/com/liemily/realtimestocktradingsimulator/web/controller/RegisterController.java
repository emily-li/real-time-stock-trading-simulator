package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.realtimestocktradingsimulator.web.domain.ControllerError;
import com.liemily.realtimestocktradingsimulator.web.domain.UserProperty;
import com.liemily.realtimestocktradingsimulator.web.service.EmailService;
import com.liemily.realtimestocktradingsimulator.web.validator.RegisterValidator;
import com.liemily.user.domain.User;
import com.liemily.user.exception.InvalidUserTokenException;
import com.liemily.user.exception.UserAlreadyExistsException;
import com.liemily.user.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import java.lang.invoke.MethodHandles;
import java.util.UUID;

/**
 * Created by Emily Li on 08/10/2017.
 */
@Controller
@Lazy
public class RegisterController {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final String REGISTER_PAGE = "register";
    private static final String REGISTER_SUCCESS_PAGE = "register_success";

    private final RegisterValidator registerValidator;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public RegisterController(RegisterValidator registerValidator, UserService userService, EmailService emailService) {
        this.registerValidator = registerValidator;
        this.userService = userService;
        this.emailService = emailService;
    }

    public static String getRegisterPage() {
        return REGISTER_PAGE;
    }

    public static String getRegisterSuccessPage() {
        return REGISTER_SUCCESS_PAGE;
    }

    @RequestMapping("register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return REGISTER_PAGE;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @Transactional
    public String register(Model model,
                           @ModelAttribute("user") User user,
                           BindingResult bindingResultUser,
                           @ModelAttribute("password_confirmation") String passwordConfirmation,
                           BindingResult bindingResultPwd,
                           @ModelAttribute("email_confirmation") String emailConfirmation,
                           BindingResult bindingResultEmail) {
        logger.info("Received registration request for user: " + user.getUsername());

        registerValidator.validate(user, bindingResultUser);
        registerValidator.validatePasswordsMatch(user, passwordConfirmation, bindingResultPwd);
        registerValidator.validateEmailsMatch(user, emailConfirmation, bindingResultEmail);

        if (bindingResultPwd.hasErrors() || bindingResultEmail.hasErrors()) {
            return REGISTER_PAGE;
        }

        if (bindingResultUser.hasErrors()
                && (bindingResultUser.getErrorCount() == 1
                && !bindingResultUser.getFieldError("birthDate").toString().contains("for value ''"))) { // Skip instances where bind has failed due to null date
            return REGISTER_PAGE;
        }

        try {
            User savedUser = userService.save(user);
            String token = generateUserToken(savedUser);
            emailService.emailConfirmation(savedUser.getEmail(), token);
            logger.info(String.format("Confirmation email sent to %s for user %s", user.getEmail(), user.getUsername()));
            return "redirect:register/success";
        } catch (UserAlreadyExistsException uaee) {
            logger.error("ERROR", uaee);
            bindingResultUser.rejectValue(UserProperty.USERNAME.toString(), ControllerError.REGISTRATION_USERNAME_ALREADY_EXISTS_ERROR.toString(), uaee.getMessage());
        } catch (MessagingException me) {
            logger.error("ERROR", me);
            bindingResultUser.reject(ControllerError.REGISTRATION_EMAIL_ERROR.toString(), me.getMessage());
        }
        return REGISTER_PAGE;
    }

    @RequestMapping("register/success")
    public String registerSuccess(Model model) {
        return REGISTER_SUCCESS_PAGE;
    }

    @RequestMapping("register/token/{token}")
    public String confirm(Model model,
                          @PathVariable("token") String token) {
        try {
            User user = userService.activateUser(token);
            logger.info("Activated user account: " + user.getUsername());
            model.addAttribute("msg", "Successfully activated user " + user.getUsername());
        } catch (InvalidUserTokenException e) {
            model.addAttribute("msg", e.getMessage());
        }
        return "generic-message";
    }

    String generateUserToken(User user) {
        String token = UUID.randomUUID().toString();
        userService.saveUserToken(user.getUsername(), token);
        return token;
    }
}
