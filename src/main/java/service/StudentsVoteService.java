package service;

import entity.Candidate;
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



    /*@Path("changereturningofficer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@QueryParam("username_old") String username_old,
                           @QueryParam("password_old") String password_old,
                           @QueryParam("username_new") String username_new,
                           @QueryParam("password_new") String password_new){
        return Repository.getInstance().changereturningofficer(username_old, password_old, username_new, password_new);
    }*/



    @Path("setCandidate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String setCandidate(Candidate candidate){
        return Repository.getInstance().setCandidate(candidate);
    }


    @Path("instanceCVs")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String instanceCVs(String schoolClass) {
        System.out.println("CVs created.");
        return Repository.getInstance().instanceCVs(schoolClass);
    }

    @Path("parseJson")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String parseJson(String json) {
        System.out.println("Points added. " + json);
        return Repository.getInstance().parseJson(json);
    }

    @Path("persistCVs")
    @POST
    public String persistCVs() {
        System.out.println("CVs persisted");
        return Repository.getInstance().persistCVs();
    }

}
