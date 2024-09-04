package pl.dreamcode.errornotifier.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.dreamcode.errornotifier.users.RegistrationForm;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        RegistrationForm user = (RegistrationForm) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
