package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(final EntityNotFoundException entityNotFoundException) {
        return Response.status(Response.Status.NOT_FOUND).entity(ApiErrorDto.of(HttpStatus.NOT_FOUND, entityNotFoundException.getMessage())).build();
    }
}
