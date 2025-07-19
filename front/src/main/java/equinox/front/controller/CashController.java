package equinox.front.controller;

import equinox.front.client.CashServiceClient;
import equinox.front.model.dto.CashOperationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class CashController {
    private final CashServiceClient cashServiceClient;

    @PostMapping("/user/cash")
    public String processCashOperation(
            @ModelAttribute CashOperationDto dto,
            RedirectAttributes model,
            Authentication authentication
    ) {
        var response = cashServiceClient.processCashOperation(authentication.getName(), dto);
        if (response.isWithError()) {
            model.addFlashAttribute("cashErrors", response.getErrors());
        }
        return "redirect:/";
    }
}
