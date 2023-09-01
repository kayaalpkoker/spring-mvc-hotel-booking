package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @Column(unique = true, nullable = false)
    private String confirmationNumber;

    @CreationTimestamp
    private LocalDateTime bookingDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Hotel hotel;

    @Column(nullable = false)
    private LocalDate checkinDate;

    @Column(nullable = false)
    private LocalDate checkoutDate;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BookedRoom> bookedRooms = new ArrayList<>();

    @OneToOne(mappedBy = "booking")
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        this.confirmationNumber = UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", confirmationNumber='" + confirmationNumber + '\'' +
                ", bookingDate=" + bookingDate +
                ", customer=" + customer +
                ", hotel=" + hotel +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", bookedRooms=" + bookedRooms +
                ", payment=" + payment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(confirmationNumber, booking.confirmationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, confirmationNumber);
    }
}
