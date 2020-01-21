package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class JSONUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T jsonToObject(final String json, final Class<T> classToConvertTo) {
        try {
            return OBJECT_MAPPER.readValue(json, classToConvertTo);
        } catch (IOException e) {
            LOGGER.error("Error when converting JSON to " + classToConvertTo.getName() + " object", e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurs when parsing JSON");
        }
    }

    public static JSONObject jsonObjectFrom(final String jsonString) {
        return new JSONObject(jsonString);
    }
}
