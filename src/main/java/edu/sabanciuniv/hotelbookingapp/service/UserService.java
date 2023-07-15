package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.User;
import edu.sabanciuniv.hotelbookingapp.model.dto.ResetPasswordDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.ResetUsernameDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(UserRegistrationDTO registrationDTO);

    Optional<User> findByUsername(String username);

    List<UserDTO> findAllUsers();

    User resetPassword(ResetPasswordDTO resetPasswordDTO);

    User resetUsername(ResetUsernameDTO resetUsernameDTO);

}
