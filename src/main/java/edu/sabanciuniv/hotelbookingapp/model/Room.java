package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private double pricePerNight;

    private int roomCount;
}
