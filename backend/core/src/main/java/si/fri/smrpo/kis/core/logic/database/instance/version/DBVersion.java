package si.fri.smrpo.kis.core.logic.database.instance.version;

import si.fri.smrpo.kis.core.jpa.base.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.instance.core.DBCore;
import si.fri.smrpo.kis.core.logic.database.manager.version.DBMVersion;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.io.Serializable;

public abstract class DBVersion<K extends Serializable> extends DBCore<K> implements DBVersionImpl<K> {

    public DBVersion() {
    }

    public DBVersion(EntityManager entityManager) {
        super(entityManager);
    }


    public <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion) throws BusinessLogicTransactionException {
        return createVersion(newEntityVersion, null);
    }

    public <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion, DBMVersion<T> dbmVersion) throws BusinessLogicTransactionException {
        try {
            newEntityVersion.update(newEntityVersion, entityManager);

            newEntityVersion.prePersist();

            validateEntity(newEntityVersion);
            if(dbmVersion != null) dbmVersion.validate(this, newEntityVersion);

            entityManager.persist(newEntityVersion);

            newEntityVersion.setOriginId(newEntityVersion.getId());
            entityManager.merge(newEntityVersion);

            return newEntityVersion;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, "Couldn't create object type " + newEntityVersion.getClass().getSimpleName(), e);
        }
    }



    public <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException {
        return updateVersion(newBaseEntityVersion, null);
    }

    public <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion, DBMVersion<T> dbmVersion) throws BusinessLogicTransactionException {
        try {
            final K oldId = newBaseEntityVersion.getId();
            T dbEntityVersion = get((Class<T>) newBaseEntityVersion.getClass(), oldId, dbmVersion);

            if(!dbEntityVersion.getIsLatest()){
                throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, "Couldn't update object type " +
                        dbEntityVersion.getClass().getSimpleName() + " with id: " + dbEntityVersion.getId() + " is not latest");
            }

            if(!dbEntityVersion.isUpdateDifferent(newBaseEntityVersion)){
                logSkip();
                return dbEntityVersion;
            }

            dbEntityVersion.setIsLatest(false);
            entityManager.merge(dbEntityVersion);

            T newEntityVersion = (T) dbEntityVersion.cloneObject();
            newEntityVersion.update(newBaseEntityVersion, entityManager);
            newEntityVersion.prePersist(dbEntityVersion.getVersionOrder() + 1);

            validateEntity(newEntityVersion);
            if(dbmVersion != null) dbmVersion.validate(this, newEntityVersion);

            entityManager.persist(newEntityVersion);

            return newEntityVersion;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }



    public <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException {
        return patchVersion(newBaseEntityVersion, null);
    }

    public <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion, DBMVersion<T> dbmVersion) throws BusinessLogicTransactionException {
        try {
            final K oldId = newBaseEntityVersion.getId();
            T dbEntityVersion = get((Class<T>) newBaseEntityVersion.getClass(), oldId, dbmVersion);

            if(!dbEntityVersion.getIsLatest()){
                throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, "Couldn't update object type " +
                        dbEntityVersion.getClass().getSimpleName() + " with id: " + dbEntityVersion.getId() + " is not latest");
            }

            if(!dbEntityVersion.isPatchDifferent(newBaseEntityVersion)){
                logSkip();
                return dbEntityVersion;
            }

            dbEntityVersion.setIsLatest(false);
            entityManager.merge(dbEntityVersion);

            T newEntityVersion = (T) dbEntityVersion.cloneObject();

            newEntityVersion.patch(newBaseEntityVersion, entityManager);
            newEntityVersion.prePersist(newEntityVersion.getVersionOrder() + 1);

            validateEntity(newEntityVersion);
            if(dbmVersion != null) dbmVersion.validate(this, newEntityVersion);

            entityManager.persist(newEntityVersion);

            return newEntityVersion;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }

}
