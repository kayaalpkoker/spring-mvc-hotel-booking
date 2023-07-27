package edu.sabanciuniv.hotelbookingapp.model.dto;

import edu.sabanciuniv.hotelbookingapp.model.RoomType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long id;

    private Long hotelId;

    private RoomType roomType;

    @NotNull(message = "Room count cannot be empty")
    @PositiveOrZero(message = "Room amount must be 0 or more")
    private Integer roomCount;

    @NotNull(message = "Price cannot be empty")
    @PositiveOrZero(message = "Room amount must be 0 or more")
    private Double pricePerNight;

}