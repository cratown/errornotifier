package pl.dreamcode.errornotifier.users;

import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

public interface RegistrationService {
    User registerNewUserAccount(RegistrationForm registrationForm) throws UserAlreadyExistException;
}
