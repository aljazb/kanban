package si.fri.smrpo.kis.core.rest.source;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.instance.Database;
import si.fri.smrpo.kis.core.logic.database.manager.core.DatabaseManagerCore;
import si.fri.smrpo.kis.core.logic.database.manager.version.DatabaseManagerVersion;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.rest.exception.ApiException;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class CrudSource<K extends Serializable> implements CrudSourceImpl<K>  {

    private Database<K> database;

    public CrudSource(EntityManager entityManager) {
        this.database = new Database<>(entityManager);
    }


    @Override
    public EntityManager getEntityManager() {
        return database.getEntityManager();
    }

    private ApiException buildApiException(LogicBaseException e) throws ApiException {
        return ApiException.transform(e);
    }

    @Override
    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param) throws ApiException {
        return getList(c, param, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.getList(c, param, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws ApiException {
        return getList(c, customFilter, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.getList(c, customFilter, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> T get(Class<T> c, K id) throws ApiException {
        return get(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> T get(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            T entity = database.get(c, id, dbmCore);
            if(entity != null){
                return entity;
            } else {
                throw new DatabaseException(LogicBaseException.Metadata.ENTITY_DOES_NOT_EXISTS);
            }
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> T create(T newEntity) throws ApiException {
        return create(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> T create(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.create(newEntity, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> T update(T newEntity) throws ApiException {
        return update(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> T update(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.update(newEntity, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> T patch(T newEntity) throws ApiException {
        return patch(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> T patch(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.patch(newEntity, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> T delete(Class<T> c, K id) throws ApiException {
        return delete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> T delete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.delete(c, id, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id) throws ApiException {
        return toggleIsDeleted(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.toggleIsDeleted(c, id, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id) throws ApiException {
        return permDelete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        try {
            return database.permDelete(c, id, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion) throws ApiException {
        return createVersion(newEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException {
        try {
            return database.createVersion(newEntityVersion, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion) throws ApiException {
        return updateVersion(newBaseEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, K>> T updateVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException {
        try {
            return database.updateVersion(newEntityVersion, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }

    @Override
    public <T extends BaseEntityVersion<T, K>> T patchVersion(T newEntityVersion) throws ApiException {
        return patchVersion(newEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, K>> T patchVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException {
        try {
            return database.patchVersion(newEntityVersion, dbmCore);
        } catch (DatabaseException e) {
            throw buildApiException(e);
        }
    }
}
