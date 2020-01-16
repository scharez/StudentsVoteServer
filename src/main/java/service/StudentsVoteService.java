package service;

import data.dto.CandidatureDTO;
import data.dto.SchoolClassResultDTO;
import data.enums.ElectionType;
import org.json.JSONObject;
import repository.*;
import utils.User;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public String loginCheck(String userString) {
        JSONObject jsonObject = new JSONObject(userString);
        String username = jsonObject.get("username").toString();
        String password = jsonObject.get("password").toString();
        System.out.println("Im Login");
        return Repository.getInstance().loginCheck(username, password);
    }

    // Returns all Candidates
    @Path("getCandidates")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCandidates(){
        System.out.println("getCandidates");
        return CandidateRepository.getInstance().getCandidates();
    }

    // Creates a new Candidate
    @Path("createCandidate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createCandidate(String json){
        JSONObject jsonObject = new JSONObject(json);
        String username = jsonObject.get("username").toString();
        String firstname = jsonObject.get("firstname").toString();
        String lastname = jsonObject.get("lastname").toString();
        System.out.println("createCandidate");
        return CandidateRepository.getInstance().createCandidate(username, firstname, lastname);
    }

    // Creates a new Candidature
    @Path("createCandidature")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createCandidature(CandidatureDTO candidatureDTO) {
        System.out.println("createCandidature");
        return CandidatureRepository.getInstance().createCandidature(candidatureDTO);
    }

    // Creates a new Election
    @Path("createElection")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createElection(String json) {
        System.out.println("Im createElection");
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        System.out.println("createElection");
        return ElectionRepository.getInstance().createElection(date, Enum.valueOf(ElectionType.class, electionType));

    }

    // Sets ElectionState to 'running'
    @Path("startElection")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String startElection(String json) {
        System.out.println("startElection");
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        return ElectionRepository.getInstance().startElection(date, Enum.valueOf(ElectionType.class, electionType));
    }

    // Sets ElectionState to 'stopped'
    @Path("endElectionTeacher")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String endElectionTeacher(String json) {
        System.out.println("endElectionTeacher");
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        return ElectionRepository.getInstance().endElectionTeacher(date, Enum.valueOf(ElectionType.class, electionType));
    }

    // Sets ElectionState to 'finished'
    @Path("endElection")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String endElection(String json) {
        System.out.println("endElection");
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        return ElectionRepository.getInstance().endElection(date, Enum.valueOf(ElectionType.class, electionType));
    }

    // Creates new SchoolClass
    @Path("createSchoolClass")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createSchoolClass(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String name = jsonObject.get("name").toString();
        String date = jsonObject.get("date").toString();
        System.out.println("createSchoolClass");
        return SchoolClassRepository.getInstance().createSchoolClass(name, date);
    }

    // Creates a new SchoolClassResult
    @Path("createSchoolClassResult")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createSchoolClassResult(SchoolClassResultDTO schoolClassResultDTO) {
        System.out.println("createSchoolClass");
        return SchoolClassResultRepository.getInstance().createSchoolClassResult(schoolClassResultDTO);
    }

    // Returns all SchoolClasses that have not yet voted
    @Path("getVotingClasses")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getVotingClasses(String json) {
        System.out.println("getVotingClasses");
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        return SchoolClassResultRepository.getInstance().getVotingClasses(date, Enum.valueOf(ElectionType.class, electionType));
    }

    // Returns all SchoolClasses that have already voted
    @Path("getFinishedClasses")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getFinishedClasses(String json) {
        System.out.println("getFinishedClasses");
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        return SchoolClassResultRepository.getInstance().getFinishedClasses(date, Enum.valueOf(ElectionType.class, electionType));
    }

    // Deletes all SchoolClassResults of a chosen SchoolClass
    @Path("deleteSchoolClassResult")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteSchoolClassResult(String json) {
        System.out.println("deleteSchoolClassResult");
        JSONObject jsonObject = new JSONObject(json);
        String schoolClassName = jsonObject.get("schoolClassName").toString();
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        return SchoolClassResultRepository.getInstance().deleteSchoolClassResult(schoolClassName, date, Enum.valueOf(ElectionType.class, electionType));
    }

    // Returns all candidates, scores and first
    @Path("getSchoolClassResults")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getSchoolClassResults(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        System.out.println("getSchoolClassResult");
        return SchoolClassResultRepository.getInstance().getSchoolClassResults(date, Enum.valueOf(ElectionType.class, electionType));
    }

    // Upload a file of all new SchoolClasses
    @Path("uploadCSV")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String uploadCSV(@FormParam("file") File file) {
        return Repository.getInstance().readCsvFile(file);
    }

    // Returns the date of the election
    @Path("getCurrentVoteDate")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getCurrentVoteDate(String electionType) {
        return Repository.getInstance().getCurrentVoteDate(Enum.valueOf(ElectionType.class, electionType));
    }

    @Path("deleteCandidature")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteCandidature(String username) {
        return CandidatureRepository.getInstance().deleteCandidature(username);
    }

    @Path("updateCandidature")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateCandidature(CandidatureDTO candidatureDTO) {
        return CandidatureRepository.getInstance().updateCandidature(candidatureDTO);
    }

    @Path("getCandidatures")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCandidatures() {
        return CandidatureRepository.getInstance().getCandidatures();
    }

}
