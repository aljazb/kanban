package si.fri.smrpo.kis.core.logic.database.interfaces;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import java.io.Serializable;

public interface DatabaseVersionImpl<I extends Serializable> extends DatabaseCrudImpl<I> {

    <E extends BaseEntityVersion<E, I>> E createVersion(E entity) throws DatabaseException;
    <E extends BaseEntityVersion<E, I>> E updateVersion(E entity) throws DatabaseException;
    <E extends BaseEntityVersion<E, I>> E patchVersion(E entity) throws DatabaseException;

}
