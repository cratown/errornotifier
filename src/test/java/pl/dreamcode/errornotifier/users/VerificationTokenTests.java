package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class VerificationTokenTests {

    @Test
    void shouldBuildEntityAndUseAllSettersAndGetters() {
        String token = "Token";
        User user = new User();
        Instant now = Instant.now();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setCreatedAt(now);
        assertEquals(user, verificationToken.getUser());
        assertEquals(token, verificationToken.getToken());
        assertEquals(now.plusSeconds(24*60*60), verificationToken.getExpiryDateTime()); // h * m * s
    }
}
