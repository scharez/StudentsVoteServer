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
    private String electionState;
    private String electionType;

    public Election(){
    }

    public Election(String currentDate, String electionType, String electionState) {
        this.currentDate = currentDate;
        this.electionState = electionState;
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

    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }

    public String getElectionState() {
        return electionState;
    }

    public void setElectionState(String electionState) {
        this.electionState = electionState;
    }

}
