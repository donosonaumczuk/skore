package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.webapp.form.MatchForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FutureEndTimeValidator
        implements ConstraintValidator<FutureEndTime, Object> {
    @Override
    public void initialize(FutureEndTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{ar.edu.itba.paw.webapp.form.Validators.FutureEndTime.message}")
                .addNode("endTime").addConstraintViolation();
        MatchForm matchForm = (MatchForm) obj;
        String startTime = matchForm.getStartTime();
        String endTime = matchForm.getEndTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime givenStartTime;
        LocalTime givenEndTime;
        try {
            givenStartTime = LocalTime.parse(startTime, formatter);
            givenEndTime = LocalTime.parse(startTime, formatter);


        } catch (Exception e) {
            return false;
        }
        System.out.println("Time: " + startTime + "-" + endTime + "\n\n\n\n\n");
        System.out.println("givenEndTime.isAfter(givenStartTime.plusMinutes(20)) = " + givenEndTime.isAfter(givenStartTime.plusMinutes(20)) + "\n\n\n\n" );

        return givenEndTime.isAfter(givenStartTime.plusMinutes(20));
    }
}