package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.AlreadyJoinedToMatchException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyJoinedToMatchExceptionMapper implements ExceptionMapper<AlreadyJoinedToMatchException> {

    @Override
    public Response toResponse(AlreadyJoinedToMatchException alreadyJoinedToMatchException) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ApiErrorDto.of(HttpStatus.CONFLICT, alreadyJoinedToMatchException.getMessage()))
                .build();
    }
}