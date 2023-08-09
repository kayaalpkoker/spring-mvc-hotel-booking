package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.Hotel;
import edu.sabanciuniv.hotelbookingapp.model.RoomType;
import edu.sabanciuniv.hotelbookingapp.model.dto.AddressDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.HotelAvailabilityDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.RoomDTO;
import edu.sabanciuniv.hotelbookingapp.repository.HotelRepository;
import edu.sabanciuniv.hotelbookingapp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelRepository hotelRepository;
    private final AddressService addressService;
    private final RoomService roomService;
    private final AvailabilityService availabilityService;

    @Override
    public List<HotelAvailabilityDTO> findAvailableHotelsByCityAndDate(String city, LocalDate checkinDate, LocalDate checkoutDate) {
        log.info("Attempting to find hotels in {} with available rooms from {} to {}", city, checkinDate, checkoutDate);

        // Calculate the number of days between check-in and check-out
        Long numberOfDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);

        // 1. Fetch hotels that satisfy the criteria
        List<Hotel> hotelsWithAvailableRooms = hotelRepository.findHotelsWithAvailableRooms(city, checkinDate, checkoutDate, numberOfDays);

        // 2. Fetch hotels that don't have any availability records for the entire booking range
        List<Hotel> hotelsWithoutAvailabilityRecords = hotelRepository.findHotelsWithoutAvailabilityRecords(city, checkinDate, checkoutDate);

        // 3. Fetch hotels with partial availability; some days with records meeting the criteria and some days without any records
        List<Hotel> hotelsWithPartialAvailabilityRecords = hotelRepository.findHotelsWithPartialAvailabilityRecords(city, checkinDate, checkoutDate, numberOfDays);

        // Combine and deduplicate the hotels using a Set
        Set<Hotel> combinedHotels = new HashSet<>(hotelsWithAvailableRooms);
        combinedHotels.addAll(hotelsWithoutAvailabilityRecords);
        combinedHotels.addAll(hotelsWithPartialAvailabilityRecords);

        log.info("Successfully found {} hotels with available rooms", combinedHotels.size());

        // Convert the combined hotel list to DTOs for the response
        return combinedHotels.stream()
                .map(hotel -> mapHotelToHotelAvailabilityDto(hotel, checkinDate, checkoutDate))
                .collect(Collectors.toList());
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

/*
    public List<HotelAvailabilityDTO> findAvailableHotelsByCityAndDate(String city, LocalDate checkinDate, LocalDate checkoutDate) {
        List<Hotel> hotels = hotelRepository.findHotelsByCity(city);
        List<HotelAvailabilityDTO> availableHotels = new ArrayList<>();

        for (Hotel hotel : hotels) {
            boolean isAnyRoomAvailable = false;

            for (Room room : hotel.getRooms()) {
                if (availabilityService.isRoomAvailable(room.getId(), checkinDate, checkoutDate)) {
                    isAnyRoomAvailable = true;
                    break;  // Break once a room type is found available for the entire range.
                }
            }

            // If at least one room type is available throughout the entire booking range, add the hotel to the result list
            if (isAnyRoomAvailable) {
                availableHotels.add(mapHotelToHotelAvailabilityDto(hotel, checkinDate, checkoutDate));
            }
        }
        return availableHotels;
    }
     */