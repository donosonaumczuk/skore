package ar.edu.itba.paw.webapp.form.Validators;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PastDateValidator
        implements ConstraintValidator<PastDate, String> {

    @Override
    public void initialize(PastDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String birthday, ConstraintValidatorContext context){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate givenDate;
        try {
            givenDate = LocalDate.parse(birthday, formatter);
        } catch (Exception e) {
            return false;
        }
        return givenDate.compareTo(LocalDate.now())< 0;
    }
}