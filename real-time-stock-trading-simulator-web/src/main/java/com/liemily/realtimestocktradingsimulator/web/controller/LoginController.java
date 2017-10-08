package com.liemily.realtimestocktradingsimulator.web.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Emily Li on 16/07/2017.
 */
@Controller
@Lazy
public class LoginController {
    @RequestMapping("login")
    String login() {
        return "login";
    }
}
