package equinox.blocker.controller;

import equinox.blocker.service.BlockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blocker")
@RequiredArgsConstructor
public class BlockerController {

    private final BlockerService blockerService;

    @PostMapping("/is-fraudulent")
    public boolean isFraudulent() {
        return blockerService.isFraudulent();
    }
}
