package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

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

    // Unidirectional relationship due to business logic and performance considerations
    // Logic: Querying Availability based on specific dates, rather than getting all Availability entities for a Hotel
    @ManyToOne
    @JoinColumn(nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;

    @Column(nullable = false)
    private int availableRooms;

    @Override
    public String toString() {
        return "Availability{" +
                "id=" + id +
                ", hotel=" + hotel +
                ", date=" + date +
                ", room=" + room +
                ", availableRooms=" + availableRooms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Availability that = (Availability) o;
        return Objects.equals(id, that.id) && Objects.equals(hotel, that.hotel) && Objects.equals(room, that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hotel, room);
    }
}
