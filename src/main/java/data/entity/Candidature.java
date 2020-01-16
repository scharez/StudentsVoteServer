package data.entity;

import javax.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Candidature {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Candidate candidate;

    @ManyToOne
    private Election election;

    @OneToOne
    private SchoolClass schoolClass;

    private File picture;
    private String electionPromise;

    @OneToMany
    private List<SchoolClassResult> schoolClassResults;

    public Candidature() {}

    public Candidature(Candidate candidate, Election election, SchoolClass schoolClass, File picture, String electionPromise) {
        this.candidate = candidate;
        this.election = election;
        this.schoolClass = schoolClass;
        this.picture = picture;
        this.electionPromise = electionPromise;
        this.schoolClassResults = new ArrayList<>();
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

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
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

    public List<SchoolClassResult> getSchoolClassResults() {
        return schoolClassResults;
    }

    public void setSchoolClassResults(List<SchoolClassResult> schoolClassResults) {
        this.schoolClassResults = schoolClassResults;
    }

}
