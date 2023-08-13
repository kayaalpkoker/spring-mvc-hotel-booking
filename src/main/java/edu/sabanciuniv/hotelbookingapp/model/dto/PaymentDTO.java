package edu.sabanciuniv.hotelbookingapp.model.dto;

import edu.sabanciuniv.hotelbookingapp.validation.annotation.CardExpiry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    @NotBlank(message = "Cardholder name cannot be empty")
    @Size(min = 2, max = 50, message = "Cardholder name should be between 2 and 50 characters")
    private String cardholderName;

    @CreditCardNumber(message = "Invalid credit card number")
    private String cardNumber;

    @CardExpiry
    private String expirationDate;

    @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits")
    private String cvv;
}
