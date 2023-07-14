package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.Customer;
import edu.sabanciuniv.hotelbookingapp.model.Role;
import edu.sabanciuniv.hotelbookingapp.model.RoleType;
import edu.sabanciuniv.hotelbookingapp.model.User;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.repository.CustomerRepository;
import edu.sabanciuniv.hotelbookingapp.repository.RoleRepository;
import edu.sabanciuniv.hotelbookingapp.repository.UserRepository;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(UserRegistrationDTO registrationDTO) {
        /*
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(registrationDTO.getUsername()));

        if (existingUser.isPresent()) {
            throw new DuplicateKeyException("This username is already registered!");
        }
         */

        Role customerRole = roleRepository.findByRoleType(RoleType.CUSTOMER);

        User user = User.builder()
                .username(registrationDTO.getUsername().trim())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .name(StringUtils.capitalize(registrationDTO.getName().trim()))
                .lastName(StringUtils.capitalize(registrationDTO.getLastName().trim()))
                .role(customerRole)
                .build();

        Customer customer = Customer.builder().user(user).build();
        customerRepository.save(customer);

        return userRepository.save(user);

        // TODO: 12.07.2023 // Handle Hotel Manager registration logic

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> userList = userRepository.findAll();
        // TODO: 14.07.2023 // User to DTO mapping 
        return null;
    }

}
