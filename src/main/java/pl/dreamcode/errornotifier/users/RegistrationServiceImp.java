package pl.dreamcode.errornotifier.users;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

@Service
public class RegistrationServiceImp implements RegistrationService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User registerNewUserAccount(RegistrationForm registrationForm) throws UserAlreadyExistException {
        if (emailExists(registrationForm.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                + registrationForm.getEmail());
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setEmail(registrationForm.getEmail());
        user.setEnabled(true);
        // user.setRoles(Arrays.asList("ROLE_USER"));

        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }
}
