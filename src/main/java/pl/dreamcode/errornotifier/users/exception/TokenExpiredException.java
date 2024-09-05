package pl.dreamcode.errornotifier.users.exception;

public class TokenExpiredException extends Exception {
    public TokenExpiredException() {
        super("Token expired");
    }
}
