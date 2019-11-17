package data.dto;

import data.enums.ElectionType;

import java.io.File;
import java.util.Date;

public class CandidatureDTO {

    private String username;
    private String date;
    private ElectionType electionType;
    private String schoolClassName;
    private File picture;
    private String electionPromise;

    public CandidatureDTO(String username, String date, ElectionType electionType, String schoolClassName, File picture, String electionPromise) {
        this.username = username;
        this.date = date;
        this.electionType = electionType;
        this.schoolClassName = schoolClassName;
        this.picture = picture;
        this.electionPromise = electionPromise;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSchoolClassName() {
        return schoolClassName;
    }

    public void setSchoolClassName(String schoolClassName) {
        this.schoolClassName = schoolClassName;
    }

    public File getPicture() {
        return picture;
    }

    public void setPicture(File picture) {
        this.picture = picture;
    }

    public String getElectionPromise() {
        return electionPromise;
    }

    public void setElectionPromise(String electionPromise) {
        this.electionPromise = electionPromise;
    }

}
