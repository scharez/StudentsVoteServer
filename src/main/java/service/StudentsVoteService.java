package service;

import entity.ReturningOfficer;
import repository.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import objects.LoginCredential;

@Path("sv")
public class StudentsVoteService {

    @Path("message")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String message() {
        return " REST Service powered by scharez.at ";
    }

    @Path("login")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String loginCheck(LoginCredential user) {
        return Repository.getInstance().loginCheck(user);
    }

    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(ReturningOfficer rs){
        return Repository.getInstance().register();
    }



}
