package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.dto.HotelAvailabilityDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelSearchDTO;
import edu.sabanciuniv.hotelbookingapp.service.HotelSearchService;
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

import java.time.LocalDate;
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
    public String findAvailableHotelsByCityAndDate(@Valid @ModelAttribute("hotelSearchDTO") HotelSearchDTO hotelSearchDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "hotelsearch/search";
        }
        try {
            validateCheckinAndCheckoutDates(hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
        } catch (IllegalArgumentException e) {
            result.rejectValue("checkoutDate", null, e.getMessage());
            return "hotelsearch/search";
        }
        try {
            log.info("Searching for hotels in the city {} between dates {} and {}", hotelSearchDTO.getCity(), hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
            List<HotelAvailabilityDTO> hotelAvailabilityDTOS = hotelSearchService.findAvailableHotelsByCityAndDate(hotelSearchDTO.getCity(), hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
            model.addAttribute("hotels", hotelAvailabilityDTOS);

            long days = ChronoUnit.DAYS.between(hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
            model.addAttribute("days", days);

            return "hotelsearch/search-results";
        } catch (Exception e) {
            log.error("An error occurred while searching for hotels", e);
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "hotelsearch/search";
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

}
