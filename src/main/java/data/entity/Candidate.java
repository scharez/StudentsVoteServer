package data.entity;

import java.io.File;
import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Candidate implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String  username;
    private String  firstname;
    private String  lastname;

    public Candidate() {}

    public Candidate(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
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

}
