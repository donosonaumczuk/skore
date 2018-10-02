package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.PremiumUser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;

@Controller
public class ResourceController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiunUserService;

    @Autowired
    @Qualifier("sportServiceImpl")
    private SportService sportService;

    @RequestMapping(value = "/lang", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String handleLanguage() {
        LOGGER.trace("Handling language request for client i18n");
        return "{ \"lang\" : \"" + messageSource.getMessage("lang", null, LocaleContextHolder.getLocale()) + "\" }";
    }

    @RequestMapping(value = "/{type}/image/{id}", method = {RequestMethod.GET})
    public ResponseEntity<byte[]> getImageSport(@PathVariable final String type,
                                                @PathVariable final String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","image/*");
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        byte[] media;
        if(type.compareTo("profile")==0) {
            media = premiunUserService.readImage(id);
        }
        else if(type.compareTo("sport")==0)  {
            media = sportService.readImage(id);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }
}
