package entity;

import javax.persistence.*;

@Entity
public class ElectionState {

    @Id
    @GeneratedValue
    private int     id;

    private boolean hasEnded;

    public ElectionState() {
        this.hasEnded = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHasEnded() {
        return hasEnded;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }

}
