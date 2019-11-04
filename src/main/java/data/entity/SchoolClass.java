package data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SchoolClass {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String date;

    public SchoolClass() {}

    public SchoolClass(String name, String date) {
        this.name = name;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
