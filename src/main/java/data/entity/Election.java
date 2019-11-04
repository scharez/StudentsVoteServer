package data.entity;

import data.enums.ElectionState;
import data.enums.ElectionType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Election {

    @Id
    @GeneratedValue
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private ElectionType electionType;
    private ElectionState electionState;

    public Election() {}

    public Election(Date date, ElectionType electionType) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
