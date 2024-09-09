package pl.dreamcode.errornotifier.users;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.dreamcode.errornotifier.users.exception.InvalidTokenException;
import pl.dreamcode.errornotifier.users.exception.TokenExpiredException;

@Service
public class VerificationTokenServiceImp {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public VerificationToken getVerificationToken(String token) throws InvalidTokenException, TokenExpiredException {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new InvalidTokenException();
        }
        int diff = verificationToken.getExpiryDateTime().compareTo(Instant.now());
        if(diff == -1) {
            throw new TokenExpiredException();
        }
        return verificationToken;
    }
}
