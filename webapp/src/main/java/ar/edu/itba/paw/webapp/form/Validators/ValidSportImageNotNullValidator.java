package ar.edu.itba.paw.webapp.form.Validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidSportImageNotNullValidator
        implements ConstraintValidator<ValidSportImageNotNull, MultipartFile> {

    @Override
    public void initialize(ValidSportImageNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {

        return image.getSize() > 0;
    }
}
