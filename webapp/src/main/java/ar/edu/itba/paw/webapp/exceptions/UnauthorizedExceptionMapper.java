package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
//TODO: this is not working! check later
    @Override
    public Response toResponse(UnauthorizedException unauthorizedException) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ApiErrorDto.of(HttpStatus.UNAUTHORIZED,
                        "You must be logged into an account to perform this action"))
                .build();
    }
}
