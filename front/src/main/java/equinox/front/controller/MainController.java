package equinox.front.controller;

import equinox.front.client.AccountServiceClient;
import equinox.front.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final AccountServiceClient accountServiceClient;

    @GetMapping()
    public String mainPage(Model model, Authentication authentication) {
        model.addAttribute("login", authentication.getName());
        UserDto user = accountServiceClient.getUser(authentication.getName());
        model.addAttribute("name", user.getName());
        model.addAttribute("birthdate", user.getBirthdate());
        model.addAttribute("accounts", user.getAccounts());
        return "main";
    }
}
