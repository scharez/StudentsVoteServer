package entity;

import javax.persistence.*;

@Entity
public class Candidate {

    @Id
    @GeneratedValue
    private int     id;

    private String username;
    private String  firstname;
    private String  lastname;
    private String  candidateClass;
    private String  abteilung;
    private String  picture;
    private String  electionPromise;
    private int     votes;

    public Candidate() {}



    public Candidate(String username, String firstname, String lastname, String candidateClass, String abteilung, String picture, String electionPromise, int votes) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.candidateClass = candidateClass;
        this.abteilung = abteilung;
        this.picture = picture;
        this.electionPromise = electionPromise;
        this.votes = votes;
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

    public String getAbteilung() {
        return abteilung;
    }

    public void setAbteilung(String abteilung) {
        this.abteilung = abteilung;
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
