package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.webapp.form.ModifyPasswordForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NewPasswordMatchesValidator implements ConstraintValidator<NewPasswordMatches, Object> {

    @Override
    public void initialize(NewPasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{ar.edu.itba.paw.webapp.form.Validators.NewPasswordMatches.message}")
                .addNode("repeatNewPassword").addConstraintViolation();
        ModifyPasswordForm form = (ModifyPasswordForm) obj;
        return form.getNewPassword().equals(form.getRepeatNewPassword());
    }
}