package ar.edu.itba.paw.webapp.Validators;

import ar.edu.itba.paw.webapp.form.UserForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserForm user = (UserForm) obj;
        return user.getPassword().equals(user.getRepeatPassword());
    }
}
