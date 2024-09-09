package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceImpTests {
    @InjectMocks
    private PasswordResetServiceImp service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Test
    void shouldFindUserByEmail() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);

        given(userRepository.findByEmail(user.getEmail())).willReturn(user);

        assertEquals(user, service.findUserByEmail(user.getEmail()));
    }

    @Test
    void shouldFindUserByToken() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setCreatedAt(Instant.now().minusSeconds(24*60*60));
        given(tokenRepository.findByToken(token)).willReturn(verificationToken);

        assertEquals(user, service.findUserByToken(token));
    }

    @Test
    void shouldReturnNullOnFindUserByTokenWhenWrongToken() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        String token = UUID.randomUUID().toString();
        given(tokenRepository.findByToken(token)).willReturn(null);

        assertNull(service.findUserByToken(token));
    }

    @Test
    void shouldUpdateUserPassword() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        String token = UUID.randomUUID().toString();
        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();
        passwordChangeForm.setPassword("SuperSecurePassword");
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setCreatedAt(Instant.now().minusSeconds(24*60*60));
        given(tokenRepository.findByToken(token)).willReturn(verificationToken);
        service.updateUserPassword(token, passwordChangeForm);
        verify(userRepository).save(any(User.class));
        verify(tokenRepository).delete(verificationToken);
    }

    @Test
    void shouldReturnNullOnUpdateUserPasswordWhenWrongToken() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1l);
        String token = UUID.randomUUID().toString();
        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();
        passwordChangeForm.setPassword("SuperSecurePassword");
        given(tokenRepository.findByToken(token)).willReturn(null);

        assertNull(service.updateUserPassword(token, passwordChangeForm));
        verify(userRepository, never()).save(any(User.class));
        verify(tokenRepository, never()).delete(any(VerificationToken.class));
    }
}
