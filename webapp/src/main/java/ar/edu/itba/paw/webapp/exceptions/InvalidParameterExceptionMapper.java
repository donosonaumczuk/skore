package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.InvalidParameterException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidParameterExceptionMapper implements ExceptionMapper<InvalidParameterException> {

    @Override
    public Response toResponse(InvalidParameterException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ApiErrorDto.of(HttpStatus.BAD_REQUEST, e.getMessage()))
                .build();
    }
}
