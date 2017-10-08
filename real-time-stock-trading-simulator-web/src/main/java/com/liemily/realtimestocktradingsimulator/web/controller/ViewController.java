package com.liemily.realtimestocktradingsimulator.web.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Emily Li on 08/10/2017.
 */
@Controller
@Lazy
public class ViewController {
    @RequestMapping("/")
    String index() {
        return "redirect:" + StocksController.getStocksPage();
    }
}
