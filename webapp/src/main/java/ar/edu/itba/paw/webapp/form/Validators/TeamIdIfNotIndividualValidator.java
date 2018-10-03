package ar.edu.itba.paw.webapp.form.Validators;

import ar.edu.itba.paw.webapp.form.MatchForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TeamIdIfNotIndividualValidator
        implements ConstraintValidator<TeamIdIfNotIndividual, Object> {

    @Override
    public void initialize(TeamIdIfNotIndividual constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        context.disableDefaultConstraintViolation();
//        context.buildConstraintViolationWithTemplate("{ar.edu.itba.paw.webapp.form.Validators.TeamIdIfNotIndividual.message}")
//                .addNode("teamId").addConstraintViolation();
//        MatchForm matchForm = (MatchForm) obj;
//        String mode = matchForm.getMode();
//        if(mode != null) {
//            if (mode.equals("Team")) {
//                return matchForm.getTeamId().length() > 0;
//            }
            return true;
//        }
//        else {
//            return false;
//        }
    }
}
