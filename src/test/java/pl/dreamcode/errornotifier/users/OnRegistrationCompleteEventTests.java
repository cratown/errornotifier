package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OnRegistrationCompleteEventTests {
    @Test
    void shouldBuildEventAndUseAllSettersAndGetters() {
        Long id = 10l;
        User user = new User();
        user.setId(id);
        OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(user);
        
        assertEquals(user, event.getUser());
        User otherUser = new User();
        otherUser.setId(id+1);
        event.setUser(otherUser);
        assertEquals(otherUser, event.getUser());
        
    }
}
