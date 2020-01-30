package data.dto;

import data.enums.ElectionType;

import java.io.File;
import java.util.Date;

public class CandidatureDTO {

    private String username;
    private String firstname = "";
    private String lastname = "";
    private String date;
    private String electionType;
    private String schoolClassName;
    private File picture;
    private String electionPromise;

    public CandidatureDTO(String username, String date, String electionType, String schoolClassName, File picture, String electionPromise) {
        this.username = username;
        this.date = date;
        this.electionType = electionType;
        this.schoolClassName = schoolClassName;
        this.picture = picture;
        this.electionPromise = electionPromise;
    }

    public CandidatureDTO(String username, String firstname, String lastname, String date, String electionType, String schoolClassName, File picture, String electionPromise) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.electionType = electionType;
        this.schoolClassName = schoolClassName;
        this.picture = picture;
        this.electionPromise = electionPromise;
    }

    public CandidatureDTO() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
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
