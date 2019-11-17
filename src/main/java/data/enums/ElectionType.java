package data.enums;

import javax.persistence.Enumerated;

public enum ElectionType {
    @Enumerated SCHULSPRECHER,
    ABTEILUNGSSPRECHER,
    WAHL,
    STICHWAHL
}
