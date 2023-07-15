package edu.sabanciuniv.hotelbookingapp.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetUsernameDTO {

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Invalid email address")
    String oldUsername;

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Invalid email address")
    String newUsername;

}
