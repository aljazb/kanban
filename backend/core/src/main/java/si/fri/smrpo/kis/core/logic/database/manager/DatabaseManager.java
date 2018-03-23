package si.fri.smrpo.kis.core.logic.database.manager;


import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;

import java.io.Serializable;

public abstract class DatabaseManager<T extends BaseEntity<T, K>, K extends Serializable> {

    public abstract boolean isUserInRole(String role);
    public abstract K getUserId();

    public CriteriaFilter<T> authCriteria() {
        return (p, cb, r) -> p;
    }

    public boolean authCriteriaRequiresDistinct(){
        return false;
    }

    public void authGet(DatabaseCore db, T entity) throws DatabaseException {

    }

    public void authSet(DatabaseCore db, T entity) throws DatabaseException {

    }

    public void validate(DatabaseCore db, T entity) throws DatabaseException {

    }

}
