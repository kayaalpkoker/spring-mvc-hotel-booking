package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String showRegistrationForm(@ModelAttribute("user") UserRegistrationDTO registrationDTO) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDTO registrationDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }
        try {
            userService.save(registrationDTO);
        } catch (DuplicateKeyException e) {
            result.rejectValue("username", "user.exists", "This username is already registered!");
            return "registration";
        }
        // redirect link needs to be changed
        return "redirect:/registration?success";
    }

}
