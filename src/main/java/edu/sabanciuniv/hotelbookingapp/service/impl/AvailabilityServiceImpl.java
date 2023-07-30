package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.Room;
import edu.sabanciuniv.hotelbookingapp.repository.AvailabilityRepository;
import edu.sabanciuniv.hotelbookingapp.service.AvailabilityService;
import edu.sabanciuniv.hotelbookingapp.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final RoomService roomService;

    @Override
    public Integer getMaxAvailableRooms(Long roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        Room room = roomService.findRoomById(roomId).orElseThrow(() -> new EntityNotFoundException("Room not found"));

        return availabilityRepository.getMaxAvailableRooms(roomId, checkinDate, checkoutDate)
                .orElse(room.getRoomCount()); // Consider no record as full availability
    }
}
