package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unidirectional relationship due to business logic and performance considerations.
    // Logic: Querying Availability based on specific dates, rather than getting all Availability entities for a Hotel
    @ManyToOne
    private Hotel hotel;

    private LocalDate date;

    @ManyToOne
    private Room room;

    private int availableRooms;

}
