package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.InvalidUserCodeException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidUserCodeExceptionMapper implements ExceptionMapper<InvalidUserCodeException> {

    @Override
    public Response toResponse(InvalidUserCodeException e) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ApiErrorDto.of(HttpStatus.CONFLICT, "INVALID_CODE", e.getMessage()))
                .build();
    }
}
