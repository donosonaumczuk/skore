package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LackOfPermissionsException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LackOfPermissionsExceptionMapper implements ExceptionMapper<LackOfPermissionsException> {

    @Override
    public Response toResponse(LackOfPermissionsException e) {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(ApiErrorDto.of(HttpStatus.FORBIDDEN, e.getMessage()))
                .build();
    }
}
