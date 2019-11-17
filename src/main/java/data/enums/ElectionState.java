package data.enums;

import javax.persistence.Enumerated;

public enum ElectionState {
    @Enumerated NEW,
    RUNNING,
    STOPPED,
    ENDED
}
