package edu.sabanciuniv.hotelbookingapp.controller;

/*@Controller
@RequiredArgsConstructor
public class CredentialResetController {

    private final UserService userService;

    @GetMapping("/reset-username")
    public String showResetUsernameForm(@ModelAttribute("resetUsername") ResetUsernameDTO resetUsernameDTO) {
        return "reset-username";
    }

    @PostMapping("/reset-username/save")
    public String resetUsername(@Valid @ModelAttribute("resetUsername") ResetUsernameDTO resetUsernameDTO, BindingResult result) {
        try {
            userService.resetUsername(resetUsernameDTO);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("newUsername", "username.duplicate", "Username is already taken!");
            return "reset-username";
        }
        return "redirect:/user-dashboard?resetSuccess";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@ModelAttribute("resetPassword") ResetPasswordDTO resetPasswordDTO) {
        return "reset-password";
    }

    @PostMapping("/reset-password/save")
    public String resetUserPassword(@Valid @ModelAttribute("resetPassword") ResetPasswordDTO resetPasswordDTO, BindingResult result) {
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
            result.rejectValue("confirmNewPassword", "password.mismatch", "New passwords do not match!");
            return "reset-password";
        }

        try {
            userService.resetPassword(resetPasswordDTO);
        } catch (IllegalArgumentException e) {
            result.rejectValue("oldPassword", "password.incorrect", "Old password is incorrect!");
            return "reset-password";
        }

        return "redirect:/user-dashboard?resetSuccess";
    }

}

 */
