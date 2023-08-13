package edu.sabanciuniv.hotelbookingapp.model;

import edu.sabanciuniv.hotelbookingapp.model.enums.Currency;
import edu.sabanciuniv.hotelbookingapp.model.enums.PaymentMethod;
import edu.sabanciuniv.hotelbookingapp.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @CreationTimestamp
    private LocalDate paymentDate;

    @OneToOne
    private Booking booking;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD; // Default to CREDIT_CARD

    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.USD; // Default to USD

}
