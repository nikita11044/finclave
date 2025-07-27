package equinox.account;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@TestConfiguration
public class OAuthBaseTestConfig {

    @Bean
    @Primary
    public OAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager() {
        return Mockito.mock(OAuth2AuthorizedClientManager.class);
    }

    @Bean
    @Primary
    public ClientRegistrationRepository reactiveClientRegistrationRepository() {
        return Mockito.mock(ClientRegistrationRepository.class);
    }

    @Bean
    @Primary
    public OAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService() {
        return Mockito.mock(OAuth2AuthorizedClientService.class);
    }
}
