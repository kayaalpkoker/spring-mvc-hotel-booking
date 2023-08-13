package edu.sabanciuniv.hotelbookingapp.validation.validator;

import edu.sabanciuniv.hotelbookingapp.validation.annotation.CardExpiry;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.YearMonth;

public class CardExpiryValidator implements ConstraintValidator<CardExpiry, String> {

    @Override
    public void initialize(CardExpiry constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !value.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            return false;
        }

        try {
            String[] parts = value.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]); // Assuming 21st century

            YearMonth cardExpiry = YearMonth.of(year, month);
            YearMonth currentMonth = YearMonth.now();

            return cardExpiry.isAfter(currentMonth) || cardExpiry.equals(currentMonth);
        } catch (NumberFormatException | DateTimeException e) {
            return false;
        }
    }
}