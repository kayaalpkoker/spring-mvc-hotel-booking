package edu.sabanciuniv.hotelbookingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewHomePage(){
        return "index";
    }

    /*
    @GetMapping("/registration")
    public String viewRegisterPage(){
        return "registration";
    }

     */

    @GetMapping("/login")
    public String viewLoginPage(){
        return "login";
    }

}

