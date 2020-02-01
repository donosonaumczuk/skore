package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.EntityAlreadyExistsException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityAlreadyExistsExceptionMapper implements ExceptionMapper<EntityAlreadyExistsException> {

    @Override
    public Response toResponse(EntityAlreadyExistsException entityAlreadyExistsException) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ApiErrorDto.of(HttpStatus.CONFLICT, entityAlreadyExistsException.getMessage()))
                .build();
    }
}
