package pl.dreamcode.errornotifier.notifications;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dreamcode.errornotifier.errors.ErrorRepository;
import pl.dreamcode.errornotifier.errors.OnNewErrorEvent;
import pl.dreamcode.errornotifier.errors.Error;

@ExtendWith(MockitoExtension.class)
public class MailNotificationTaskTests {
    @InjectMocks
    private MailNotificationTask service;

    @Mock
    private EmailService emailService;

    @Mock
    private ErrorRepository errorRepository;

    @Test
    void shouldSendMailAboutLastErrorInProject() throws Exception {

        String projectName = "Project name";
        Error projectError = new Error();
        projectError.setProjectName(projectName);
        OnNewErrorEvent event = new OnNewErrorEvent(projectError);
        doNothing().when(emailService).sendEmail(any(String.class), any(String.class), any(String.class));
        given(errorRepository.shouldSendNotification(any(String.class), any(Instant.class))).willReturn(true);

        Error lastError = new Error();
        given(errorRepository.findFirstByProjectNameOrderByIdDesc(any(String.class))).willReturn(lastError);

        service.runNotification(event);

        verify(emailService).sendEmail(any(String.class), any(String.class), any(String.class));
    }

    @Test
    void shouldNotSendMailAboutLastErrorInProjectWhenAlreadySend() throws Exception {

        String projectName = "Project name";
        Error projectError = new Error();
        projectError.setProjectName(projectName);
        OnNewErrorEvent event = new OnNewErrorEvent(projectError);
        given(errorRepository.shouldSendNotification(any(String.class), any(Instant.class))).willReturn(false);

        service.runNotification(event);

        verify(emailService, never()).sendEmail(any(String.class), any(String.class), any(String.class));
    }

    @Test
    void shouldNotSendMailAboutLastErrorInProjectWhenNoLastError() throws Exception {

        String projectName = "Project name";
        Error projectError = new Error();
        projectError.setProjectName(projectName);
        OnNewErrorEvent event = new OnNewErrorEvent(projectError);
        given(errorRepository.shouldSendNotification(any(String.class), any(Instant.class))).willReturn(true);
        given(errorRepository.findFirstByProjectNameOrderByIdDesc(any(String.class))).willReturn(null);
        service.runNotification(event);

        verify(emailService, never()).sendEmail(any(String.class), any(String.class), any(String.class));
    }
}
