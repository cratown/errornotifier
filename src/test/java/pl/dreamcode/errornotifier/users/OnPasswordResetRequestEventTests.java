package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OnPasswordResetRequestEventTests {
@Test
    void shouldBuildEventAndUseAllSettersAndGetters() {
        String email = "test@example.com";
        OnPasswordResetRequestEvent event = new OnPasswordResetRequestEvent(email);
        
        assertEquals(email, event.getEmail());
        String otherEmail = "test1@example.com";
        event.setEmail(otherEmail);
        assertEquals(otherEmail, event.getEmail());
        
    }
}
