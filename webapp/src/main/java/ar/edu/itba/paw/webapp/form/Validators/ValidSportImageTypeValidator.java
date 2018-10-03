package ar.edu.itba.paw.webapp.form.Validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidSportImageTypeValidator
        implements ConstraintValidator<ValidSportImageType, MultipartFile> {

    @Override
    public void initialize(ValidSportImageType constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if(image == null || image.getSize() == 0) {
            return true;
        }

        String type = image.getContentType().replace("image/", "");
        //System.out.println("image : |" + image + "|" + "\n\n\n\n" );

        //System.out.println("type : |" + type + "|" + "\n\n\n\n" );
        return type.equals("png") || type.equals("jpeg") || type.equals("svg+xml");
    }

}
