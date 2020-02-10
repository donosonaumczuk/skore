package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.TeamFullException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TeamFullExceptionMapper implements ExceptionMapper<TeamFullException> {

    @Override
    public Response toResponse(TeamFullException e) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ApiErrorDto.of(HttpStatus.CONFLICT, e.getMessage()))
                .build();
    }
}
