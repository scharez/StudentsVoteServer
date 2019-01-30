package entity;

import javax.persistence.*;

@Entity
public class ReturningOfficer {

    @Id
    @GeneratedValue
    private int     id;

    private String  username;

    public ReturningOfficer() {}

    public ReturningOfficer(String username, String password, String email) {
        this.username = username;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

