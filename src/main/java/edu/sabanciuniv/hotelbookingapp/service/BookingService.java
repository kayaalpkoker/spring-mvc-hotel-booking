package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Booking;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingInitiationDTO;

import java.util.Optional;

public interface BookingService {

    Booking saveBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId);

    BookingDTO confirmBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId);

    Optional<BookingDTO> findBookingByIdAndUsername(Long bookingId, String username);

    BookingDTO mapBookingModelToBookingDto(Booking booking);

}
