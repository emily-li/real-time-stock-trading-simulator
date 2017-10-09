package com.liemily.realtimestocktradingsimulator.web.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Emily Li on 09/10/2017.
 */
@Controller
@Lazy
class ReportController {

    @RequestMapping("report")
    public String report() {
        return "report";
    }
}
