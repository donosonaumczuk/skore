package ar.edu.itba.paw.webapp.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriBuilder;

@Component
public final class URLConstants {

    public static String BASE_URL;
    public static final String API_PATH = "api/";
    public static String API_BASE_URL;

    private static UriBuilder BASE_URL_BUILDER;
    private static UriBuilder API_BASE_URL_BUILDER;

    @Autowired
    public URLConstants(Environment environment) {
        URLConstants.BASE_URL = environment.getRequiredProperty(environment.getRequiredProperty("state") + ".url.backend.base");
        URLConstants.BASE_URL_BUILDER = UriBuilder.fromUri(BASE_URL);
        URLConstants.API_BASE_URL = getBaseUrlBuilder().path(API_PATH).toTemplate();
        URLConstants.API_BASE_URL_BUILDER = UriBuilder.fromUri(API_BASE_URL);
    }

    public static UriBuilder getApiBaseUrlBuilder() {
        return API_BASE_URL_BUILDER.clone();
    }

    public static UriBuilder getBaseUrlBuilder() {
        return BASE_URL_BUILDER.clone();
    }
}
