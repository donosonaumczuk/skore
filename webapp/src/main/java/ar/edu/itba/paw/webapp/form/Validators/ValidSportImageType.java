package ar.edu.itba.paw.webapp.form.Validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidSportImageTypeValidator.class)
@Documented
public @interface ValidSportImageType {
    String message() default "{ar.edu.itba.paw.webapp.form.SportForm.image.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}