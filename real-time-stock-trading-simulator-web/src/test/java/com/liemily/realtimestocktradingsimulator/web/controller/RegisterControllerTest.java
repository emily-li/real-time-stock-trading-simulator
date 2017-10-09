package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.realtimestocktradingsimulator.web.service.EmailService;
import com.liemily.realtimestocktradingsimulator.web.validator.RegisterValidator;
import com.liemily.user.domain.User;
import com.liemily.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

/**
 * Created by Emily Li on 08/10/2017.
 */
public class RegisterControllerTest {
    private RegisterController registerController;
    private EmailService emailService;

    @Before
    public void setup() {
        emailService = mock(EmailService.class);
        registerController = new RegisterController(mock(RegisterValidator.class), mock(UserService.class), emailService);
    }

    /**
     * C.Reg14 The user should be notified if their registration has been successfully submitted
     */
    @Test
    public void testUserNotifiedOnRegistrationSubmission() throws Exception {
        User user = new User();
        user.setEmail("email");
        registerController.register(new ExtendedModelMap(), user, mock(BindingResult.class), null, mock(BindingResult.class), null, mock(BindingResult.class));
        verify(emailService, times(1)).emailConfirmation(anyString(), anyString());
    }
}
