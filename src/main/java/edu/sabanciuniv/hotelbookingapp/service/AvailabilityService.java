package edu.sabanciuniv.hotelbookingapp.service;

import java.time.LocalDate;

public interface AvailabilityService {

    Integer getMaxAvailableRooms(Long roomId, LocalDate checkinDate, LocalDate checkoutDate);

}
