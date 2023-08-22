package edu.sabanciuniv.hotelbookingapp.repository;

import edu.sabanciuniv.hotelbookingapp.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    // Find max amount of available rooms for the least available day throughout the booking range
    @Query("SELECT MIN(COALESCE(a.availableRooms, r.roomCount)) FROM Room r LEFT JOIN Availability a ON a.room.id = r.id AND a.date BETWEEN :checkinDate AND :checkoutDate WHERE r.id = :roomId")
    Optional<Integer> getMinAvailableRooms(@Param("roomId") Long roomId, @Param("checkinDate") LocalDate checkinDate, @Param("checkoutDate") LocalDate checkoutDate);

    Optional<Availability> findByRoomIdAndDate(Long roomId, LocalDate date);

}
