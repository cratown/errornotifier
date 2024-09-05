package pl.dreamcode.errornotifier.users;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dreamcode.errornotifier.notifications.EmailService;

@ExtendWith(MockitoExtension.class)
public class RegistrationListenerTests {
    @InjectMocks
    private RegistrationListener listener;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private EmailService emailService;

    @Test
    void shouldCreateAndSendEmailWithConfirmationLink() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setEmail("test@example.com");
        OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(user);
        listener.onApplicationEvent(event);

        verify(registrationService).createVerificationToken(eq(user), any(String.class));
        verify(emailService).sendEmail(eq(user.getEmail()), any(String.class), any(String.class));
    }
}
