package pl.dreamcode.errornotifier.users;

import pl.dreamcode.errornotifier.users.exception.InvalidTokenException;
import pl.dreamcode.errornotifier.users.exception.TokenExpiredException;
import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

public interface RegistrationService {
    User registerNewUserAccount(RegistrationForm registrationForm) throws UserAlreadyExistException;

    void createVerificationToken(User user, String token);

    User confirm(String token) throws InvalidTokenException, TokenExpiredException;
}
