package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.alreadyexists.EntityAlreadyExistsException;
import ar.edu.itba.paw.exceptions.alreadyexists.UserAlreadyExistException;
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
                .entity(entityFrom(entityAlreadyExistsException))
                .build();
    }

    private ApiErrorDto entityFrom(EntityAlreadyExistsException exception) {
        if (exception instanceof UserAlreadyExistException) {
            return ApiErrorDto.of(
                    HttpStatus.CONFLICT,
                    ((UserAlreadyExistException) exception).getConflictingAttributeName().toUpperCase() + "_EXISTS",
                    exception.getMessage()
            );
        }
        return ApiErrorDto.of(HttpStatus.CONFLICT, exception.getMessage());
    }
}
