package pl.dreamcode.errornotifier.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.dreamcode.errornotifier.users.exception.UserAlreadyExistException;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImpTests {
    @InjectMocks
    private RegistrationServiceImp service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterUser() throws Exception {
        String email = "test@example.com";
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail(email);
        registrationForm.setPassword("secretPassword");

        User user = new User();
        user.setId(1l);
        user.setEmail(email);

        given(userRepository.save(any(User.class))).willReturn(user);

        User savedUser = service.registerNewUserAccount(registrationForm);
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserWithSameEmailExists() throws Exception {
        String email = "test@example.com";
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail(email);
        registrationForm.setPassword("secretPassword");

        User user = new User();
        user.setId(1l);
        user.setEmail(email);

        given(userRepository.findByEmail(email)).willReturn(user);

        assertThrows(UserAlreadyExistException.class, () -> service.registerNewUserAccount(registrationForm));
    }
}
