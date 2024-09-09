package pl.dreamcode.errornotifier.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.dreamcode.errornotifier.users.exception.InvalidTokenException;
import pl.dreamcode.errornotifier.users.exception.TokenExpiredException;
import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

@Service
public class RegistrationServiceImp extends VerificationTokenServiceImp implements RegistrationService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    @Override
    public User registerNewUserAccount(RegistrationForm registrationForm) throws UserAlreadyExistException {
        if (emailExists(registrationForm.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                + registrationForm.getEmail());
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setEmail(registrationForm.getEmail());
        user.setEnabled(false); // User needs to activate account.
        // user.setRoles(Arrays.asList("ROLE_USER"));

        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }
    
    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    // User confirmation.
    public User confirm(String token) throws InvalidTokenException, TokenExpiredException {
        VerificationToken verificationToken = getVerificationToken(token);
        User user = verificationToken.getUser();
        user.setEnabled(true);
        repository.save(user);
        tokenRepository.delete(verificationToken); // Clean up verification token
        return user;
    }
}
