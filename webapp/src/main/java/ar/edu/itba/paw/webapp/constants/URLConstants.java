package ar.edu.itba.paw.webapp.constants;

import javax.ws.rs.core.UriBuilder;

public final class URLConstants {

    /* Localhost for development */
    public static final String BASE_URL = "http://localhost:8080/";

//    /* PawServer for production */
//    public static final String BASE_URL = "http://pawserver.it.itba.edu.ar/paw-2018b-04/";

    public static final String API_PATH = "api/";

    private static final UriBuilder BASE_URL_BUILDER = UriBuilder.fromUri(BASE_URL);

    public static final String API_BASE_URL = getBaseUrlBuilder().path(API_PATH).toTemplate();

    private static final UriBuilder API_BASE_URL_BUILDER = UriBuilder.fromUri(API_BASE_URL);

    public static UriBuilder getApiBaseUrlBuilder() {
        return API_BASE_URL_BUILDER.clone();
    }

    public static UriBuilder getBaseUrlBuilder() {
        return BASE_URL_BUILDER.clone();
    }
}
