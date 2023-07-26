package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Hotel hotel;

    @CreationTimestamp
    private LocalDate bookingDate;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    // private int bookedSingleRooms;
    // private int bookedDoubleRooms;

    @OneToOne(mappedBy = "booking")
    private Payment payment;

}
