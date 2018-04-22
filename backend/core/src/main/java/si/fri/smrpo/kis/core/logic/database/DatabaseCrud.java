package si.fri.smrpo.kis.core.logic.database;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseCrudImpl;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import java.io.Serializable;

@PermitAll
public abstract class DatabaseCrud<I extends Serializable> extends DatabaseGet<I> implements DatabaseCrudImpl<I> {

    public DatabaseCrud(EntityManager entityManager) {
        super(entityManager);
    }

    protected void validateEntity(BaseEntity baseEntity) throws DatabaseException, IllegalAccessException {
        String error = baseEntity.isValidDatabaseItem();
        if(error != null){
            throw new DatabaseException(error);
        }
    }

    public  <E extends BaseEntity<E, I>> E create(E entity) throws DatabaseException {
        try {
            E dbEntity = entity.cloneObject(entityManager);

            dbEntity.prePersist();

            validateEntity(dbEntity);

            entityManager.persist(dbEntity);

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception creating entity.", e);
        }
    }

    public <E extends BaseEntity<E, I>> E update(E entity) throws DatabaseException {
        try {

            E dbEntity;
            if(entityManager.contains(entity)) {
                dbEntity = entity;
            } else {
                Class<E> c = entity.getType();
                dbEntity = get(c, entity.getId());

                dbEntity.update(entity, entityManager);
            }

            validateEntity(dbEntity);
            dbEntity.preUpdate();

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Could not update entity.", e);
        }
    }

    public <E extends BaseEntity<E, I>> E patch(E entity) throws DatabaseException {
        try {

            E dbEntity;
            if(entityManager.contains(entity)) {
                dbEntity = entity;
            } else {
                Class<E> c = entity.getType();
                dbEntity = get(c, entity.getId());

                dbEntity.patch(entity, entityManager);
            }

            validateEntity(dbEntity);
            entity.preUpdate();

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception patching entity.", e);
        }
    }

    public <E extends BaseEntity<E, I>> E delete(Class<E> c, I id) throws DatabaseException {
        try {
            E dbEntity = get(c, id);

            dbEntity.setIsDeleted(true);
            dbEntity.preUpdate();

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception deleting entity", e);
        }
    }

    public <E extends BaseEntity<E, I>> E toggleIsDeleted(Class<E> c, I id) throws DatabaseException {
        try {
            E dbEntity = get(c, id);

            dbEntity.preUpdate();
            dbEntity.setIsDeleted(!dbEntity.getIsDeleted());

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception activating/deactivating entity.", e);
        }
    }

    public <E extends BaseEntity<E, I>> E permDelete(Class<E> c, I id) throws DatabaseException {
        try {
            E dbEntity = get(c, id);

            entityManager.remove(dbEntity);

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception deleting entity permanently.", e);
        }
    }

}
