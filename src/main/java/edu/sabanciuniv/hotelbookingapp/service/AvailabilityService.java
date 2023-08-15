package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Availability;

import java.time.LocalDate;

public interface AvailabilityService {

    Availability saveAvailability();

    Integer getMinAvailableRooms(Long roomId, LocalDate checkinDate, LocalDate checkoutDate);

}
