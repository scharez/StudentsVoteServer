package data.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class ReturningOfficer implements Serializable {

    @Id
    private int id;

    private String username;


    public ReturningOfficer( int id, String username) {
        this.id = id;
        this.username = username;
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

}
