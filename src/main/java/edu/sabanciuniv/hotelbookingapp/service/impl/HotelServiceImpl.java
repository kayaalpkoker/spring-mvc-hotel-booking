package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.exception.HotelAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.*;
import edu.sabanciuniv.hotelbookingapp.model.dto.AddressDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.RoomCountDTO;
import edu.sabanciuniv.hotelbookingapp.repository.HotelRepository;
import edu.sabanciuniv.hotelbookingapp.service.AddressService;
import edu.sabanciuniv.hotelbookingapp.service.HotelManagerService;
import edu.sabanciuniv.hotelbookingapp.service.HotelService;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final HotelManagerService hotelManagerService;

    @Override
    public HotelDTO findHotelDtoByName(String name) {
        Hotel hotel = hotelRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        return mapHotelToHotelDto(hotel);
    }

    @Override
    @Transactional
    public Hotel saveHotel(HotelRegistrationDTO hotelRegistrationDTO) {
        log.info("Attempting to save a new hotel: {}", hotelRegistrationDTO.toString());

        Optional<Hotel> existingHotel = hotelRepository.findByName(hotelRegistrationDTO.getName());
        if (existingHotel.isPresent()) {
            throw new HotelAlreadyExistsException("This hotel is already registered!");
        }

        Hotel hotel = mapHotelRegistrationDtoToHotel(hotelRegistrationDTO);
        Address savedAddress = addressService.saveAddress(hotelRegistrationDTO.getAddressDTO());
        hotel.setAddress(savedAddress);

        // Get the username of the currently logged-in hotel manager
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Retrieve the Hotel Manager associated with this username
        HotelManager hotelManager = hotelManagerService.findByUser(userService.findUserByUsername(username));
        hotel.setHotelManager(hotelManager);

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Successfully saved new hotel with ID: {}", hotel.getId());
        return savedHotel;
    }

    private Hotel mapHotelRegistrationDtoToHotel(HotelRegistrationDTO dto) {
        Map<RoomType, Integer> roomCounts = dto.getRoomCountDTOS().stream()
                .collect(Collectors.toMap(RoomCountDTO::getRoomType, RoomCountDTO::getCount));

        return Hotel.builder()
                .name(dto.getName())
                .roomCounts(roomCounts)
                .build();
    }

    private HotelDTO mapHotelToHotelDto(Hotel hotel) {
        // Convert Map<RoomType, Integer> to List<RoomCountDTO>
        List<RoomCountDTO> roomCountDTOs = hotel.getRoomCounts().entrySet().stream()
                .map(entry -> new RoomCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        // Map Address to AddressDTO
        AddressDTO addressDTO = mapAddressToAddressDto(hotel.getAddress());

        return HotelDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .addressDTO(addressDTO)
                .roomCountDTOS(roomCountDTOs)
                .build();
    }

    private AddressDTO mapAddressToAddressDto(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }
}
