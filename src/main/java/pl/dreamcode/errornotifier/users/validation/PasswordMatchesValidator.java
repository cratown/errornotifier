package pl.dreamcode.errornotifier.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.dreamcode.errornotifier.users.PasswordMatchesForm;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        PasswordMatchesForm user = (PasswordMatchesForm) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
