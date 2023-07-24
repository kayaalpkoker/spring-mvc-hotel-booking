package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    @ElementCollection
    @CollectionTable(name = "availability_room_counts", joinColumns = @JoinColumn(name = "availability_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "room_type")
    @Column(name = "count")
    private Map<RoomType, Integer> availableRoomCounts = new HashMap<>();

}
