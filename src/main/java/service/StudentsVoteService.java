package service;

import entity.Candidate;
import repository.Repository;
import utils.User;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.File;

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

    @Path("getCandidates")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getCandidates(){

        return Repository.getInstance().getCandidate(false);
    }

    @Path("getfullCandidates")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getfullCandidates(){

        return Repository.getInstance().getCandidate(true);
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

    /*@Path("gimmeimage/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveimage(File file, @PathParam("id") int id){

        Repository.getInstance().saveimage(file, id);
        return "got it";
    }*/

    @Path("endelection")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public String endelection(){

        Repository.getInstance().endelection();

        return "this is the real end!";
    }


    @Path("test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
        return "lol";
    }



}
