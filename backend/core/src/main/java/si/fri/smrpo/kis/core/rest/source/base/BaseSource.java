package si.fri.smrpo.kis.core.rest.source.base;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;

import java.io.Serializable;

public abstract class BaseSource<E extends BaseEntity<E, I>, I extends Serializable> {

    protected DatabaseImpl<I> database;
    protected DatabaseManager<E, I> dbmCore;

    public BaseSource(DatabaseImpl<I> database) {
        this.database = database;
    }

    public BaseSource(DatabaseImpl<I> database, DatabaseManager<E, I> dbmCore) {
        this.database = database;
        this.dbmCore = dbmCore;
    }
}
