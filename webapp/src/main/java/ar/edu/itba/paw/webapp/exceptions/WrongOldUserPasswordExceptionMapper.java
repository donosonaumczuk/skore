package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.WrongOldUserPasswordException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WrongOldUserPasswordExceptionMapper implements ExceptionMapper<WrongOldUserPasswordException> {

    @Override
    public Response toResponse(WrongOldUserPasswordException e) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ApiErrorDto.of(HttpStatus.CONFLICT, "WRONG_OLD_PASSWORD", e.getMessage()))
                .build();
    }
}
