package edu.sabanciuniv.hotelbookingapp.model.dto;

import edu.sabanciuniv.hotelbookingapp.model.enums.PaymentMethod;
import edu.sabanciuniv.hotelbookingapp.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long id;
    private String confirmationNumber;
    private LocalDateTime bookingDate;
    private Long customerId;
    private Long hotelId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private List<RoomSelectionDTO> roomSelections = new ArrayList<>();
    private BigDecimal totalPrice;
    private String hotelName;
    private AddressDTO hotelAddress;
    private String customerName;
    private String customerEmail;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    
}
