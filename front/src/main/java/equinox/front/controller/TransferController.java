package equinox.front.controller;

import equinox.front.client.TransferServiceClient;
import equinox.front.model.dto.TransferDto;
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
public class TransferController {
    private final TransferServiceClient transferServiceClient;

    @PostMapping("/user/transfer")
    public String processTransferOperation(
            @ModelAttribute TransferDto dto,
            RedirectAttributes model,
            Authentication authentication
    ) {
        var response = transferServiceClient.processTransferOperation(authentication.getName(), dto);
        if (response.isWithError()) {
            model.addFlashAttribute("transferErrors", response.getErrors());
        }
        return "redirect:/";
    }
}
