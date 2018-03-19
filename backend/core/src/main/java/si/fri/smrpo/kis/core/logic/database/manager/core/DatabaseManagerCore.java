package si.fri.smrpo.kis.core.logic.database.manager.core;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCore;
import si.fri.smrpo.kis.core.logic.database.manager.base.DatabaseManagerBase;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

public class DatabaseManagerCore<T extends BaseEntity> extends DatabaseManagerBase {

    public void authQuery(DatabaseCore dbCore, Class<T> c, QueryParameters param) throws DatabaseException {

    }

    public void authCriteria(DatabaseCore dbCore, Class<T> c, CriteriaFilter<T> criteriaFilter) throws DatabaseException {

    }

    public void authGet(DatabaseCore db, T entity) throws DatabaseException {

    }

    public void authSet(DatabaseCore db, T entity) throws DatabaseException {

    }

    public void validate(DatabaseCore db, T entity) throws DatabaseException {

    }

}
