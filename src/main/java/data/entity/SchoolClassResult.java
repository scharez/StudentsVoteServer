package data.entity;

import javax.persistence.*;

@Entity
public class SchoolClassResult {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private SchoolClass schoolClass;

    private int score;
    private int first;

    public SchoolClassResult() {}

    public SchoolClassResult(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
        this.score = 0;
        this.first = 0;
    }

    public SchoolClassResult(SchoolClass schoolClass, int score, int first) {
        this.schoolClass = schoolClass;
        this.score = score;
        this.first = first;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
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
