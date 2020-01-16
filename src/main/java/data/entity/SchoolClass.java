package data.entity;

import javax.persistence.*;

@Entity
public class SchoolClass {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String currentDate;

    public SchoolClass() {}

    public SchoolClass(String name, String currentDate) {
        this.name = name;
        this.currentDate = currentDate;
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
        return currentDate;
    }

    public void setded(String ded) {
        this.currentDate = ded;
    }

}
