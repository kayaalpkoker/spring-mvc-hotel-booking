package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.exception.UsernameAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MyAccountController {

    private final UserService userService;

    // Customer actions
    @GetMapping("/customer/account")
    public String showCustomerAccount(Model model){
        addLoggedInUserDataToModel(model);
        return "customer/account";
    }

    @GetMapping("/customer/account/edit")
    public String showCustomerEditForm(Model model){
        addLoggedInUserDataToModel(model);
        return "customer/account-edit";
    }

    @PostMapping("/customer/account/edit")
    public String editCustomerAccount(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "customer/account-edit";
        }
        try {
            userService.updateLoggedInUser(userDTO);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", "user.exists", "Username is already registered!");
            return "customer/account-edit";
        }
        return "redirect:/customer/account?success";
    }

    // Hotel Manager actions
    @GetMapping("/manager/account")
    public String showHotelManagerAccount(Model model){
        addLoggedInUserDataToModel(model);
        return "hotelmanager/account";
    }

    @GetMapping("/manager/account/edit")
    public String showHotelManagerEditForm(Model model){
        addLoggedInUserDataToModel(model);
        return "hotelmanager/account-edit";
    }

    @PostMapping("/manager/account/edit")
    public String editHotelManagerAccount(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "hotelmanager/account-edit";
        }
        try {
            userService.updateLoggedInUser(userDTO);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", "user.exists", "Username is already registered!");
            return "hotelmanager/account-edit";
        }
        return "redirect:/manager/account?success";
    }

    private void addLoggedInUserDataToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.findUserDTOByUsername(username);
        model.addAttribute("user", userDTO);
    }

}
