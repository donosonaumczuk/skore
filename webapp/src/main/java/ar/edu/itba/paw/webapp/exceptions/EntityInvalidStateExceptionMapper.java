package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.invalidstate.EntityInvalidStateException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityInvalidStateExceptionMapper implements ExceptionMapper<EntityInvalidStateException> {

    @Override
    public Response toResponse(EntityInvalidStateException e) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ApiErrorDto.of(HttpStatus.CONFLICT, e.getType(), e.getMessage()))
                .build();
    }
}
