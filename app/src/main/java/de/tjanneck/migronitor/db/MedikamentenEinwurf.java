package de.tjanneck.migronitor.db;

import java.util.Date;

/**
 * Created by Programmieren on 04.12.2014.
 */
public class MedikamentenEinwurf {
    private long id;
    private Date datum;
    private long mid;
    private int menge;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }
}
