package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.repository.BookingRepository;
import edu.sabanciuniv.hotelbookingapp.service.AvailabilityService;
import edu.sabanciuniv.hotelbookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AvailabilityService availabilityService;

    @Override
    public void processBooking(/* Relevant data for booking */) {
        // Create a booking entry and save
        // Update the availability of the selected hotel's rooms
    }
}
