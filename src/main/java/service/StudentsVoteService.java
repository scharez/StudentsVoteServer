package service;

import entity.Candidate;
import org.json.JSONObject;
import repository.Repository;
import utils.Point;
import utils.User;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.File;
import java.util.List;
import java.util.jar.JarEntry;

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
    public List<Candidate> getCandidates(){
        return Repository.getInstance().getCandidates();
    }

    /*
    @Path("getfullCandidates")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getfullCandidates(){

        return Repository.getInstance().getCandidate(true);
    }
    */

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

    @Path("test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
        return "lol";
    }


    //Aufruf: Am Beginn der Wahl in einer Klasse
    //Funktion: Die teporär gespeicherten CVs werden gelöcht. Dann wird für jeden Candidate ein CV angelegt
    @Path("instanceCVs")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String instanceCVs(String schoolClass) {
        System.out.println("CVs created.");
        return Repository.getInstance().instanceCVs(schoolClass);
    }

    //Aufruf: Nach dem Eintragen eines einzelnen Stimmzettels
    //Funktion: Der Array von Points wird übergeben und der score wird in das entsprechende CV eingetragen
    @Path("parseJson")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String parseJson(Point[] points) {
        System.out.println("Points added.");
        return Repository.getInstance().parseJson(points);
    }

    //Aufruf: Nach dem Abschluss einer Wahl in einer einzelnen Klasse
    // Funktion: Persistiert alle temporär gespeicherten CVs und löscht sie danach
    @Path("persistCVs")
    @POST
    public String persistCVs() {
        System.out.println("CVs persisted");
        return Repository.getInstance().persistCVs();
    }

    @Path("getCVs")
    @POST

    public String getCVs() {
        System.out.println("Got CVs");
        return Repository.getInstance().getCVs();
    }

    //Aufruf: Nachdem die Wahlen in allen Klassen abgeschlossen sind
    //Funktion: Die einzelnen CVs werden in dem jeweiligen Result zusammengefügt und diese werden anschließend persistiert
    @Path("endElection")
    @POST
    public String endElection() {
        System.out.println("Results persisted.");
        return Repository.getInstance().endElection();
    }

}
