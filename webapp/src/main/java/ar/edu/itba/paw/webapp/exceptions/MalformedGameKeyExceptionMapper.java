package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.MalformedGameKeyException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MalformedGameKeyExceptionMapper implements ExceptionMapper<MalformedGameKeyException> {

    @Override
    public Response toResponse(MalformedGameKeyException malformedGameKeyException) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ApiErrorDto.of(HttpStatus.BAD_REQUEST, malformedGameKeyException.getMessage()))
                .build();
    }
}
