package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.model.RoleType;
import edu.sabanciuniv.hotelbookingapp.model.User;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register/customer")
    public String showCustomerRegistrationForm(@ModelAttribute("user") UserRegistrationDTO registrationDTO) {
        return "register-customer";
    }

    @PostMapping("/register/customer")
    public String registerCustomerAccount(@Valid @ModelAttribute("user") UserRegistrationDTO registrationDTO, BindingResult result) {
        Optional<User> existingUser = userService.findByUsername(registrationDTO.getUsername());
        if (existingUser.isPresent()) {
            result.rejectValue("username", "user.exists", "Username is already registered!");
            return "register-customer";
        }
        if (result.hasErrors()) {
            return "register-customer";
        }
        registrationDTO.setRoleType(RoleType.CUSTOMER);
        userService.save(registrationDTO);
        return "redirect:/register/customer?success";
    }

    @GetMapping("/register/manager")
    public String showManagerRegistrationForm(@ModelAttribute("user") UserRegistrationDTO registrationDTO) {
        return "register-manager";
    }

    @PostMapping("/register/manager")
    public String registerManagerAccount(@Valid @ModelAttribute("user") UserRegistrationDTO registrationDTO, BindingResult result) {
        Optional<User> existingUser = userService.findByUsername(registrationDTO.getUsername());
        if (existingUser.isPresent()) {
            result.rejectValue("username", "user.exists", "Username is already registered!");
            return "register-manager";
        }
        if (result.hasErrors()) {
            return "register-manager";
        }
        registrationDTO.setRoleType(RoleType.HOTEL_MANAGER);
        userService.save(registrationDTO);
        return "redirect:/register/manager?success";
    }

}

