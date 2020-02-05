package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonExistantEmailValidator
        implements ConstraintValidator<NonExistantEmail, String> {

    @Autowired
    private PremiumUserService userService;

    @Override
    public void initialize(NonExistantEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){
        try {
            userService.findByEmail(email);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}

