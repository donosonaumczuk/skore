package ar.edu.itba.paw.webapp.form.Validators;

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
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{ar.edu.itba.paw.webapp.form.Validators.PasswordMatches.message}")
                .addNode("repeatPassword").addConstraintViolation();
        UserForm user = (UserForm) obj;
        return user.getPassword().equals(user.getRepeatPassword());
    }
}
