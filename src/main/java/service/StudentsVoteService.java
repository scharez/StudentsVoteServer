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
     * @return a List of all Candidates
     */
    @Path("getCandidates")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Candidate> getCandidates(){
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

    /*@Path("gimmeimage/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveimage(File file, @PathParam("id") int id){

        Repository.getInstance().saveimage(file, id);
        return "got it";
    }*/

}
