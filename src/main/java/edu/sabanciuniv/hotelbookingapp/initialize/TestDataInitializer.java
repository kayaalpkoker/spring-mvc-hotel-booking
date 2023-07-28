package edu.sabanciuniv.hotelbookingapp.initialize;

import edu.sabanciuniv.hotelbookingapp.model.*;
import edu.sabanciuniv.hotelbookingapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final HotelManagerRepository hotelManagerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final HotelRepository hotelRepository;

    @Override
    @Transactional
    public void run(String... args) {

        try {
            log.warn("Checking if test data persistence is required...");

            if (roleRepository.count() == 0 && userRepository.count() == 0) {
                log.info("Initiating test data persistence");

                Role adminRole = new Role(RoleType.ADMIN);
                Role customerRole = new Role(RoleType.CUSTOMER);
                Role hotelManagerRole = new Role(RoleType.HOTEL_MANAGER);

                roleRepository.save(adminRole);
                roleRepository.save(customerRole);
                roleRepository.save(hotelManagerRole);
                log.info("Role data persisted");

                User user1 = User.builder().username("admin@hotel.com").password(passwordEncoder.encode("1")).name("Admin").lastName("Admin").role(adminRole).build();
                User user2 = User.builder().username("customer1@hotel.com").password(passwordEncoder.encode("1")).name("Kaya Alp").lastName("Koker").role(customerRole).build();
                User user3 = User.builder().username("manager1@hotel.com").password(passwordEncoder.encode("1")).name("John").lastName("Doe").role(hotelManagerRole).build();
                User user4 = User.builder().username("manager2@hotel.com").password(passwordEncoder.encode("1")).name("Max").lastName("Mustermann").role(hotelManagerRole).build();

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);

                Admin admin1 = Admin.builder().user(user1).build();
                Customer c1 = Customer.builder().user(user2).build();
                HotelManager hm1 = HotelManager.builder().user(user3).build();
                HotelManager hm2 = HotelManager.builder().user(user4).build();

                adminRepository.save(admin1);
                customerRepository.save(c1);
                hotelManagerRepository.save(hm1);
                hotelManagerRepository.save(hm2);
                log.info("User data persisted");

                Address address1 = Address.builder()
                        .addressLine("Visnezade, Acisu Sokagi No:19")
                        .city("Istanbul")
                        .country("Turkey")
                        .build();

                Address address2 = Address.builder()
                        .addressLine("Unter den Linden 77")
                        .city("Berlin")
                        .country("Germany")
                        .build();

                addressRepository.save(address1);
                addressRepository.save(address2);

                Hotel hotel1 = Hotel.builder()
                        .name("Swissotel The Bosphorus Istanbul")
                        .address(address1)
                        .hotelManager(hm1)
                        .build();

                Hotel hotel2 = Hotel.builder()
                        .name("Hotel Adlon Kempinski Berlin")
                        .address(address2)
                        .hotelManager(hm2)
                        .build();

                Room singleRoom1 = Room.builder()
                        .roomType(RoomType.SINGLE)
                        .pricePerNight(100.0)
                        .roomCount(20)
                        .hotel(hotel1)
                        .build();

                Room doubleRoom1 = Room.builder()
                        .roomType(RoomType.DOUBLE)
                        .pricePerNight(200.0)
                        .roomCount(10)
                        .hotel(hotel1)
                        .build();

                Room singleRoom2 = Room.builder()
                        .roomType(RoomType.SINGLE)
                        .pricePerNight(120.0)
                        .roomCount(25)
                        .hotel(hotel2)
                        .build();

                Room doubleRoom2 = Room.builder()
                        .roomType(RoomType.DOUBLE)
                        .pricePerNight(250.0)
                        .roomCount(15)
                        .hotel(hotel2)
                        .build();

                hotel1.getRooms().addAll(Arrays.asList(singleRoom1,doubleRoom1));
                hotel2.getRooms().addAll(Arrays.asList(singleRoom2,doubleRoom2));

                hotelRepository.save(hotel1);
                hotelRepository.save(hotel2);
                log.info("Hotel test data persisted");
            } else {
                log.info("Test data persistence is not required");
            }
            log.warn("App ready");
        } catch (DataAccessException e) {
            log.error("Exception occurred during data persistence: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception occurred: " + e.getMessage());
        }
    }
}
