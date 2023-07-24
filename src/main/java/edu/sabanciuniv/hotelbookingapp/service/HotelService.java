package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Hotel;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelRegistrationDTO;

public interface HotelService {

    HotelDTO findHotelDtoByName(String name);

    Hotel saveHotel(HotelRegistrationDTO hotelRegistrationDTO);

}
