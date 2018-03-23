package si.fri.smrpo.kis.core.rest.source;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.source.base.BaseSource;

import java.io.Serializable;

public class GetSource<E extends BaseEntity<E, I>, I extends Serializable> extends BaseSource<E, I> {

    public GetSource(DatabaseImpl<I> database) {
        super(database);
    }

    public GetSource(DatabaseImpl<I> database, DatabaseManager<E, I> dbmCore) {
        super(database, dbmCore);
    }

    public Paging<E> getList(Class<E> c, QueryParameters param) throws ApiException {
        try {
            return database.getList(c, param, dbmCore);
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }

    public E get(Class<E> c, I id) throws ApiException {
        try {
            E entity = database.get(c, id, dbmCore);
            if(entity != null){
                return entity;
            } else {
                throw new DatabaseException(LogicBaseException.Metadata.ENTITY_DOES_NOT_EXISTS);
            }
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }
}
