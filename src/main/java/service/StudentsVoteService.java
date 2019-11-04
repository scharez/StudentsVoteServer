package service;

import data.dto.CandidatureDTO;
import data.dto.SchoolClassResultDTO;
import data.enums.ElectionType;
import org.json.JSONObject;
import repository.*;
import utils.User;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

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
    @Produces(MediaType.APPLICATION_JSON)
    public String getCandidates(){
        System.out.println("getCandidates");
        return CandidateRepository.getInstance().getCandidates();
    }

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

    @Path("createCandidature")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCandidature(CandidatureDTO candidatureDTO) {
        System.out.println("createCandidature");
        return CandidatureRepository.getInstance().createCandidature(candidatureDTO);
    }

    @Path("createElection")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createElection(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        System.out.println("createElection");
        return ElectionRepository.getInstance().createElection(date, Enum.valueOf(ElectionType.class, electionType));

    }

    @Path("startElection")
    @POST
    public String startElection() {
        System.out.println("startElection");
        return ElectionRepository.getInstance().startElection();
    }

    @Path("endElectionTeacher")
    @POST
    public String endElectionTeacher() {
        System.out.println("endElectionTeacher");
        return ElectionRepository.getInstance().endElectionTeacher();
    }

    @Path("endElection")
    @POST
    public String endElection() {
        System.out.println("endElection");
        return ElectionRepository.getInstance().endElection();
    }

    @Path("createSchoolClass")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createSchoolClass(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String name = jsonObject.get("name").toString();
        String date = jsonObject.get("date").toString();
        System.out.println("createSchoolClass");
        return SchoolClassRepository.getInstance().createSchoolClass(name, date);
    }

    @Path("createSchoolClassResult")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createSchoolClassResult(SchoolClassResultDTO schoolClassResultDTO) {
        System.out.println("createSchoolClass");
        return SchoolClassResultRepository.getInstance().createSchoolClassResult(schoolClassResultDTO);
    }

    @Path("getVotingClasses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getVotingClasses() {
        System.out.println("getVotingClasses");
        return SchoolClassResultRepository.getInstance().getVotingClasses();
    }

    @Path("getFinishedClasses")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getFinishedClasses() {
        System.out.println("getFinishedClasses");
        return SchoolClassResultRepository.getInstance().getFinishedClasses();
    }

    @Path("deleteSchoolClassResult")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteSchoolClassResult(String schoolClassName) {
        System.out.println("deleteSchoolClassResult");
        return SchoolClassResultRepository.getInstance().deleteSchoolClassResult(schoolClassName);
    }

    @Path("getSchoolClassResults")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSchoolClassResults(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String date = jsonObject.get("date").toString();
        String electionType = jsonObject.get("electionType").toString();
        System.out.println("deleteSchoolClassResult");
        return SchoolClassResultRepository.getInstance().getSchoolClassResults(date, Enum.valueOf(ElectionType.class, electionType));
    }

}
