package pl.dreamcode.errornotifier.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class ErrorTests {
    @Test
    void shouldBuildEntityAndUseAllSettersAndGetters() {
        Long id = 10l;
        Instant createdAt = Instant.now();
        String projectName = "Project name";
        String body = "Test body";
        Error error = new Error();
        error.setId(id);
        error.setProjectName(projectName);
        error.setBody(body);
        error.setCreatedAt(createdAt);
        assertEquals(id, error.getId());
        assertEquals(projectName, error.getProjectName());
        assertEquals(body, error.getBody());
        assertEquals(createdAt, error.getCreatedAt());
    }
}
