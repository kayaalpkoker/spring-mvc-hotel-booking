package edu.sabanciuniv.hotelbookingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class HotelManagerController {

    //private final HotelService hotelService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "hotelmanager/dashboard";
    }

    @GetMapping("/hotels")
    public String manageHotels() {
        return "hotelmanager/hotels";
    }

    @GetMapping("/hotels/add")
    public String addHotel() {
        return "hotelmanager/hotel-add";
    }

    @GetMapping("/hotels/{hotelId}/edit")
    public String editHotel(@PathVariable Long hotelId) {
        // hotelId can be used to load the particular hotel's data
        return "hotelmanager/hotel-edit";
    }

    @GetMapping("/bookings")
    public String manageBookings() {
        return "hotelmanager/bookings";
    }

}
