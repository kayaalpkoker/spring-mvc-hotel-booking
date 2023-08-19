package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.*;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingInitiationDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.RoomSelectionDTO;
import edu.sabanciuniv.hotelbookingapp.repository.BookingRepository;
import edu.sabanciuniv.hotelbookingapp.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AvailabilityService availabilityService;
    private final PaymentService paymentService;
    private final CustomerService customerService;
    private final HotelService hotelService;


    @Override
    @Transactional
    public Booking saveBooking(BookingInitiationDTO bookingInitiationDTO, Long userId) {
        validateBookingDates(bookingInitiationDTO.getCheckinDate(), bookingInitiationDTO.getCheckoutDate());

        Customer customer = customerService.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with user ID: " + userId));

        Hotel hotel = hotelService.findHotelById(bookingInitiationDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with ID: " + bookingInitiationDTO.getHotelId()));

        Booking booking = mapBookingInitDtoToBookingModel(bookingInitiationDTO, customer, hotel);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public BookingDTO confirmBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId) {
        Booking savedBooking = this.saveBooking(bookingInitiationDTO, customerId);
        Payment savedPayment = paymentService.savePayment(bookingInitiationDTO, savedBooking);
        savedBooking.setPayment(savedPayment);
        bookingRepository.save(savedBooking);
        // Step 3: Update/save the availability of the selected hotel's rooms
        availabilityService.updateAvailabilities(
                bookingInitiationDTO.getHotelId(),
                bookingInitiationDTO.getCheckinDate(),
                bookingInitiationDTO.getCheckoutDate(),
                bookingInitiationDTO.getRoomSelections());
        return null;
    }

    private void validateBookingDates(LocalDate checkinDate, LocalDate checkoutDate) {
        if (checkinDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        if (checkoutDate.isBefore(checkinDate.plusDays(1))) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    private Booking mapBookingInitDtoToBookingModel(BookingInitiationDTO bookingInitiationDTO, Customer customer, Hotel hotel) {
        Booking booking = Booking.builder()
                .customer(customer)
                .hotel(hotel)
                .checkinDate(bookingInitiationDTO.getCheckinDate())
                .checkoutDate(bookingInitiationDTO.getCheckoutDate())
                .build();

        for (RoomSelectionDTO roomSelection : bookingInitiationDTO.getRoomSelections()) {
            if (roomSelection.getCount() > 0) {
                BookedRoom bookedRoom = BookedRoom.builder()
                        .booking(booking)
                        .roomType(roomSelection.getRoomType())
                        .count(roomSelection.getCount())
                        .build();
                booking.getBookedRooms().add(bookedRoom);
            }
        }

        return booking;
    }

}
