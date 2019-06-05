package entity;

import javax.persistence.*;

@Entity
public class ElectionState {

    @Id
    @GeneratedValue
    private int     id;

    private boolean started;
    private boolean ended;
    public boolean  endedCompletely;

    public ElectionState() {
        this.started = false;
        this.ended = false;
        this.endedCompletely = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isEndedCompletely() {
        return endedCompletely;
    }

    public void setEndedCompletely(boolean endedCompletely) {
        this.endedCompletely = endedCompletely;
    }

}
