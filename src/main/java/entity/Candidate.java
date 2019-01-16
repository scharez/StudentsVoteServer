package entity;

public class Candidate {

    private String firstname;
    private String lastname;
    private String cadidateClass;
    private String email;
    private String picture;
    private String electionPromise;
    private int votes;


    public Candidate(String firstname, String lastname, String cadidateClass, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.cadidateClass = cadidateClass;
        this.email = email;
    }




    public Candidate() {}

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
