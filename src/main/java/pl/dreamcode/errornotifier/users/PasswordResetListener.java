package pl.dreamcode.errornotifier.users;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.dreamcode.errornotifier.notifications.EmailService;

@Component
public class PasswordResetListener {

    @Autowired
    private PasswordResetService service;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EmailService emailService;

    @Value("${application.url}")
    private String applicationUrl;

    @Async
    @EventListener
    public void handleUserRegistrationEvent(OnPasswordResetRequestEvent event) {
        User user = service.findUserByEmail(event.getEmail());
        if(user == null) {
            return;
        }
        String token = UUID.randomUUID().toString();
        registrationService.createVerificationToken(user, token);

        emailService.sendEmail(user.getEmail(), "Reset password", applicationUrl + "/changePassword?token=" + token);
    }
}
