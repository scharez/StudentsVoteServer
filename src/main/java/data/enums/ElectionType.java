package data.enums;

import javax.persistence.Enumerated;

public enum ElectionType {
    @Enumerated
    SCHULSPRECHER,
    ABTEILUNGSSPRECHER,
    STICHWAHL_SCHULSPRECHER,
    STICHWAHL_ABTEILUNGSSPRECHER
}
