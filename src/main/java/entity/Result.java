package entity;

import javax.persistence.*;

@Entity
public class Result {

    @Id
    @GeneratedValue
    private int         id;

    @OneToOne
    private Candidate   candidate;

    private int         score;
    private int         first;

    public Result() {}

    public Result(Candidate candidate) {
        this.candidate = candidate;
        this.score = 0;
        this.first = 0;
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

}
