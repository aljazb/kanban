package si.fri.smrpo.kis.core.logic.database.instance.version;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCoreImpl;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import java.io.Serializable;

public interface DatabaseVersionImpl<I extends Serializable> extends DatabaseCoreImpl<I> {

    <E extends BaseEntityVersion<E, I>> E createVersion(E newEntityVersion) throws DatabaseException;
    <E extends BaseEntityVersion<E, I>> E createVersion(E newEntityVersion, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntityVersion<E, I>> E updateVersion(E newBaseEntityVersion) throws DatabaseException;
    <E extends BaseEntityVersion<E, I>> E updateVersion(E newBaseEntityVersion, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntityVersion<E, I>> E patchVersion(E newBaseEntityVersion) throws DatabaseException;
    <E extends BaseEntityVersion<E, I>> E patchVersion(E newBaseEntityVersion, DatabaseManager<E, I> dbmCore) throws DatabaseException;

}
