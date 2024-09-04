package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetailsTests {
    @Test
    void shouldBuildEntityAndUseAllGetters() {
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

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        assertEquals(id, customUserDetails.getId());
        assertEquals(email, customUserDetails.getUsername());
        assertEquals(password, customUserDetails.getPassword());
        assertEquals(Arrays.asList(new SimpleGrantedAuthority("admin")), customUserDetails.getAuthorities());
        assertTrue(customUserDetails.isEnabled());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isEnabled());
    }
}
