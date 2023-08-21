package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.service.BookingService;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "customer/dashboard";
    }

    @GetMapping("/bookings")
    public String listBookings(Model model, RedirectAttributes redirectAttributes) {
        try {
            Long customerId = getCurrentCustomerId();
            List<BookingDTO> bookingDTOs = bookingService.findBookingsByCustomerId(customerId);
            model.addAttribute("bookings", bookingDTOs);
            return "customer/bookings";
        } catch (EntityNotFoundException e) {
            log.error("No customer found with the provided ID", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Customer not found. Please log in again.");
            return "redirect:/login";
        } catch (Exception e) {
            log.error("An error occurred while listing bookings", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/";
        }
    }

    @GetMapping("/bookings/{id}")
    public String viewBookingDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Long customerId = getCurrentCustomerId();
            BookingDTO bookingDTO = bookingService.findBookingByIdAndCustomerId(id, customerId);
            model.addAttribute("bookingDTO", bookingDTO);

            LocalDate checkinDate = bookingDTO.getCheckinDate();
            LocalDate checkoutDate = bookingDTO.getCheckoutDate();
            long durationDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
            model.addAttribute("days", durationDays);

            return "customer/bookings-details";
        } catch (EntityNotFoundException e) {
            log.error("No booking found with the provided ID", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found. Please try again later.");
            return "redirect:/customer/bookings";
        } catch (Exception e) {
            log.error("An error occurred while displaying booking details", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/";
        }
    }

    private Long getCurrentCustomerId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(username).getCustomer().getId();
    }

}