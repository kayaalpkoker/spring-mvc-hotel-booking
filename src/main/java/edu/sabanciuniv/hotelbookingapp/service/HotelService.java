package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Hotel;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelRegistrationDTO;

import java.util.List;

public interface HotelService {

    Hotel saveHotel(HotelRegistrationDTO hotelRegistrationDTO);

    HotelDTO findHotelDtoByName(String name);

    HotelDTO findHotelById(Long id);

    List<HotelDTO> findAllHotels();

    HotelDTO updateHotel(HotelDTO hotelDTO);

    void deleteHotelById(Long id);

    List<HotelDTO> findAllHotelsByManagerId(Long managerId);

    HotelDTO findHotelByIdAndManagerId(Long hotelId, Long managerId);

    HotelDTO updateHotelByManagerId(HotelDTO hotelDTO, Long managerId);

    void deleteHotelByIdAndManagerId(Long hotelId, Long managerId);

    HotelDTO mapHotelToHotelDto(Hotel hotel);

}
