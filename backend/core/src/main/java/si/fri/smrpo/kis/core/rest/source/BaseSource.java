package si.fri.smrpo.kis.core.rest.source;

import si.fri.smrpo.kis.core.logic.database.Database;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.rest.source.interfaces.BaseSourceImpl;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import java.io.Serializable;

@PermitAll
public abstract class BaseSource<I extends Serializable> implements BaseSourceImpl<I> {

    protected DatabaseImpl<I> database;

    public BaseSource() { }

    public BaseSource(DatabaseImpl<I> database) {
        this.database = database;
    }

    public DatabaseImpl<I> getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseImpl<I> database) {
        this.database = database;
    }

    public void setDatabase(EntityManager em) {
        this.database = new Database<>(em);
    }
}
