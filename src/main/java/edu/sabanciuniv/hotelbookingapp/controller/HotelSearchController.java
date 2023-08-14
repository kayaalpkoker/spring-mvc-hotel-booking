package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.dto.HotelAvailabilityDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelSearchDTO;
import edu.sabanciuniv.hotelbookingapp.service.HotelSearchService;
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
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HotelSearchController {

    private final HotelSearchService hotelSearchService;

    @GetMapping("/search")
    public String showSearchForm(@ModelAttribute("hotelSearchDTO") HotelSearchDTO hotelSearchDTO) {
        return "hotelsearch/search";
    }


    @PostMapping("/search")
    public String findAvailableHotelsByCityAndDate(@Valid @ModelAttribute("hotelSearchDTO") HotelSearchDTO hotelSearchDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "hotelsearch/search";
        }
        try {
            validateCheckinAndCheckoutDates(hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
        } catch (IllegalArgumentException e) {
            result.rejectValue("checkoutDate", null, e.getMessage());
            return "hotelsearch/search";
        }

        // Redirect to a new GET endpoint with parameters for data fetching. Allows page refreshing
        return String.format("redirect:/search-results?city=%s&checkinDate=%s&checkoutDate=%s", hotelSearchDTO.getCity(), hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
    }

    @GetMapping("/search-results")
    public String showSearchResults(@RequestParam String city, @RequestParam String checkinDate, @RequestParam String checkoutDate, Model model, RedirectAttributes redirectAttributes) {
        try {
            LocalDate parsedCheckinDate = LocalDate.parse(checkinDate);
            LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate);

            validateCheckinAndCheckoutDates(parsedCheckinDate, parsedCheckoutDate);

            log.info("Searching hotels for city {} between dates {} and {}", city, checkinDate, checkoutDate);
            List<HotelAvailabilityDTO> hotels = hotelSearchService.findAvailableHotelsByCityAndDate(city, parsedCheckinDate, parsedCheckoutDate);

            if (hotels.isEmpty()) {
                model.addAttribute("noHotelsFound", true);
            }

            long durationDays = ChronoUnit.DAYS.between(parsedCheckinDate, parsedCheckoutDate);

            model.addAttribute("hotels", hotels);
            model.addAttribute("city", city);
            model.addAttribute("days", durationDays);
            model.addAttribute("checkinDate", checkinDate);
            model.addAttribute("checkoutDate", checkoutDate);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date format. Please use the search form.");
            return "redirect:/search";
        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/search";
        } catch (Exception e) {
            log.error("An error occurred while searching for hotels", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/search";
        }

        return "hotelsearch/search-results";
    }

    @GetMapping("/hotel-details/{id}")
    public String showHotelDetails(@PathVariable Long id, @RequestParam String checkinDate, @RequestParam String checkoutDate, Model model, RedirectAttributes redirectAttributes) {
        try {
            LocalDate parsedCheckinDate = LocalDate.parse(checkinDate);
            LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate);

            validateCheckinAndCheckoutDates(parsedCheckinDate, parsedCheckoutDate);

            HotelAvailabilityDTO hotelAvailabilityDTO = hotelSearchService.findAvailableHotelById(id, parsedCheckinDate, parsedCheckoutDate);

            long durationDays = ChronoUnit.DAYS.between(parsedCheckinDate, parsedCheckoutDate);

            model.addAttribute("hotel", hotelAvailabilityDTO);
            model.addAttribute("durationDays", durationDays);
            model.addAttribute("checkinDate", checkinDate);
            model.addAttribute("checkoutDate", checkoutDate);

            return "hotelsearch/hotel-details";


        } catch (DateTimeParseException e) {
            log.error("Invalid date format provided", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date format. Please use the search form.");
            return "redirect:/search";
        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments provided for URL search", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/search";
        } catch (EntityNotFoundException e) {
            log.error("No hotel found with ID {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "The selected hotel is no longer available. Please start a new search.");
            return "redirect:/search";
        } catch (Exception e) {
            log.error("An error occurred while searching for hotels", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "redirect:/search";
        }
    }

    private void validateCheckinAndCheckoutDates(LocalDate checkinDate, LocalDate checkoutDate) {
        if (checkinDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        if (checkoutDate.isBefore(checkinDate.plusDays(1))) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    private void parseAndValidateBookingDates(String checkinDate, String checkoutDate) {
        LocalDate parsedCheckinDate = LocalDate.parse(checkinDate);
        LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate);
        validateCheckinAndCheckoutDates(parsedCheckinDate, parsedCheckoutDate);
    }

}