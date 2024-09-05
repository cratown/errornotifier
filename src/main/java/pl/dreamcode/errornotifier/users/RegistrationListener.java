package pl.dreamcode.errornotifier.users;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pl.dreamcode.errornotifier.notifications.EmailService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private RegistrationService service;

    @Autowired
    private EmailService emailService;

    @Value("${application.url}")
    private String applicationUrl;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);
        emailService.sendEmail(user.getEmail(), "Registration Confirmation", applicationUrl + "/regitrationConfirm?token=" + token);
    }

}
