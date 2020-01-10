package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    private static Validator validator;

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiumUserService;

    private Validator() {
        //Singleton
    }

    public static Validator getValidator() {
        if (validator == null) {
            validator = new Validator();
        }
        return validator;
    }

    public Validator userExist(String username) {
        premiumUserService.findByUserName(username)
            .orElseThrow(() -> {
                LOGGER.trace("Can't get '{}' profile, user not found", username);
                return new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
            });
        return this;
    }
}
