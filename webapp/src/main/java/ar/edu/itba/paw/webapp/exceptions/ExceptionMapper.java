package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.constants.MessageConstants;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        LoggerFactory.getLogger(ExceptionMapper.class).error("general exception mapper", e);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiErrorDto.of(HttpStatus.INTERNAL_SERVER_ERROR, MessageConstants.SERVER_ERROR_GENERIC_MESSAGE))
                .build();
    }
}

//public class ExceptionMapper {
//    /* TODO: This is to see the full exception in the response... */
//}
