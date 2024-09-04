package pl.dreamcode.errornotifier.users.exception;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
