package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    private String name;

    @OneToOne
    private Address address;

    @ManyToOne
    private HotelManager hotelManager;

    @OneToMany(mappedBy = "hotel")
    private List<Booking> bookingList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "room_counts", joinColumns = @JoinColumn(name = "hotel_id"))
    @MapKeyColumn(name = "room_type")
    @Column(name = "room_count")
    private Map<RoomType, Integer> roomCounts;


}
