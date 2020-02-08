package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.TemporalTeamNotCreatedException;
import ar.edu.itba.paw.webapp.constants.MessageConstants;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TemporalTeamNotCreatedExceptionMapper implements ExceptionMapper<TemporalTeamNotCreatedException> {

    @Override
    public Response toResponse(TemporalTeamNotCreatedException e) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiErrorDto.of(HttpStatus.INTERNAL_SERVER_ERROR, MessageConstants.SERVER_ERROR_GENERIC_MESSAGE))
                .build();
    }
}
