package equinox.front.service;

import equinox.front.client.AccountServiceClient;
import equinox.front.model.dto.UserDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinclaveUserDetailsService implements UserDetailsService {
    private final AccountServiceClient accountServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDto user = accountServiceClient.getUser(username);

            return User.withUsername(username)
                    .password(user.getPassword())
                    .authorities(new GrantedAuthority[0])
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .accountLocked(false)
                    .disabled(false)
                    .build();

        } catch (FeignException.FeignClientException ex) {
            throw new UsernameNotFoundException("Account service unavailable", ex);
        }
    }
}
