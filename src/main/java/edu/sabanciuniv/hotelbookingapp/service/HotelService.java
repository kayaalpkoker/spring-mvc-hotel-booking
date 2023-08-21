package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Hotel;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    Hotel saveHotel(HotelRegistrationDTO hotelRegistrationDTO);

    HotelDTO findHotelDtoByName(String name);

    HotelDTO findHotelDtoById(Long id);

    Optional<Hotel> findHotelById(Long id);

    List<HotelDTO> findAllHotels();

    HotelDTO updateHotel(HotelDTO hotelDTO);

    void deleteHotelById(Long id);

    List<Hotel> findAllHotelsByManagerId(Long managerId);

    List<HotelDTO> findAllHotelDtosByManagerId(Long managerId);

    HotelDTO findHotelByIdAndManagerId(Long hotelId, Long managerId);

    HotelDTO updateHotelByManagerId(HotelDTO hotelDTO, Long managerId);

    void deleteHotelByIdAndManagerId(Long hotelId, Long managerId);

    HotelDTO mapHotelToHotelDto(Hotel hotel);

}
