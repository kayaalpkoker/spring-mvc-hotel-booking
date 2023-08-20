package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.Booking;
import edu.sabanciuniv.hotelbookingapp.model.Customer;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.service.BookingService;
import edu.sabanciuniv.hotelbookingapp.service.CustomerService;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "customer/dashboard";
    }

    @GetMapping("/bookings")
    public String listBookings(Model model, RedirectAttributes redirectAttributes) {
        try {
            String username = getCurrentUsername();
            Optional<Customer> customerOptional = customerService.findByUsername(username);

            if (customerOptional.isPresent()) {
                List<Booking> bookings = customerOptional.get().getBookingList();
                List<BookingDTO> bookingDTOs = bookings.stream()
                        .map(bookingService::mapBookingModelToBookingDto)
                        .sorted(Comparator.comparing(BookingDTO::getCheckinDate)) // Sort by check-in date in ascending order
                        .collect(Collectors.toList());
                model.addAttribute("bookings", bookingDTOs);
                return "customer/bookings";
            } else {
                throw new EntityNotFoundException("Customer not found with username: " + username);
            }
        } catch (EntityNotFoundException e) {
            log.error("No customer found with the provided username", e);
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
            String username = getCurrentUsername();
            Optional<BookingDTO> bookingOptional = bookingService.findBookingByIdAndUsername(id, username);

            if (bookingOptional.isPresent()) {
                BookingDTO bookingDTO = bookingOptional.get();
                model.addAttribute("bookingDTO", bookingDTO);

                LocalDate checkinDate = bookingDTO.getCheckinDate();
                LocalDate checkoutDate = bookingDTO.getCheckoutDate();
                long durationDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);

                model.addAttribute("days", durationDays);

                return "customer/bookings-details";
            } else {
                throw new EntityNotFoundException("Booking not found with id: " + id);
            }
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

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}

