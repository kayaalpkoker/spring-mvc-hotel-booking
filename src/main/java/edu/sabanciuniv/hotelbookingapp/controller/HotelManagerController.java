package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.exception.HotelAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.RoomType;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.RoomCountDTO;
import edu.sabanciuniv.hotelbookingapp.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class HotelManagerController {

    private final HotelService hotelService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "hotelmanager/dashboard";
    }

    @GetMapping("/hotels")
    public String manageHotels() {
        return "hotelmanager/hotels";
    }

    @GetMapping("/hotels/add")
    public String showAddHotelForm(Model model) {
        HotelRegistrationDTO hotelRegistrationDTO = new HotelRegistrationDTO();

        // Initialize roomCountDTOS with SINGLE and DOUBLE room types
        RoomCountDTO singleRoom = new RoomCountDTO(RoomType.SINGLE, 0);
        RoomCountDTO doubleRoom = new RoomCountDTO(RoomType.DOUBLE, 0);
        hotelRegistrationDTO.getRoomCountDTOS().add(singleRoom);
        hotelRegistrationDTO.getRoomCountDTOS().add(doubleRoom);

        model.addAttribute("hotel", hotelRegistrationDTO);
        return "hotelmanager/hotels-add";
    }

    @PostMapping("/hotels/add")
    public String addHotel(@Valid @ModelAttribute("hotel") HotelRegistrationDTO hotelRegistrationDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.warn("Hotel creation failed due to validation errors: {}", result.getAllErrors());
            return "hotelmanager/hotels-add";
        }
        try {
            hotelService.saveHotel(hotelRegistrationDTO);
            redirectAttributes.addFlashAttribute("hotelName", hotelRegistrationDTO.getName());
            return "redirect:/hotelmanager/hotels?success";
        } catch (HotelAlreadyExistsException e) {
            result.rejectValue("name", "hotel.exists", e.getMessage());
            return "hotelmanager/hotels-add";
        }
    }


    @GetMapping("/hotels/{hotelId}/edit")
    public String editHotel(@PathVariable Long hotelId) {
        // hotelId can be used to load the particular hotel's data
        // TODO: 24.07.2023
        return "hotelmanager/hotels-edit";
    }

    @GetMapping("/bookings")
    public String manageBookings() {
        // TODO: 24.07.2023
        return "hotelmanager/bookings";
    }

}
