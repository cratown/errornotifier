package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.dreamcode.errornotifier.users.exception.InvalidTokenException;
import pl.dreamcode.errornotifier.users.exception.TokenExpiredException;
import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImpTests {
    @InjectMocks
    private RegistrationServiceImp service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Test
    void shouldRegisterUser() throws Exception {
        String email = "test@example.com";
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail(email);
        registrationForm.setPassword("secretPassword");

        User user = new User();
        user.setId(1l);
        user.setEmail(email);

        given(userRepository.save(any(User.class))).willReturn(user);

        User savedUser = service.registerNewUserAccount(registrationForm);
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserWithSameEmailExists() throws Exception {
        String email = "test@example.com";
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail(email);
        registrationForm.setPassword("secretPassword");

        User user = new User();
        user.setId(1l);
        user.setEmail(email);

        given(userRepository.findByEmail(email)).willReturn(user);

        assertThrows(UserAlreadyExistException.class, () -> service.registerNewUserAccount(registrationForm));
    }

    @Test
    void shouldCreateVerificationToken() throws Exception {
        String token = UUID.randomUUID().toString();
        String email = "test@example.com";
        User user = new User();
        user.setId(1l);
        user.setEmail(email);

        given(tokenRepository.save(any(VerificationToken.class))).willReturn(any(VerificationToken.class));

        service.createVerificationToken(user, token);

        verify(tokenRepository).save(any(VerificationToken.class));
    }

    @Test
    void shouldConfirmUserWhenCorrectToken() throws Exception {
        String token = UUID.randomUUID().toString();
        String email = "test@example.com";
        User user = new User();
        user.setId(1l);
        user.setEmail(email);
        VerificationToken myToken = new VerificationToken(token, user);
        myToken.setCreatedAt(Instant.now().minusSeconds(24*60*60)); // In last second
        given(tokenRepository.findByToken(token)).willReturn(myToken);

        service.confirm(token);
        verify(userRepository).save(user);
        verify(tokenRepository).delete(myToken);
    }

    @Test
    void shouldThrowExceptionWhenNoToken() throws Exception {
        String token = UUID.randomUUID().toString();
        String email = "test@example.com";
        User user = new User();
        user.setId(1l);
        user.setEmail(email);
        given(tokenRepository.findByToken(token)).willReturn(null);

        assertThrows(InvalidTokenException.class, () -> service.confirm(token));
    }

    @Test
    void shouldThrowExceptionWhenTokenExpired() throws Exception {
        String token = UUID.randomUUID().toString();
        String email = "test@example.com";
        User user = new User();
        user.setId(1l);
        user.setEmail(email);
        VerificationToken myToken = new VerificationToken(token, user);
        myToken.setCreatedAt(Instant.now().minusSeconds(24*60*60+1)); // Day and 1 second ago
        given(tokenRepository.findByToken(token)).willReturn(myToken);

        assertThrows(TokenExpiredException.class, () -> service.confirm(token));
    }
}
