package edu.sabanciuniv.hotelbookingapp.repository;

import edu.sabanciuniv.hotelbookingapp.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findByName(String name);
    List<Hotel> findAllByHotelManager_Id(Long id);
    Optional<Hotel> findByIdAndHotelManager_Id(Long id, Long managerId);
}
