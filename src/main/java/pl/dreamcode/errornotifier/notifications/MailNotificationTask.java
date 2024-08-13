package pl.dreamcode.errornotifier.notifications;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.dreamcode.errornotifier.errors.ErrorRepository;
import pl.dreamcode.errornotifier.errors.Error;

@Component
public class MailNotificationTask implements NotificationTask{

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private EmailService emailService;

    @Async
    @Override
    public void runNotification(String projectName) {
        Boolean shouldSend = errorRepository.shouldSendNotification(projectName, Instant.now().minus(Duration.ofMinutes(10)));
        System.out.println("Async task: " + Thread.currentThread().getName() + "Should send: " + (shouldSend ? "TRUE" : "FALSE"));
        // Return if shouldn't send mail
        if(!shouldSend) { return; }

        Error lastError = errorRepository.findFirstByProjectNameOrderByIdDesc(projectName);
        // Return if no last error.
        if(lastError == null) {return; }

        emailService.sendEmail("luke@dreamcode.pl", "Error in project: "+projectName, String.format("""
            We are detected error in project: %s.
            Content of error:
            %s 
            """, projectName, lastError.getBody()));
    }

}
