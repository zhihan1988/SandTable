package com.rathink.ie.system.basic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pgwt on 10/3/15.
 */
@Controller
public class HomeController {

    @RequestMapping({"/home.do"})
    public String home(){
        return "/home";
    }

}
