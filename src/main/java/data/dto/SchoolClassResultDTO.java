package data.dto;

import java.util.Date;

public class SchoolClassResultDTO {

    private String username;
    private String schoolClassName;
    private String date;
    private int score;

    public SchoolClassResultDTO(String username, String schoolClassName, String date, int score) {
        this.username = username;
        this.schoolClassName = schoolClassName;
        this.date = date;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchoolClassName() {
        return schoolClassName;
    }

    public void setSchoolClassName(String schoolClassName) {
        this.schoolClassName = schoolClassName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
