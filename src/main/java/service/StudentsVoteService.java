package service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("persons")
public class StudentsVoteService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("message")
    public String message() {
        return " REST Service powered by scharez.at ";
    }
}
