package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.form.ModifyPasswordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCurrentPasswordValidator implements ConstraintValidator<ValidCurrentPassword, Object> {
    @Autowired
    PremiumUserService premiumUserService;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    public void initialize(ValidCurrentPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{ar.edu.itba.paw.webapp.form.Validators.ValidCurrentPassword.message}")
                .addNode("oldPassword").addConstraintViolation();
        ModifyPasswordForm form = (ModifyPasswordForm) obj;
        PremiumUser user = premiumUserService.findByUserName(form.getUsername());//should never be empty
        return bcrypt.matches(form.getOldPassword(), user.getPassword());
    }
}