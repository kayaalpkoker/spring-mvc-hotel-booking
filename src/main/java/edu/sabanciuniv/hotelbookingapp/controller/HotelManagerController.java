package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.exception.HotelAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.RoomType;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.RoomCountDTO;
import edu.sabanciuniv.hotelbookingapp.service.HotelService;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class HotelManagerController {

    private final HotelService hotelService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "hotelmanager/dashboard";
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

    @GetMapping("/hotels")
    public String listHotels(Model model) {
        Long managerId = getCurrentManagerId();
        List<HotelDTO> hotelList = hotelService.findAllHotelsByManagerId(managerId);
        model.addAttribute("hotels", hotelList);
        return "hotelmanager/hotels";
    }

    @GetMapping("/hotels/edit/{id}")
    public String showEditHotelForm(@PathVariable Long id, Model model) {
        Long managerId = getCurrentManagerId();
        HotelDTO hotelDTO = hotelService.findHotelByIdAndManagerId(id, managerId);
        model.addAttribute("hotel", hotelDTO);
        return "hotelmanager/hotels-edit";
    }

    @PostMapping("/hotels/edit/{id}")
    public String editHotel(@PathVariable Long id, @Valid @ModelAttribute("hotel") HotelDTO hotelDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "hotelmanager/hotels-edit";
        }
        try {
            Long managerId = getCurrentManagerId();
            hotelDTO.setId(id); // set the id from the path variable
            hotelService.updateHotelByManagerId(hotelDTO, managerId);
        } catch (HotelAlreadyExistsException e) {
            result.rejectValue("name", "hotel.exists", e.getMessage());
            return "hotelmanager/hotels-edit";
        }
        redirectAttributes.addFlashAttribute("updatedHotelId", hotelDTO.getId());
        return "redirect:/manager/hotels?success";
    }

    @PostMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable Long id) {
        Long managerId = getCurrentManagerId();
        hotelService.deleteHotelByIdAndManagerId(id, managerId);
        return "redirect:/manager/hotels";
    }

    @GetMapping("/bookings")
    public String manageBookings() {
        // TODO: 24.07.2023
        return "hotelmanager/bookings";
    }

    private Long getCurrentManagerId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(username).getHotelManager().getId();
    }
}
