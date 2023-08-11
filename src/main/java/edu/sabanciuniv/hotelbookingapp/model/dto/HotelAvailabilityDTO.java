package edu.sabanciuniv.hotelbookingapp.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class HotelAvailabilityDTO {

    private Long id;

    private String name;

    private AddressDTO addressDTO;

    private List<RoomDTO> roomDTOs = new ArrayList<>();

    private Integer maxAvailableSingleRooms;

    private Integer maxAvailableDoubleRooms;

}
