package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.dto.PremiumUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("test")
@Produces({ MediaType.APPLICATION_JSON })
public class TestController {

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiumUserService;

    @GET
    @Path("/{username}")
    public Response getPremiumUser(@PathParam("username") String username) {
        PremiumUser premiumUser = premiumUserService.findByUserName(username).get();
        return Response.ok(new PremiumUserDto(premiumUser)).build();
    }
}