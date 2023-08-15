package edu.sabanciuniv.hotelbookingapp.model;

import edu.sabanciuniv.hotelbookingapp.model.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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
    @JoinColumn(nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private int count;

    @Override
    public String toString() {
        return "BookedRoom{" +
                "id=" + id +
                ", booking=" + booking +
                ", roomType=" + roomType +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookedRoom that = (BookedRoom) o;
        return Objects.equals(id, that.id) && Objects.equals(booking, that.booking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, booking);
    }
}
