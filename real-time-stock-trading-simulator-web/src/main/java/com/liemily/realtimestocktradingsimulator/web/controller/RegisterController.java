package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.realtimestocktradingsimulator.web.domain.ControllerError;
import com.liemily.realtimestocktradingsimulator.web.domain.UserProperty;
import com.liemily.realtimestocktradingsimulator.web.validator.RegisterValidator;
import com.liemily.user.domain.User;
import com.liemily.user.exception.UserAlreadyExistsException;
import com.liemily.user.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.invoke.MethodHandles;

/**
 * Created by Emily Li on 08/10/2017.
 */
@Controller
@Lazy
public class RegisterController {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final String REGISTER_PAGE = "register";
    private static final String REGISTER_SUCCESS_PAGE = "register-success";

    private final RegisterValidator registerValidator;
    private final UserService userService;

    @Autowired
    public RegisterController(RegisterValidator registerValidator, UserService userService) {
        this.registerValidator = registerValidator;
        this.userService = userService;
    }

    public static String getRegisterPage() {
        return REGISTER_PAGE;
    }

    @RequestMapping("register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return REGISTER_PAGE;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
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

        if (bindingResultUser.hasErrors() || bindingResultPwd.hasErrors() || bindingResultEmail.hasErrors()) {
            return REGISTER_PAGE;
        }

        try {
            userService.save(user);
            logger.info("Registered user: " + user.getUsername());
            return REGISTER_SUCCESS_PAGE;
        } catch (UserAlreadyExistsException uaee) {
            bindingResultUser.rejectValue(UserProperty.USERNAME.toString(), ControllerError.REGISTRATION_USERNAME_ALREADY_EXISTS_ERROR.toString(), uaee.getMessage());
            return REGISTER_PAGE;
        }
    }
}
