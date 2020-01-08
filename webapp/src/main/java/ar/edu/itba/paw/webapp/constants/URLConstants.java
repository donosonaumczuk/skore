package ar.edu.itba.paw.webapp.constants;

import javax.ws.rs.core.UriBuilder;

public final class URLConstants {

    /* Localhost for development */
    public static final String API_BASE_URL = "http://localhost:8080/api/";

//    /* PawServer for production */
//    public static final String API_BASE_URL = "http://pawserver.it.itba.edu.ar/paw-2018b-04/api/";

    public static final UriBuilder API_BASE_URL_BUILDER = UriBuilder.fromUri(API_BASE_URL);
}
