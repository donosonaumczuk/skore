package ar.edu.itba.paw.webapp.form.Validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidSportImageSizeValidator
        implements ConstraintValidator<ValidSportImageSize, MultipartFile> {

    private static final int MEGABYTE = 1024 * 1024;
    private final int maxSize = 1 * MEGABYTE;

    @Override
    public void initialize(ValidSportImageSize constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if(image == null) {
            return true;
        }

        long value = image.getSize();

        return value < maxSize;
    }
}
