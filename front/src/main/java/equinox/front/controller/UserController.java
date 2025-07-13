package equinox.front.controller;

import equinox.front.model.dto.PasswordUpdateDto;
import equinox.front.model.dto.UserDto;
import equinox.front.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String createUser(
            @ModelAttribute UserDto dto,
            Model model,
            HttpServletRequest request
    ) {
        var response = userService.createUser(dto);

        if (response.isWithError()) {
            model.addAttribute("errors", response.getErrors());
            return "signup";
        } else {
            setModelAttributes(dto, model);
            userService.authenticate(dto, request);
            return "redirect:/";
        }
    }

    @PostMapping("/user/updateUser")
    public String updateUser(
            Authentication authentication,
            @ModelAttribute UserDto dto,
            Model model
    ) {
        var login = authentication.getName();
        var response = userService.updateUser(login, dto);

        if (response.isWithError()) {
            model.addAttribute("userAccountsErrors", response.getErrors());
            return "main";
        }

        var user = userService.getUser(login);
        setModelAttributes(user, model);
        return "main";
    }

    @PostMapping("/user/updatePassword")
    public String updatePassword(
            Authentication authentication,
            @ModelAttribute PasswordUpdateDto dto,
            Model model
    ) {
        var login = authentication.getName();
        var response = userService.updatePassword(login, dto);

        if (response.isWithError()) {
            model.addAttribute("passwordErrors", response.getErrors());
            return "main";
        }

        var user = userService.getUser(login);
        setModelAttributes(user, model);
        return "main";
    }

    private void setModelAttributes(UserDto dto, Model model) {
        model.addAttribute("login", dto.getLogin());
        model.addAttribute("name", dto.getName());
        model.addAttribute("birthdate", dto.getBirthdate());
        model.addAttribute("accounts", dto.getAccounts());
    }
}
