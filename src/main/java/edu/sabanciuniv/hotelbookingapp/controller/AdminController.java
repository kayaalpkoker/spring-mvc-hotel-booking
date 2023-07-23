package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.exception.UsernameAlreadyExistsException;
import edu.sabanciuniv.hotelbookingapp.model.dto.UserDTO;
import edu.sabanciuniv.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDTO> userDTOList = userService.findAllUsers();
        model.addAttribute("users", userDTOList);
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.findUserById(id);
        model.addAttribute("user", userDTO);
        return "admin/users-edit";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, @Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/users-edit";
        }
        try {
            userService.updateUser(userDTO);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", "user.exists", "Username is already registered!");
            return "admin/users-edit";
        }

        redirectAttributes.addFlashAttribute("updatedUserId", userDTO.getId());
        return "redirect:/admin/users?success";
    }

    // Workaround for @DeleteMapping via post method
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/hotels")
    public String listHotels() {
        return "admin/hotels";
    }

    @GetMapping("/bookings")
    public String listBookings() {
        return "admin/bookings";
    }

}
