package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NonExistantUserValidator
        implements ConstraintValidator<NonExistantUser, String> {

    @Autowired
    private PremiumUserService userService;

    @Override
    public void initialize(NonExistantUser constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context){
        if(userService.findByUserName(username).isPresent()) {
            return false;
        }

        return true;
    }
}
