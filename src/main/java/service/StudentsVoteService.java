package service;

import entity.Candidate;
import entity.ReturningOfficer;
import repository.Repository;
import utils.User;

import javax.ws.rs.*;
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
    public String register(ReturningOfficer rsold, ReturningOfficer rsnew){
        return Repository.getInstance().changereturningofficer(rsold, rsnew);
    }



    @Path("setCandidate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String setCandidate(Candidate candidate){
        return Repository.getInstance().setCandidate(candidate);
    }



}
