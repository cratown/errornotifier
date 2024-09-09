package pl.dreamcode.errornotifier.users;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dreamcode.errornotifier.notifications.EmailService;

@ExtendWith(MockitoExtension.class)
public class PasswordResetListenerTests {

    @InjectMocks
    private PasswordResetListener listener;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private EmailService emailService;

    @Test
    void shouldCreateAndSendEmailWithResetPasswordLink() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@example.com");

        OnPasswordResetRequestEvent event = new OnPasswordResetRequestEvent(user.getEmail());

        given(passwordResetService.findUserByEmail(user.getEmail())).willReturn(user);

        listener.handleUserRegistrationEvent(event);

        verify(registrationService).createVerificationToken(eq(user), any(String.class));
        verify(emailService).sendEmail(eq(user.getEmail()), any(String.class), any(String.class));
    }

    @Test
    void shouldNotCreateTokenWhenWrongEmail() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@example.com");

        OnPasswordResetRequestEvent event = new OnPasswordResetRequestEvent(user.getEmail());

        given(passwordResetService.findUserByEmail(user.getEmail())).willReturn(null);

        listener.handleUserRegistrationEvent(event);

        verify(registrationService, never()).createVerificationToken(eq(user), any(String.class));
        verify(emailService, never()).sendEmail(eq(user.getEmail()), any(String.class), any(String.class));
    }
}
