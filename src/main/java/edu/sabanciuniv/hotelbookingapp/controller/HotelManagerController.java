package edu.sabanciuniv.hotelbookingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class HotelManagerController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "manager-dashboard";
    }

    @GetMapping("/hotels")
    public String manageHotels() {
        return "manager-hotels";
    }

    @GetMapping("/hotels/add")
    public String addHotel() {
        return "manager-hotel-add";
    }

    @GetMapping("/hotels/{hotelId}/edit")
    public String editHotel(@PathVariable Long hotelId) {
        // hotelId can be used to load the particular hotel's data
        return "manager-hotel-edit";
    }

    @GetMapping("/bookings")
    public String manageBookings() {
        return "manager-bookings";
    }

}
