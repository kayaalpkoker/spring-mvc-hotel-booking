package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne
    private Address address;

    @ElementCollection
    @CollectionTable(name = "hotel_room_counts", joinColumns = @JoinColumn(name = "hotel_id"))
    @MapKeyColumn(name = "room_type")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "count")
    private Map<RoomType, Integer> roomCounts = new HashMap<>();

    @OneToMany(mappedBy = "hotel")
    private List<Booking> bookingList = new ArrayList<>();

    @ManyToOne
    private HotelManager hotelManager;
}
