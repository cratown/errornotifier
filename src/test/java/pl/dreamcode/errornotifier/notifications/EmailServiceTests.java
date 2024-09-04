package pl.dreamcode.errornotifier.notifications;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {
    @InjectMocks
    private EmailService service;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void shouldSendMail() throws Exception {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        service.sendEmail("test@example.com", "Test subject", "Body of mail message.");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
