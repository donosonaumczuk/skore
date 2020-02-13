package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        LoggerFactory.getLogger(ExceptionMapper.class).error("A generic exception was been thrown", e);
        Response.StatusType type = getStatusType(e);
        return Response
                .status(type.getStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ApiErrorDto.of(HttpStatus.valueOf(type.getStatusCode())))
                .build();
    }

    private Response.StatusType getStatusType(Exception e) {
        if (e instanceof WebApplicationException) {
            return ((WebApplicationException) e).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}

//public class ExceptionMapper {
//    /* TODO: This is to see the full exception in the response... */
//    /* TODO: Remove me when webapp is finished! */
//}
