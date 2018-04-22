package si.fri.smrpo.kis.core.logic.database;

import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseVersionImpl;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import java.io.Serializable;

@PermitAll
public abstract class DatabaseVersion<I extends Serializable> extends DatabaseCrud<I> implements DatabaseVersionImpl<I> {


    public DatabaseVersion(EntityManager entityManager) {
        super(entityManager);
    }


    public <E extends BaseEntityVersion<E, I>> E createVersion(E entity) throws DatabaseException {
        try {
            E dbEntity = entity.cloneObject(entityManager);

            dbEntity.update(entity, entityManager);

            dbEntity.prePersist();

            validateEntity(dbEntity);

            entityManager.persist(dbEntity);
            dbEntity.setOriginId(dbEntity.getId());

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception creating entity", e);
        }
    }

    public <E extends BaseEntityVersion<E, I>> E updateVersion(E entity) throws DatabaseException {
        try {
            Class<E> c = entity.getType();
            E dbEntity = get(c, entity.getId());

            if(!dbEntity.getIsLatest()){
                throw new DatabaseException("Couldn't update object type " +
                        dbEntity.getClass().getSimpleName() + " with id: " + dbEntity.getId() + " is not latest");
            }

            if(!dbEntity.isUpdateDifferent(entity)){
                return dbEntity;
            }

            dbEntity.setIsLatest(false);

            E dbEntityClone = dbEntity.cloneObject(entityManager);
            dbEntityClone.update(entity, entityManager);
            dbEntityClone.prePersist(dbEntityClone.getVersionOrder() + 1);

            validateEntity(dbEntityClone);

            entityManager.persist(dbEntityClone);

            return dbEntityClone;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception updating entity", e);
        }
    }

    public <E extends BaseEntityVersion<E, I>> E patchVersion(E entity) throws DatabaseException {
        try {
            Class<E> c = entity.getType();
            E dbEntity = get(c, entity.getId());

            if(!dbEntity.getIsLatest()){
                throw new DatabaseException("Couldn't update object type " +
                        dbEntity.getClass().getSimpleName() + " with id: " + dbEntity.getId() + " is not latest");
            }

            if(!dbEntity.isPatchDifferent(entity)) {
                return dbEntity;
            }

            dbEntity.setIsLatest(false);

            E dbEntityClone = dbEntity.cloneObject(entityManager);
            dbEntityClone.patch(entity, entityManager);
            dbEntityClone.prePersist(dbEntityClone.getVersionOrder() + 1);

            validateEntity(dbEntityClone);

            entityManager.persist(dbEntityClone);

            return dbEntityClone;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception patching entity.", e);
        }
    }

}
