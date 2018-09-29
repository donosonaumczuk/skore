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
        System.out.println("birthday = " + birthday);
        try {
            givenDate = LocalDate.parse(birthday, formatter);
        } catch (Exception e) {
            return false;
        }
        System.out.println("givenDate= " + givenDate);
        System.out.println("givenDate.compareTo(LocalDate.now())" + givenDate.compareTo(LocalDate.now()) + "\n\n\n\n\n\n\n");
        return givenDate.compareTo(LocalDate.now())< 0;
    }
}