package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.PaymentDTO;
import edu.sabanciuniv.hotelbookingapp.service.BookingService;
import edu.sabanciuniv.hotelbookingapp.service.HotelSearchService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final HotelSearchService hotelSearchService;
    private final BookingService bookingService;

    @PostMapping("/initiate-booking")
    public String initiateBooking(@ModelAttribute BookingDTO bookingDTO, RedirectAttributes redirectAttributes, HttpSession session) {
        session.setAttribute("bookingDTO", bookingDTO);
        return "redirect:/payment";
    }

    @GetMapping("/payment")
    public String showPaymentPage(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO");
        if (bookingDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No booking details found. Please start a new search.");
            return "redirect:/search";
        }

        model.addAttribute("bookingDTO", bookingDTO);
        model.addAttribute("paymentDTO", new PaymentDTO());

        return "payment";
    }

    @PostMapping("/finalize-booking")
    public String finalizeBooking(@Valid @ModelAttribute("paymentDTO") PaymentDTO paymentDTO, BindingResult result, HttpSession session) {
        // Validate paymentDTO...

        BookingDTO bookingDTO = (BookingDTO) session.getAttribute("bookingDTO");

        // Map bookingDTO and paymentDTO to entities...
        //Booking booking = convertToEntity(bookingDTO);

        // Save to the database

        return "bookingConfirmation";
    }

}
