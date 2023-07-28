package edu.sabanciuniv.hotelbookingapp.repository;

import edu.sabanciuniv.hotelbookingapp.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findByName(String name);

    List<Hotel> findAllByHotelManager_Id(Long id);

    Optional<Hotel> findByIdAndHotelManager_Id(Long id, Long managerId);

    @Query("SELECT h FROM Hotel h WHERE h.address.city = :city")
    List<Hotel> findByCity(@Param("city") String city);

    // A more adaptive query for various SQL dialects
    @Query("SELECT h FROM Hotel h WHERE h.address.city = :city AND NOT EXISTS (" +
            "SELECT 1 FROM Room r WHERE r.hotel = h AND (" +
            "EXISTS (SELECT 1 FROM Availability a WHERE a.room = r AND a.date BETWEEN :checkinDate AND :checkoutDate AND a.availableRooms = 0) OR " +
            "(SELECT COUNT(*) FROM Availability a WHERE a.room = r AND a.date BETWEEN :checkinDate AND :checkoutDate) < :days" +
            "))")
    List<Hotel> findAvailableHotelsByCityAndDate(String city, LocalDate checkinDate, LocalDate checkoutDate, long days);

    /*
    // This query relies on the FUNCTION('DATEDIFF', :checkoutDate, :checkinDate) construct,
    // which is not universally supported. Supported for MySQL
    @Query("SELECT h FROM Hotel h WHERE h.address.city = :city AND NOT EXISTS (" +
            "SELECT 1 FROM Room r WHERE r.hotel = h AND (" +
            "EXISTS (SELECT 1 FROM Availability a WHERE a.room = r AND a.date BETWEEN :checkinDate AND :checkoutDate AND a.availableRooms = 0) OR " +
            "(SELECT COUNT(*) FROM Availability a WHERE a.room = r AND a.date BETWEEN :checkinDate AND :checkoutDate) < FUNCTION('DATEDIFF', :checkoutDate, :checkinDate) + 1" +
            "))")
    List<Hotel> findAvailableHotelsByCityAndDate(String city, LocalDate checkinDate, LocalDate checkoutDate);
     */
}
