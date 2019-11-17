package data.entity;

import data.enums.ElectionState;
import data.enums.ElectionType;

import javax.persistence.*;

@Entity
public class Election {

    @Id
    @GeneratedValue
    private int id;

    private String date;

    private ElectionType electionType;
    private ElectionState electionState;

    public Election() {}

    public Election(String date, ElectionType electionType) {
        this.date = date;
        this.electionType = electionType;
        this.electionState = ElectionState.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ElectionType getElectionType() {
        return electionType;
    }

    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }

    public ElectionState getElectionState() {
        return electionState;
    }

    public void setElectionState(ElectionState electionState) {
        this.electionState = electionState;
    }

}
