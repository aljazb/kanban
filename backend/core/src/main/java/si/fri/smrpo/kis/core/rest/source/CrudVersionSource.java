package si.fri.smrpo.kis.core.rest.source;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.instance.interfaces.DatabaseImpl;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.rest.exception.ApiException;

import java.io.Serializable;

public class CrudVersionSource<
            E extends BaseEntityVersion<E, I>,
            I extends Serializable
        > extends GetSource<E, I>  {

    public CrudVersionSource(DatabaseImpl<I> database) {
        super(database);
    }

    public CrudVersionSource(DatabaseImpl<I> database, DatabaseManager<E, I> dbmCore) {
        super(database, dbmCore);
    }

    public E create(E newEntityVersion) throws ApiException {
        try {
            return database.createVersion(newEntityVersion, dbmCore);
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }

    public E update(E newEntityVersion) throws ApiException {
        try {
            return database.updateVersion(newEntityVersion, dbmCore);
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }

    public E patch(E newEntityVersion) throws ApiException {
        try {
            return database.patchVersion(newEntityVersion, dbmCore);
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }

    public E delete(Class<E> c, I id) throws ApiException {
        try {
            return database.delete(c, id, dbmCore);
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }

    public E toggleIsDeleted(Class<E> c, I id) throws ApiException {
        try {
            return database.toggleIsDeleted(c, id, dbmCore);
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }

    public E permDelete(Class<E> c, I id) throws ApiException {
        try {
            return database.permDelete(c, id, dbmCore);
        } catch (DatabaseException e) {
            throw ApiException.transform(e);
        }
    }



}
