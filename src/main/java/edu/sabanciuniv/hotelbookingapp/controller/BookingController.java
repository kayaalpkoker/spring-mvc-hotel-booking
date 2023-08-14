package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.dto.BookingInitiationDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.PaymentCardDTO;
import edu.sabanciuniv.hotelbookingapp.service.HotelService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final HotelService hotelService;

    @PostMapping("/initiate")
    public String initiateBooking(@ModelAttribute BookingInitiationDTO bookingInitiationDTO, RedirectAttributes redirectAttributes, HttpSession session) {
        session.setAttribute("bookingInitiationDTO", bookingInitiationDTO);
        log.debug("BookingInitiationDTO set in session: {}", bookingInitiationDTO);
        return "redirect:/booking/payment";
    }

    @GetMapping("/payment")
    public String showPaymentPage(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        BookingInitiationDTO bookingInitiationDTO = (BookingInitiationDTO) session.getAttribute("bookingInitiationDTO");
        log.debug("BookingInitiationDTO retrieved from session: {}", bookingInitiationDTO);

        if (bookingInitiationDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your session has expired. Please start a new search.");
            return "redirect:/search";
        }

        HotelDTO hotelDTO = hotelService.findHotelById(bookingInitiationDTO.getHotelId());

        model.addAttribute("bookingInitiationDTO", bookingInitiationDTO);
        model.addAttribute("hotelDTO", hotelDTO);
        model.addAttribute("paymentCardDTO", new PaymentCardDTO());

        return "booking/payment";
    }

    @PostMapping("/payment")
    public String completeBooking(@Valid @ModelAttribute("paymentCardDTO") PaymentCardDTO paymentDTO, BindingResult result, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        BookingInitiationDTO bookingInitiationDTO = (BookingInitiationDTO) session.getAttribute("bookingInitiationDTO");
        log.debug("BookingInitiationDTO retrieved from session at the beginning of completeBooking: {}", bookingInitiationDTO);
        if (bookingInitiationDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your session has expired. Please start a new search.");
            return "redirect:/search";
        }

        if (result.hasErrors()) {
            log.warn("Validation errors occurred while completing booking");
            HotelDTO hotelDTO = hotelService.findHotelById(bookingInitiationDTO.getHotelId());
            model.addAttribute("bookingInitiationDTO", bookingInitiationDTO);
            model.addAttribute("hotelDTO", hotelDTO);
            model.addAttribute("paymentCardDTO", paymentDTO);
            return "booking/payment";
        }

        try {
            // Map bookingDTO and paymentDTO to entities...
            // Save to the database

            return "bookingConfirmation";
        } catch (Exception e) {
            log.error("An error occurred while completing the booking", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/booking/payment"; // Redirect back to the payment page
        }
    }
}