package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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

    @CreationTimestamp
    private LocalDate bookingDate;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Hotel hotel;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedRoom> bookedRooms = new ArrayList<>();

    @OneToOne(mappedBy = "booking")
    private Payment payment;

}
