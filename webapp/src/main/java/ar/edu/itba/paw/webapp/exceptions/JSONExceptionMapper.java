package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JSONExceptionMapper implements ExceptionMapper<JSONException> {

    @Override
    public Response toResponse(JSONException jsonException) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ApiErrorDto.of(HttpStatus.BAD_REQUEST, "The request body must be a valid JSON"))
                .build();
    }
}