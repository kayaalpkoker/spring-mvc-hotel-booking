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

    @Override
    @Transactional
    public void run(String... args) {

        try {
            log.warn("Checking if test data persistence is required...");

            if (roleRepository.count() == 0 && userRepository.count() == 0) {
                log.warn("Initiating test data persistence");

                Role adminRole = new Role(RoleType.ADMIN);
                Role customerRole = new Role(RoleType.CUSTOMER);
                Role hotelManagerRole = new Role(RoleType.HOTEL_MANAGER);

                roleRepository.save(adminRole);
                roleRepository.save(customerRole);
                roleRepository.save(hotelManagerRole);
                log.warn("Role data persisted");

                User user1 = User.builder().username("admin@admin.com").password(passwordEncoder.encode("111")).name("Admin").lastName("Admin").role(adminRole).build();
                User user2 = User.builder().username("customer1@customer.com").password(passwordEncoder.encode("222")).name("Kaya Alp").lastName("Koker").role(customerRole).build();
                User user3 = User.builder().username("manager1@manager.com").password(passwordEncoder.encode("333")).name("John").lastName("Doe").role(hotelManagerRole).build();
                User user4 = User.builder().username("manager2@manager.com").password(passwordEncoder.encode("444")).name("Max").lastName("Mustermann").role(hotelManagerRole).build();

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
                log.warn("User data persisted");

                log.warn("App ready");
            } else {
                log.warn("App ready - Test data persistence is not required");
            }
        } catch (DataAccessException e) {
            log.error("Exception occurred during data persistence: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception occurred: " + e.getMessage());
        }
    }
}
