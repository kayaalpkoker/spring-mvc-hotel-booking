package edu.sabanciuniv.hotelbookingapp.model;

import edu.sabanciuniv.hotelbookingapp.model.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private int count;

}
