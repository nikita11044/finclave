package equinox.blocker.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class BlockerService {
    private static final int BLOCK_PROBABILITY = 1;

    public boolean isFraudulent() {
        int chance = ThreadLocalRandom.current().nextInt(100);
        return chance < BLOCK_PROBABILITY;
    }
}
