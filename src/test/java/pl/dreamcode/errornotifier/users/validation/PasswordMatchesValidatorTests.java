package pl.dreamcode.errornotifier.users.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;
import pl.dreamcode.errornotifier.users.RegistrationForm;

@ExtendWith(MockitoExtension.class)
public class PasswordMatchesValidatorTests {

    @Mock
    private PasswordMatches passwordMatches;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void shouldCheckMatchedPasswords() throws Exception {
        String password = "testPassword1234";
        RegistrationForm registrationForm = new RegistrationForm();

        registrationForm.setPassword(password);
        registrationForm.setMatchingPassword(password);

        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        assertTrue(validator.isValid(registrationForm, constraintValidatorContext));

    }

    @Test
    void shouldCheckNotMatchedPasswords() throws Exception {
        String password = "testPassword1234";
        RegistrationForm registrationForm = new RegistrationForm();

        registrationForm.setPassword(password);
        registrationForm.setMatchingPassword(password+"sad");

        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        assertFalse(validator.isValid(registrationForm, constraintValidatorContext));

    }
}
