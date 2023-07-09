package edu.sabanciuniv.hotelbookingapp.initialize;

import edu.sabanciuniv.hotelbookingapp.model.*;
import edu.sabanciuniv.hotelbookingapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
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

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0 && userRepository.count() == 0) {
            log.warn("Initiating data persistence actions");

            Role adminRole = new Role(RoleType.ADMIN);
            Role customerRole = new Role(RoleType.CUSTOMER);
            Role hotelManagerRole = new Role(RoleType.HOTEL_MANAGER);

            roleRepository.save(adminRole);
            roleRepository.save(customerRole);
            roleRepository.save(hotelManagerRole);
            log.warn("Role data saved");

            User user1 = User.builder().username("admin1").password("123...").name("Kaya Alp").lastName("Köker").role(adminRole).build();
            User user2 = User.builder().username("customer1").password("111").name("John").lastName("Doe").role(customerRole).build();
            User user3 = User.builder().username("manager1").password("222").name("Jane").lastName("Doe").role(hotelManagerRole).build();
            User user4 = User.builder().username("customer2").password("333").name("Jack").lastName("Black").role(customerRole).build();
            User user5 = User.builder().username("manager2").password("444").name("Eddie").lastName("Murphy").role(hotelManagerRole).build();

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            Admin admin1 = new Admin();
            Customer c1 = new Customer();
            Customer c2 = new Customer();
            HotelManager hm1 = new HotelManager();
            HotelManager hm2 = new HotelManager();

            admin1.setUser(user1);
            c1.setUser(user2);
            c2.setUser(user4);
            hm1.setUser(user3);
            hm2.setUser(user5);

            adminRepository.save(admin1);
            customerRepository.save(c1);
            customerRepository.save(c2);
            hotelManagerRepository.save(hm1);
            hotelManagerRepository.save(hm2);

            log.warn("All test user data saved");
        }

    }
}
