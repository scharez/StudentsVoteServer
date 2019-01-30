package service;

import entity.ReturningOfficer;
import entity.User;
import repository.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Path("sv")
public class StudentsVoteService {

    @Path("message")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String message() {
        return " REST Service powered by scharez.at ";
    }

    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String loginCheck(User user) {
        return Repository.getInstance().loginCheck(user);
    }

    @Path("changereturningofficer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(ReturningOfficer rs){
        return Repository.getInstance().changereturningofficer(rs);
    }

    @Path("setCandidate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String setCandidate(){
        return Repository.getInstance().setCandidate();
    }



}
