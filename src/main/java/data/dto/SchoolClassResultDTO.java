package data.dto;

public class SchoolClassResultDTO {

    private String username;
    private String schoolClassName;
    private String date;
    private int score;
    private int first;

    public SchoolClassResultDTO(String username, String schoolClassName, String date, int score, int first) {
        this.username = username;
        this.schoolClassName = schoolClassName;
        this.date = date;
        this.score = score;
        this.first = first;
    }

    public SchoolClassResultDTO() {

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

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

}
