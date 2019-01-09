package service;

import repository.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("persons")
public class StudentsVoteService {

    private Repository repo = new Repository();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("message")
    public String message() {
        return " REST Service powered by scharez.at ";
    }

    @GET
    @Path("loginCheck")
    public String loginCheck(String username, String password) {
        return repo.loginCheck(username, password);
    }

}
