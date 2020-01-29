package data.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class ReturningOfficer implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String username;
    private String password;

    public ReturningOfficer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ReturningOfficer() {

    }

    public int getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
