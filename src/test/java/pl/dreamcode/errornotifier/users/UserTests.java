package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class UserTests {
    @Test
    void shouldBuildEntityAndUseAllSettersAndGetters() {
        Long id = 10l;
        Instant createdAt = Instant.now();
        String email = "test@example.com";
        String password = "secretPassword";
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(createdAt);
        user.setEnabled(true);
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(createdAt, user.getUpdatedAt());
        assertTrue(user.getEnabled());
    }
}
