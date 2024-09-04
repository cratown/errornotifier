package pl.dreamcode.errornotifier.errors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ApiErrorFormTests {
    
    @Test
    void shouldBuildEntityAndUseAllSettersAndGetters() {
        String projectName = "Project name";
        String body = "Test body";
        ApiErrorForm error = new ApiErrorForm();
        error.setProjectName(projectName);
        error.setBody(body);
        assertEquals(projectName, error.getProjectName());
        assertEquals(body, error.getBody());
    }
}
