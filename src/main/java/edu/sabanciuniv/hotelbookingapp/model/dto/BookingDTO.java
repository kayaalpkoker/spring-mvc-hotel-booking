package edu.sabanciuniv.hotelbookingapp.model.dto;

import edu.sabanciuniv.hotelbookingapp.model.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long id;
    private LocalDate bookingDate;
    private Long customerId;
    private Long hotelId;
    private String checkinDate;
    private String checkoutDate;
    private List<RoomSelection> roomSelections = new ArrayList<>();
    private BigDecimal totalPrice;
    private String hotelName;
    private String customerName;
    private String customerEmail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomSelection {
        private RoomType roomType;
        private int count;

    }
}
