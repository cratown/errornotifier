package pl.dreamcode.errornotifier.users.exception;

public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
        super("Invalid token");
    }
}
