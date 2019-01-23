package entity;

import javax.persistence.*;

public class Candidate {

    @Id
    @GeneratedValue
    private int     id;

    private String  firstname;
    private String  lastname;
    private String candidateClass;
    private String  email;
    private String  picture;
    private String  electionPromise;
    private int     votes;

    public Candidate() {}

    public Candidate(int id, String firstname, String lastname, String candidateClass, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.candidateClass = candidateClass;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getElectionPromise() {
        return electionPromise;
    }

    public void setElectionPromise(String electionPromise) {
        this.electionPromise = electionPromise;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

}

