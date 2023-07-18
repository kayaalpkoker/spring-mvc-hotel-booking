package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.exception.UsernameAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.*;
import edu.sabanciuniv.hotelbookingapp.model.dto.ResetPasswordDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.ResetUsernameDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.repository.CustomerRepository;
import edu.sabanciuniv.hotelbookingapp.repository.HotelManagerRepository;
import edu.sabanciuniv.hotelbookingapp.repository.RoleRepository;
import edu.sabanciuniv.hotelbookingapp.repository.UserRepository;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final HotelManagerRepository hotelManagerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(UserRegistrationDTO registrationDTO) {

        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(registrationDTO.getUsername()));
        if (existingUser.isPresent()) {throw new UsernameAlreadyExistsException("This username is already registered!");}

        Role userRole = roleRepository.findByRoleType(registrationDTO.getRoleType());

        User user = User.builder()
                .username(registrationDTO.getUsername().trim())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .name(StringUtils.capitalize(registrationDTO.getName().trim()))
                .lastName(StringUtils.capitalize(registrationDTO.getLastName().trim()))
                .role(userRole)
                .build();

        if (RoleType.CUSTOMER.equals(registrationDTO.getRoleType())) {
            Customer customer = Customer.builder().user(user).build();
            customerRepository.save(customer);
        } else if (RoleType.HOTEL_MANAGER.equals(registrationDTO.getRoleType())) {
            HotelManager hotelManager = HotelManager.builder().user(user).build();
            hotelManagerRepository.save(hotelManager);
        }

        return userRepository.save(user);
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

    @Override
    public User resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        User user = userRepository.findByUsername(loggedInUsername);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(resetPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        return userRepository.save(user);
    }

    @Override
    public User resetUsername(ResetUsernameDTO resetUsernameDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        User user = userRepository.findByUsername(loggedInUsername);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<User> userNewUsername = Optional.ofNullable(userRepository.findByUsername(resetUsernameDTO.getNewUsername()));
        if (userNewUsername.isPresent()) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }

        user.setUsername(resetUsernameDTO.getNewUsername());

        return userRepository.save(user);
    }

}
