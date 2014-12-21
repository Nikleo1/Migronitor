package de.tjanneck.migronitor.db;

import java.util.Date;

/**
 * Created by Programmieren on 03.12.2014.
 */
public class Attacke {
    private long id;
    private Date datumStart;
    private Date datumEnde;
    private String bemerkung;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDatumStart() {
        return datumStart;
    }

    public void setDatumStart(Date datumStart) {
        this.datumStart = datumStart;
    }

    public Date getDatumEnde() {
        return datumEnde;
    }

    public void setDatumEnde(Date datumEnde) {
        this.datumEnde = datumEnde;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }
}
