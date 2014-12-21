package de.tjanneck.migronitor.de.tjanneck.migronitor.db;

import java.util.Date;

/**
 * Created by Programmieren on 04.12.2014.
 */
public class Schlaf {
    private long id;
    private Date schlafen;
    private Date wach;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getSchlafen() {
        return schlafen;
    }

    public void setSchlafen(Date schlafen) {
        this.schlafen = schlafen;
    }

    public Date getWach() {
        return wach;
    }

    public void setWach(Date wach) {
        this.wach = wach;
    }
}
