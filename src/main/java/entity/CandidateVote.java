package entity;

import javax.persistence.*;

@Entity
public class CandidateVote {

    @Id
    @GeneratedValue
    private int         id;

    @ManyToOne
    private Candidate   candidate;

    private int         score;
    private int         first;

    @ManyToOne
    private SchoolClass schoolClass;

    public CandidateVote() {}

    public CandidateVote(Candidate candidate, SchoolClass schoolClass) {
        this.candidate = candidate;
        this.score = 0;
        this.first = 0;
        this.schoolClass = schoolClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

}
