package pl.dreamcode.errornotifier.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class ErrorDBServiceTests {
    
    @InjectMocks
    private ErrorDBService errorDBService;

    @Mock
    private ErrorRepository errorRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void shouldCreate() throws Exception {
        Error error = new Error();
        error.setProjectName("error_notifier");
        error.setBody("body on error");
        
        given(errorRepository.save(error)).willReturn(error);

        Error savedError = errorDBService.create(error);

        assertEquals(savedError, error);
        // Check if repository save called
        verify(errorRepository).save(any(Error.class));
        // Check if event added
        verify(eventPublisher).publishEvent(any(OnNewErrorEvent.class));
    }
}
