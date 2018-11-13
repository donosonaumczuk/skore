package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.interfaces.SportService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UniqueSportNameValidator implements ConstraintValidator<UniqueSportName, String> {

    @Autowired
    SportService sportService;
    @Override
    public void initialize(UniqueSportName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String sportName, ConstraintValidatorContext context){
    try {

        if (sportService.findByName(sportName) == null) {
            return true;
        }
    } catch(Exception e) {
        return true;
    }

        return false;
    }
}
