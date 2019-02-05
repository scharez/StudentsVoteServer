package service;

import entity.Candidate;
import entity.ReturningOfficer;
import repository.Repository;
import utils.User;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("sv")
public class StudentsVoteService {

    @Path("message")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String message() {
        System.out.println("Im message");

        return " REST Service powered by scharez.at ";
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginCheck(User user) {
        System.out.println("Im Login");
        return Repository.getInstance().loginCheck(user);
    }



    @Path("changereturningofficer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(ReturningOfficer rsold){
        return null;
    }



    @Path("setCandidate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String setCandidate(Candidate candidate){
        return Repository.getInstance().setCandidate(candidate);
    }



}
