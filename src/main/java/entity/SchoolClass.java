package entity;

import javax.persistence.*;

@Entity
public class SchoolClass {

    @Id
    @GeneratedValue
    private int     id;

    private String  name;
    private boolean hasVoted;

    public SchoolClass() {}

    public SchoolClass(String name) {
        this.name = name;
        this.hasVoted = false;
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

    public boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVorted) {
        this.hasVoted = hasVorted;
    }

}
