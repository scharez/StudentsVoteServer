package data.entity;

import data.enums.ElectionState;
import data.enums.ElectionType;

import javax.persistence.*;

@Entity
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String currentDate;

    private ElectionType electionType;
    private ElectionState electionState;

    public Election() {
        this.electionState = ElectionState.NEW;
    }

    public Election(String currentDate, ElectionType electionType) {
        this.currentDate = currentDate;
        this.electionType = electionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcurrentDate() {
        return currentDate;
    }

    public void setcurrentDate(String currentDate) {
        this.currentDate = currentDate;
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
