package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.exception.HotelAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.exception.UsernameAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.service.BookingService;
import edu.sabanciuniv.hotelbookingapp.service.HotelService;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final HotelService hotelService;
    private final BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDTO> userDTOList = userService.findAllUsers();
        model.addAttribute("users", userDTOList);
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.findUserById(id);
        model.addAttribute("user", userDTO);
        return "admin/users-edit";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, @Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/users-edit";
        }
        try {
            userService.updateUser(userDTO);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", "user.exists", "Username is already registered!");
            return "admin/users-edit";
        }

        redirectAttributes.addFlashAttribute("updatedUserId", userDTO.getId());
        return "redirect:/admin/users?success";
    }

    // Workaround for @DeleteMapping via post method
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/hotels")
    public String listHotels(Model model) {
        List<HotelDTO> hotelDTOList = hotelService.findAllHotels();
        model.addAttribute("hotels", hotelDTOList);
        return "admin/hotels";
    }

    @GetMapping("/hotels/edit/{id}")
    public String showEditHotelForm(@PathVariable Long id, Model model) {
        HotelDTO hotelDTO = hotelService.findHotelDtoById(id);
        model.addAttribute("hotel", hotelDTO);
        return "admin/hotels-edit";
    }

    @PostMapping("/hotels/edit/{id}")
    public String editHotel(@PathVariable Long id, @Valid @ModelAttribute("hotel") HotelDTO hotelDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/hotels-edit";
        }
        try {
            hotelService.updateHotel(hotelDTO);
        } catch (HotelAlreadyExistsException e) {
            result.rejectValue("name", "hotel.exists", e.getMessage());
            return "admin/hotels-edit";
        }

        redirectAttributes.addFlashAttribute("updatedHotelId", hotelDTO.getId());
        return "redirect:/admin/hotels?success";
    }

    @PostMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotelById(id);
        return "redirect:/admin/hotels";
    }

    @GetMapping("/bookings")
    public String listBookings(Model model) {
        List<BookingDTO> bookingDTOList = bookingService.findAllBookings();
        model.addAttribute("bookings", bookingDTOList);
        return "admin/bookings";
    }

    @GetMapping("/bookings/{id}")
    public String viewBookingDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            BookingDTO bookingDTO = bookingService.findBookingById(id);
            model.addAttribute("bookingDTO", bookingDTO);

            LocalDate checkinDate = bookingDTO.getCheckinDate();
            LocalDate checkoutDate = bookingDTO.getCheckoutDate();
            long durationDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
            model.addAttribute("days", durationDays);

            return "admin/bookings-details";
        } catch (EntityNotFoundException e) {
            log.error("No booking found with the provided ID", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found. Please try again later.");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            log.error("An error occurred while displaying booking details", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/admin/dashboard";
        }
    }

}
