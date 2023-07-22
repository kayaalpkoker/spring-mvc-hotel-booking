package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.User;
import edu.sabanciuniv.hotelbookingapp.model.dto.ResetPasswordDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(UserRegistrationDTO registrationDTO);

    // For registration
    Optional<User> findUserByUsername(String username);

    UserDTO findUserDTOByUsername(String username);

    UserDTO findUserById(Long id);

    List<UserDTO> findAllUsers();

    void updateUser(UserDTO userDTO);

    void updateLoggedInUser(UserDTO userDTO);

    void deleteUserById(Long id);

    User resetPassword(ResetPasswordDTO resetPasswordDTO);

}
