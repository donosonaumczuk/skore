package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(final ApiException apiException) {
        return Response.status(apiException.getStatusCode()).entity(ApiErrorDto.from(apiException)).build();
    }
}
