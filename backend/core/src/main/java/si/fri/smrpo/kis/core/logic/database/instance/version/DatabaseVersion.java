package si.fri.smrpo.kis.core.logic.database.instance.version;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCore;
import si.fri.smrpo.kis.core.logic.database.manager.version.DatabaseManagerVersion;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import javax.persistence.EntityManager;
import java.io.Serializable;

public abstract class DatabaseVersion<K extends Serializable> extends DatabaseCore<K> implements DatabaseVersionImpl<K> {

    public DatabaseVersion() {
    }

    public DatabaseVersion(EntityManager entityManager) {
        super(entityManager);
    }


    public <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion) throws DatabaseException {
        return createVersion(newEntityVersion, null);
    }

    public <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmVersion) throws DatabaseException {
        try {
            newEntityVersion.update(newEntityVersion, entityManager);

            newEntityVersion.prePersist();

            validateEntity(newEntityVersion);
            if(dbmVersion != null) dbmVersion.validate(this, newEntityVersion);

            entityManager.persist(newEntityVersion);

            newEntityVersion.setOriginId(newEntityVersion.getId());
            entityManager.merge(newEntityVersion);

            return newEntityVersion;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception creating entity", e);
        }
    }



    public <T extends BaseEntityVersion<T, K>> T updateVersion(T newBaseEntityVersion) throws DatabaseException {
        return updateVersion(newBaseEntityVersion, null);
    }

    public <T extends BaseEntityVersion<T, K>> T updateVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmVersion) throws DatabaseException {
        try {
            Class<T> c = (Class<T>) newEntityVersion.getClass();

            T dbEntityVersion = get(c, newEntityVersion.getId(), dbmVersion);

            if(dbEntityVersion == null){
                throw new DatabaseException(String.format("Entity %s with id %s does not exist.",
                        c.getClass().getSimpleName(), newEntityVersion.getId().toString()));
            }

            if(!dbEntityVersion.getIsLatest()){
                throw new DatabaseException("Couldn't update object type " +
                        dbEntityVersion.getClass().getSimpleName() + " with id: " + dbEntityVersion.getId() + " is not latest");
            }

            if(!dbEntityVersion.isUpdateDifferent(newEntityVersion)){
                return dbEntityVersion;
            }

            dbEntityVersion.setIsLatest(false);
            entityManager.merge(dbEntityVersion);

            T dbEntityClone = (T) dbEntityVersion.cloneObject();
            dbEntityClone.update(newEntityVersion, entityManager);
            dbEntityClone.prePersist(dbEntityVersion.getVersionOrder() + 1);

            validateEntity(dbEntityClone);
            if(dbmVersion != null) dbmVersion.validate(this, dbEntityClone);

            entityManager.persist(dbEntityClone);

            return dbEntityClone;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception updating entity", e);
        }
    }



    public <T extends BaseEntityVersion<T, K>> T patchVersion(T newBaseEntityVersion) throws DatabaseException {
        return patchVersion(newBaseEntityVersion, null);
    }

    public <T extends BaseEntityVersion<T, K>> T patchVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmVersion) throws DatabaseException {
        try {
            Class<T> c = (Class<T>) newEntityVersion.getClass();

            T dbEntityVersion = get(c, newEntityVersion.getId(), dbmVersion);

            if(dbEntityVersion == null){
                throw new DatabaseException(String.format("Entity %s with id %s does not exist.",
                        c.getClass().getSimpleName(), newEntityVersion.getId().toString()));
            }

            if(!dbEntityVersion.getIsLatest()){
                throw new DatabaseException("Couldn't update object type " +
                        dbEntityVersion.getClass().getSimpleName() + " with id: " + dbEntityVersion.getId() + " is not latest");
            }

            if(!dbEntityVersion.isPatchDifferent(newEntityVersion)) {
                return dbEntityVersion;
            }

            dbEntityVersion.setIsLatest(false);
            entityManager.merge(dbEntityVersion);

            T dbEntityClone = (T) dbEntityVersion.cloneObject();

            dbEntityClone.patch(newEntityVersion, entityManager);
            dbEntityClone.prePersist(dbEntityClone.getVersionOrder() + 1);

            validateEntity(dbEntityClone);
            if(dbmVersion != null) dbmVersion.validate(this, dbEntityClone);

            entityManager.persist(dbEntityClone);

            return dbEntityClone;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception patching entity.", e);
        }
    }

}
