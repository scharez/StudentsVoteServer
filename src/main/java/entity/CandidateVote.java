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
    private String      schoolClass;

    public CandidateVote() {}

    public CandidateVote(Candidate candidate, String schoolClass) {
        this.candidate = candidate;
        this.score = 0;
        this.first = 0;
        this.schoolClass = schoolClass;
    }

    public void addScore(int score) {
        this.score += score;
        if (score == 6) {
            this.first++;
        }
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

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

}
