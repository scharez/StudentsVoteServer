package data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SchoolClass {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String ded;

    public SchoolClass() {}

    public SchoolClass(String name, String ded) {
        this.name = name;
        this.ded = ded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getded() {
        return ded;
    }

    public void setded(String ded) {
        this.ded = ded;
    }

}
