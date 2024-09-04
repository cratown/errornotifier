package pl.dreamcode.errornotifier.users.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserAlreadyExistExceptionTests {

    @Test
    void shouldInitializeExceptionWithMessage() throws Exception {
        UserAlreadyExistException ex = new UserAlreadyExistException("Message!");

        assertEquals("Message!", ex.getMessage());
        
    }

}
