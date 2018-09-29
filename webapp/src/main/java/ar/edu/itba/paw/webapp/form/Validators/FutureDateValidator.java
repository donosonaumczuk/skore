package ar.edu.itba.paw.webapp.form.Validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FutureDateValidator
        implements ConstraintValidator<FutureDate, String> {

    @Override
    public void initialize(FutureDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context){
        System.out.println("date: " + date + "\n\n\n\n\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate givenDate;
        try {
            givenDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            return false;
        }
        //System.out.println("givenDate.compareTo(LocalDate.now()) = " + givenDate.compareTo(LocalDate.now()) + "\n\n\n\n" );
        return givenDate.compareTo(LocalDate.now())>= 0;
    }
}