package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.GameNotPlayedYetException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GameNotPlayedYetExceptionMapper implements ExceptionMapper<GameNotPlayedYetException> {

    @Override
    public Response toResponse(GameNotPlayedYetException e) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(ApiErrorDto.of(HttpStatus.CONFLICT, e.getMessage()))
                .build();
    }
}
