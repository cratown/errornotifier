package pl.dreamcode.errornotifier.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.dreamcode.errornotifier.users.exception.InvalidTokenException;
import pl.dreamcode.errornotifier.users.exception.TokenExpiredException;

@Service
public class PasswordResetServiceImp extends VerificationTokenServiceImp implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByToken(String token) {
        VerificationToken verificationToken;
        try {
            verificationToken = getVerificationToken(token);
        } catch (InvalidTokenException | TokenExpiredException e) {
            return null;
        }
        return verificationToken.getUser();
    }

    @Override
    public User updateUserPassword(String token, PasswordChangeForm passwordChangeForm) {
        VerificationToken verificationToken;
        try {
            verificationToken = getVerificationToken(token);
        } catch (InvalidTokenException | TokenExpiredException e) {
            return null;
        }
        User user = verificationToken.getUser();
        user.setPassword(passwordChangeForm.getPassword());
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        return user;
    }
}
