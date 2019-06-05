package service;

import entity.Candidate;
import repository.Repository;
import utils.Point;
import utils.User;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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

    /**
     * Returns all Candidates
     *
     * @return a stringyfied List of all Candidates
     */
    @Path("getCandidates")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public String getCandidates(){
        return Repository.getInstance().getCandidates();
    }

    // Nachdem der Wahlleiter einen Kandidaten eingetragen hat
    /**
     * Persists a new Candidate and creates the corresponding Result
     *
     * @param candidate Candidate
     * @return a String
     */
    @Path("setCandidate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String setCandidate(Candidate candidate){
        return Repository.getInstance().setCandidate(candidate);
    }

    // Nachdem der Lehrer die Klasse angegeben hat || Nachdem der Wahlleiter die Klasse angegeben hat, deren Cvs gelöscht werden sollen
    /**
     * Creates and temporarily saves a CV in a SchoolClass for each Candidate
     *
     * @param schoolClass String of SchoolClass name
     * @return a String
     */
    @Path("instanceCVs")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String instanceCVs(String schoolClass) {
        System.out.println("CVs created.");
        return Repository.getInstance().instanceCVs(schoolClass);
    }

    // Nachdem der Lehrer einen einzelnen Zettel bestätigt
    /**
     * Temporarily saves the Points from a single voting paper
     *
     * @param points Point[]
     * @return a String
     */
    @Path("parseJson")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String parseJson(Point[] points) {
        System.out.println("Points added.");
        return Repository.getInstance().parseJson(points);
    }

    // Nachdem der Lehrer die Wahl in einer Klasse beendet || Nachdem der Wahlleiter den Nachtrag einer Klasse beendet
    /**
     * Persist the CVs of a SchoolClass
     *
     * @return a String
     */
    @Path("persistCVs")
    @POST
    public String persistCVs() {
        System.out.println("CVs persisted");
        return Repository.getInstance().persistCVs();
    }

    // Nachdem der Wahlleiter die Diagramme lädt
    /**
     * Return current Results and SchoolClasses that already voted
     *
     * @return a String (Json) of the Results and SchoolClasses
     */
    @Path("getCVs")
    @POST
    public String getCVs() {
        System.out.println("Got CVs");
        return Repository.getInstance().getCVs();
    }

    // Nachdem der Wahlleiter die Klasse angegeben hat, deren Cvs gelöscht werden sollen
    /**
     * Delete previously persisted CVs
     *
     * @param schoolClass String of the SchoolClass's name who's Results must be removed
     * @return a String
     */
    @Path("deleteCVs")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String deleteCVs(String schoolClass) {
        System.out.println("CVs deleted.");
        return Repository.getInstance().deleteCVs(schoolClass);
    }

    // Nachdem der Wahlleiter die Wahl für alle beendet
    /**
     * End the election, calculate and persist the result
     *
     * @return a String
     */
    @Path("endElection")
    @POST
    public String endElection() {
        System.out.println("Results persisted.");
        return Repository.getInstance().endElection();
    }

    // Nachdem der Wahlleiter die Wahl startet
    /**
     * Starts the election, makes Teacher able to log in
     *
     * @return a String
     */
    @Path("startElection")
    @POST
    public String startElection() {
        System.out.println("Election started.");
        return Repository.getInstance().startElection();
    }

    // Nachdem der Wahlleiter die Wahl für die Lehrer beendet
    /**
     * End the election for the Teacher, makes them unable to log in
     *
     * @return a String
     */
    @Path("endElectionTeacher")
    @POST
    public String endElectionTeacher() {
        System.out.println("Election for Teacher ended.");
        return Repository.getInstance().endElectionTeacher();
    }

    /*@Path("gimmeimage/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveimage(File file, @PathParam("id") int id){

        Repository.getInstance().saveimage(file, id);
        return "got it";
    }*/

}
