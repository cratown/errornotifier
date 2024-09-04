package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTests {
    @InjectMocks
    private CustomUserDetailsService service;

    @Mock
    private UserRepository userRepository;
    

    @Test
    void shouldBuildUserDetailsFromUserEntity() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        given(userRepository.findByEmail(user.getEmail())).willReturn(user);

        UserDetails userDetails = service.loadUserByUsername(user.getEmail());
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenNoUserFound() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        given(userRepository.findByEmail(user.getEmail())).willReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(user.getEmail()));
    }
}
