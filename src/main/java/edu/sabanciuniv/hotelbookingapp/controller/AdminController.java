package edu.sabanciuniv.hotelbookingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }

    /*
    @GetMapping("/users")
    public String listUsers() {
        return "admin-users";
    }

    @GetMapping("/hotels")
    public String listHotels() {
        return "admin-hotels";
    }

    @GetMapping("/bookings")
    public String listBookings() {
        return "admin-bookings";
    }
     */

}
