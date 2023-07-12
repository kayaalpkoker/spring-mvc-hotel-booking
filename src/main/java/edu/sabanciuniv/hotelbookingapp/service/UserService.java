package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.User;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;

import java.util.Optional;

public interface UserService {

    User save(UserRegistrationDTO registrationDTO);

    Optional<User> findByUsername(String username);


}
