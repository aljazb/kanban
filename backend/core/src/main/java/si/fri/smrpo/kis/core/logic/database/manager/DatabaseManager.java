package si.fri.smrpo.kis.core.logic.database.manager;


import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import java.io.Serializable;

public abstract class DatabaseManager<T extends BaseEntity<T, K>, K extends Serializable> {

    public abstract boolean isUserInRole(String role);
    public abstract K getUserId();

    public CriteriaFilter<T> authCriteria(DatabaseCore dbCore, Class<T> c, CriteriaFilter<T> criteriaFilter) throws DatabaseException {
        return criteriaFilter;
    }

    public void authGet(DatabaseCore db, T entity) throws DatabaseException {

    }

    public void authSet(DatabaseCore db, T entity) throws DatabaseException {

    }

    public void validate(DatabaseCore db, T entity) throws DatabaseException {

    }

}
