package pl.dreamcode.errornotifier.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ErrorRepositoryTests {

    @Autowired
    private ErrorRepository errorRepository;

    private Error testError;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testError = new Error();
        testError.setProjectName("error_notifier");
        testError.setBody("Error body");
        errorRepository.save(testError);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        errorRepository.delete(testError);
    }

    @Test
    void getErrorById() {
        Error savedError = errorRepository.findById(testError.getId()).orElse(null);
        assertNotNull(savedError);
        assertEquals(testError.getProjectName(), savedError.getProjectName());
        assertEquals(testError.getBody(), savedError.getBody());
    }

    @Test
    void shouldSendNotification() {
        Boolean shouldSend = errorRepository.shouldSendNotification(testError.getProjectName(), Instant.now().minus(Duration.ofMinutes(10)));
        assertTrue(shouldSend);
    }

    @Test
    void shouldNotSendNotificationForOtherProjectName() {
        Boolean shouldSend = errorRepository.shouldSendNotification("Other project name", Instant.now().minus(Duration.ofMinutes(10)));
        assertFalse(shouldSend);
    }

    @Test
    void shouldNotSendNotificationForMultipleRecords() {
        setUp();
        Boolean shouldSend = errorRepository.shouldSendNotification("Other project name", Instant.now().minus(Duration.ofMinutes(10)));
        assertFalse(shouldSend);
    }
}
