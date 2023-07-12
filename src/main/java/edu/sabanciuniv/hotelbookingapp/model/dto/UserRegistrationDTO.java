package edu.sabanciuniv.hotelbookingapp.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDTO {

    @NotBlank(message = "Username is required")
    @Email(message = "Invalid email address")
    String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 to 20 characters")
    String password;

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Last name is required")
    String lastName;

}
