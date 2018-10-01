package ar.edu.itba.paw.webapp.form.Validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ValidDurationValidator
        implements ConstraintValidator<ValidDuration, String> {

    @Override
    public void initialize(ValidDuration constraintAnnotation) {
    }

    @Override
    public boolean isValid(String duration, ConstraintValidatorContext context){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime givenDuration;
        try {
            givenDuration = LocalTime.parse(duration, formatter);
        } catch (Exception e) {
            return false;
        }
        return givenDuration.getHour()*60 + givenDuration.getMinute() >= 20;
    }
}
