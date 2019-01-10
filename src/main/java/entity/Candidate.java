package entity;

public class Candidate {

    private String firstname;
    private String lastname;
    private String electionPromise;
    private String cadidateClass;
    private String email;
    private String picture;
    private int votes;

    public Candidate(String firstname, String lastname, String electionPromise, String cadidateClass, String email, String picture, int votes) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.electionPromise = electionPromise;
        this.cadidateClass = cadidateClass;
        this.email = email;
        this.picture = picture;
        this.votes = votes;
    }

    public Candidate() {

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

    public String getElectionPromise() {
        return electionPromise;
    }

    public void setElectionPromise(String electionPromise) {
        this.electionPromise = electionPromise;
    }

    public String getCadidateClass() {
        return cadidateClass;
    }

    public void setCadidateClass(String cadidateClass) {
        this.cadidateClass = cadidateClass;
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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }


}
