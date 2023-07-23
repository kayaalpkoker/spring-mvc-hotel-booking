package edu.sabanciuniv.hotelbookingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    // private final CustomerService customerService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "customer/dashboard";
    }
}

