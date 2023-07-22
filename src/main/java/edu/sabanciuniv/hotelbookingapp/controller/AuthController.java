package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.exception.UsernameAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.RoleType;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserRegistrationDTO;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        registrationDTO.setRoleType(RoleType.CUSTOMER);
        return registerUser(registrationDTO, result, "register-customer", "register/customer");
    }

    @GetMapping("/register/manager")
    public String showManagerRegistrationForm(@ModelAttribute("user") UserRegistrationDTO registrationDTO) {
        return "register-manager";
    }

    @PostMapping("/register/manager")
    public String registerManagerAccount(@Valid @ModelAttribute("user") UserRegistrationDTO registrationDTO, BindingResult result) {
        registrationDTO.setRoleType(RoleType.HOTEL_MANAGER);
        return registerUser(registrationDTO, result, "register-manager", "register/manager");
    }

    private String registerUser(UserRegistrationDTO registrationDTO, BindingResult result, String view, String redirectUrl) {
        if (result.hasErrors()) {
            return view;
        }
        try {
            userService.save(registrationDTO);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", "user.exists", e.getMessage());
            return view;
        }
        return "redirect:/" + redirectUrl + "?success";
    }

}

