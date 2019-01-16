package service;

import entity.ReturningOfficer;
import repository.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import entity.User;

@Path("sv")
public class StudentsVoteService {


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("message")
    public String message() {
        return " REST Service powered by scharez.at ";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("login")
    public String loginCheck(User user) {
        return Repository.getInstance().loginCheck(user);
    }

    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(ReturningOfficer rs){
        return Repository.getInstance().register();
    }



}
