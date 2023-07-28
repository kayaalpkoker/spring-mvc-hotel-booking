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

    @Query("SELECT MIN(a.availableRooms) FROM Availability a WHERE a.room.id = :roomId AND a.date BETWEEN :checkinDate AND :checkoutDate")
    Optional<Integer> getMinAvailableRooms(@Param("roomId") Long roomId, @Param("checkinDate") LocalDate checkinDate, @Param("checkoutDate") LocalDate checkoutDate);

}
