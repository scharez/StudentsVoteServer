package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Election {

    @Id
    @GeneratedValue
    private int     id;

    @Temporal(TemporalType.DATE)
    private Date    date;
    private int     electionState; // 2 = running, 1 = ended for teachers, 0 = ended completely

    @OneToMany
    private List<Candidate> candidates;

    public Election() {}

    public Election(Date date) {
        this.date = date;
        this.candidates = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getElectionState() {
        return electionState;
    }

    public void setElectionState(int electionState) {
        this.electionState = electionState;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

}
