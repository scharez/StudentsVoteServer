package entity;

import java.io.File;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;

@Entity
public class Candidate implements Serializable {

    @Id
    @GeneratedValue
    private int     id;

    private String  username;
    private String  firstname;
    private String  lastname;
    private String  candidateClass;
    private String  department;
    private File    picture;
    private String  electionPromise;
    private String  position;

    @OneToMany
    private List<CandidateVote> candicateVotes;

    public Candidate() {}

    public Candidate(String username, String firstname, String lastname, String candidateClass, String department, File picture, String electionPromise, String position) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.candidateClass = candidateClass;
        this.department = department;
        this.picture = picture;
        this.electionPromise = electionPromise;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCandidateClass() {
        return candidateClass;
    }

    public void setCandidateClass(String candidateClass) {
        this.candidateClass = candidateClass;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }

    public String getElectionPromise() {
        return electionPromise;
    }

    public void setElectionPromise(String electionPromise) {
        this.electionPromise = electionPromise;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<CandidateVote> getCandicateVotes() {
        return candicateVotes;
    }

    public void setCandicateVotes(List<CandidateVote> candicateVotes) {
        this.candicateVotes = candicateVotes;
    }

}
