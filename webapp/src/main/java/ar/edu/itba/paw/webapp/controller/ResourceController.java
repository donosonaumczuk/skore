package ar.edu.itba.paw.webapp.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;

@Controller
public class ResourceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/lang", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String handleLanguage() {
        LOGGER.trace("Handling language request for client i18n");
        return "{ \"lang\" : \"" + messageSource.getMessage("lang", null, LocaleContextHolder.getLocale()) + "\" }";
    }
}
