package pl.dreamcode.errornotifier.users;

public interface PasswordResetService {

    User findUserByEmail(String email);

    User findUserByToken(String token);

    User updateUserPassword(String token, PasswordChangeForm passwordChangeForm);

}
