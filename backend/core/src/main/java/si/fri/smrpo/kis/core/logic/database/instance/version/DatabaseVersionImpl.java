package si.fri.smrpo.kis.core.logic.database.instance.version;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCoreImpl;
import si.fri.smrpo.kis.core.logic.database.manager.version.DatabaseManagerVersion;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import java.io.Serializable;

public interface DatabaseVersionImpl<K extends Serializable> extends DatabaseCoreImpl<K> {

    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion) throws DatabaseException;
    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws DatabaseException;

    <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion) throws DatabaseException;
    <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion, DatabaseManagerVersion<T> dbmCore) throws DatabaseException;

    <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion) throws DatabaseException;
    <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion, DatabaseManagerVersion<T> dbmCore) throws DatabaseException;

}
