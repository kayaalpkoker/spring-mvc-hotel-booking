package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.Availability;
import edu.sabanciuniv.hotelbookingapp.model.Hotel;
import edu.sabanciuniv.hotelbookingapp.model.Room;
import edu.sabanciuniv.hotelbookingapp.model.RoomType;
import edu.sabanciuniv.hotelbookingapp.model.dto.AddressDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelAvailabilityDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.RoomDTO;
import edu.sabanciuniv.hotelbookingapp.repository.AvailabilityRepository;
import edu.sabanciuniv.hotelbookingapp.repository.HotelRepository;
import edu.sabanciuniv.hotelbookingapp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelRepository hotelRepository;
    private final AddressService addressService;
    private final RoomService roomService;
    private final AvailabilityService availabilityService;

    // Service must do it!
    private final AvailabilityRepository availabilityRepository;

    /*
    // Old one

    @Override
    public List<HotelAvailabilityDTO> findAvailableHotelsByCityAndDate(String city, LocalDate checkinDate, LocalDate checkoutDate) {
        long days = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
        log.info("Attempting to find hotels in {} with available rooms from {} to {}", city, checkinDate, checkoutDate);
        List<Hotel> hotels = hotelRepository.findAvailableHotelsByCityAndDate(city, checkinDate, checkoutDate, days);
        log.info("Successfully found {} hotels with available rooms", hotels.size());
        return hotels.stream().map(hotel -> mapHotelToHotelAvailabilityDto(hotel, checkinDate, checkoutDate)).collect(Collectors.toList());
    }
     */

    public List<HotelAvailabilityDTO> findAvailableHotelsByCityAndDate(String city, LocalDate checkinDate, LocalDate checkoutDate) {
        List<Hotel> hotels = hotelRepository.findHotelsByCity(city);
        List<HotelAvailabilityDTO> availableHotels = new ArrayList<>();

        for (Hotel hotel : hotels) {
            boolean isAvailable = true;
            for (Room room : hotel.getRooms()) {
                List<Availability> availabilities = availabilityRepository.findByRoom_IdAndDateBetween(room.getId(), checkinDate, checkoutDate);

                // If there's no availability entry for a room, it means it's fully available
                if (availabilities.isEmpty()) continue;

                // If there are availability entries, check if there's enough rooms for each day
                for (Availability availability : availabilities) {
                    if (availability.getAvailableRooms() < 1) {
                        isAvailable = false;
                        break;
                    }
                }

                // If a room is not available, the hotel is not available
                if (!isAvailable) break;
            }

            // If all rooms of the hotel are available, add it to the result list
            if (isAvailable) {
                availableHotels.add(mapHotelToHotelAvailabilityDto(hotel, checkinDate, checkoutDate));
            }
        }
        return availableHotels;
    }

    @Override
    public HotelAvailabilityDTO mapHotelToHotelAvailabilityDto(Hotel hotel, LocalDate checkinDate, LocalDate checkoutDate) {
        List<RoomDTO> roomDTOs = hotel.getRooms().stream()
                .map(roomService::mapRoomToRoomDto)  // convert each Room to RoomDTO
                .collect(Collectors.toList());

        AddressDTO addressDTO = addressService.mapAddressToAddressDto(hotel.getAddress());
        
        HotelAvailabilityDTO hotelAvailabilityDTO = HotelAvailabilityDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .addressDTO(addressDTO)
                .roomDTOs(roomDTOs)
                .build();
        
        // For each room type, find the minimum available rooms across the date range
        int maxAvailableSingleRooms = hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == RoomType.SINGLE)
                .mapToInt(room -> availabilityService.getMinAvailableRooms(room.getId(), checkinDate, checkoutDate))
                .max()
                .orElse(0); // Assume no single rooms if none match the filter
        hotelAvailabilityDTO.setMaxAvailableSingleRooms(maxAvailableSingleRooms);

        int maxAvailableDoubleRooms = hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == RoomType.DOUBLE)
                .mapToInt(room -> availabilityService.getMinAvailableRooms(room.getId(), checkinDate, checkoutDate))
                .max()
                .orElse(0); // Assume no double rooms if none match the filter
        hotelAvailabilityDTO.setMaxAvailableDoubleRooms(maxAvailableDoubleRooms);

        return hotelAvailabilityDTO;
    }

}
