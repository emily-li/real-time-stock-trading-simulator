package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.realtimestocktradingsimulator.web.service.EmailService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests exception handler e-mail functionality
 * Created by Emily Li on 04/10/2017.
 */
public class ControllerExceptionHandlerIT {
    private ControllerExceptionHandler controllerExceptionHandler;
    private EmailService emailService;

    @Before
    public void setup() {
        emailService = mock(EmailService.class);
        controllerExceptionHandler = new ControllerExceptionHandler(emailService, "");
    }

    /**
     * S.M01 The administrator should receive an e-mail should an exception occur
     */
    @Test
    public void testAdminEmailedOnException() throws Exception {
        controllerExceptionHandler.handleException(new Exception("Exception message", new Throwable("Throwable message")));
        verify(emailService, times(1)).email(anyString(), anyString(), anyString());
    }
}
