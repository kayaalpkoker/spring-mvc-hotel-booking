package edu.sabanciuniv.hotelbookingapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id;

    @NotBlank(message = "Address line cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9 .,:-]*$", message = "Address line can only contain letters, numbers, and some special characters (. , : - )")
    private String addressLine;

    @NotBlank(message = "City cannot be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "City must only contain letters")
    private String city;

    @NotBlank(message = "Country cannot be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$", message = "Country must only contain letters")
    private String country;
}
