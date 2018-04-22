package si.fri.smrpo.kis.core.rest.source.interfaces;

import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseImpl;

import javax.persistence.EntityManager;
import java.io.Serializable;

public interface BaseSourceImpl<I extends Serializable> {

    DatabaseImpl<I> getDatabase();
    void setDatabase(DatabaseImpl<I> database);
    void setDatabase(EntityManager em);

}
