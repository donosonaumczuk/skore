package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.webapp.form.MatchForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FutureTimeValidator
        implements ConstraintValidator<FutureTime, Object> {
    @Override
    public void initialize(FutureTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{ar.edu.itba.paw.webapp.form.Validators.FutureTime.message}")
                .addNode("startTime").addConstraintViolation();
        MatchForm matchForm = (MatchForm) obj;
        String dateTime = matchForm.getDate() + " " + matchForm.getStartTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        LocalDateTime givenDateTime;
        try {
            givenDateTime = LocalDateTime.parse(dateTime, formatter);
        } catch (Exception e) {
            return false;
        }
        System.out.println("dateTime: " + dateTime + "\n\n\n\n\n");
        System.out.println("givenDateTime.compareTo(LocalDateTime.now()) = " + givenDateTime.compareTo(LocalDateTime.now().plusMinutes(30)) + "\n\n\n\n" );
        return givenDateTime.compareTo(LocalDateTime.now().plusMinutes(30))>= 0;
    }
}