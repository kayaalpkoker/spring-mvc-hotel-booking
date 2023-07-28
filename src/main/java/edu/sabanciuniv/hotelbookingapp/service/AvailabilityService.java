package edu.sabanciuniv.hotelbookingapp.service;

import java.time.LocalDate;

public interface AvailabilityService {

    Integer getMinAvailableRooms(Long roomId, LocalDate checkinDate, LocalDate checkoutDate);

}
