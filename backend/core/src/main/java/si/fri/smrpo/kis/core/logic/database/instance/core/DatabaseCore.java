package si.fri.smrpo.kis.core.logic.database.instance.core;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import com.github.tfaga.lynx.utils.JPAUtils;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.base.DatabaseBase;
import si.fri.smrpo.kis.core.logic.database.manager.core.DatabaseManagerCore;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public abstract class DatabaseCore<K extends Serializable> extends DatabaseBase implements DatabaseCoreImpl<K> {

    public DatabaseCore() {
    }

    public DatabaseCore(EntityManager entityManager) {
        super(entityManager);
    }

    protected void validateEntity(BaseEntity baseEntity) throws DatabaseException, IllegalAccessException {
        String error = baseEntity.isValidDatabaseItem();
        if(error != null){
            throw new DatabaseException(error);
        }
    }



    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param) throws DatabaseException {
        return getList(c, param, null);
    }

    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            if(dbmCore != null) dbmCore.authQuery(this, c, param);

            List<T> items = JPAUtils.queryEntities(entityManager, c, param);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, param);

            return new Paging<T>(items, count);
        } catch (Exception e) {
            throw new DatabaseException("Exception during query.", e);
        }
    }



    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws DatabaseException {
        return getList(c, customFilter, null);
    }

    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try{
            if(dbmCore != null) dbmCore.authCriteria(this, c, customFilter);

            List<T> items = JPAUtils.queryEntities(entityManager, c, customFilter);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, customFilter);

            return new Paging<T>(items, count);
        } catch (Exception e){
            throw new DatabaseException("Exception during query.", e);
        }
    }


    public <T extends BaseEntity<T, K>> T get(Class<T> c, K id) throws DatabaseException {
        return get(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T get(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            T entity = entityManager.find(c, id);

            if(dbmCore != null) dbmCore.authGet(this, entity);

            return entity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException(String.format("Error finding entity with id: %s", id.toString()) , e,
                    LogicBaseException.Metadata.ENTITY_DOES_NOT_EXISTS);
        }
    }


    public  <T extends BaseEntity<T, K>> T create(T newEntity) throws DatabaseException {
        return create(newEntity, null);
    }

    public  <T extends BaseEntity<T, K>> T create(T newEntity, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            if(dbmCore != null) dbmCore.authSet(this, newEntity);

            newEntity.update(newEntity, entityManager);

            newEntity.prePersist();

            validateEntity(newEntity);
            if(dbmCore != null) dbmCore.validate(this, newEntity);

            entityManager.persist(newEntity);

            return newEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception creating entity.", e);
        }
    }



    public <T extends BaseEntity<T, K>> T update(T newEntity) throws DatabaseException {
        return update(newEntity, null);
    }

    public <T extends BaseEntity<T, K>> T update(T newEntity, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            Class<T> c = (Class<T>) newEntity.getClass();

            T dbEntity = get(c, newEntity.getId(), dbmCore);

            if(dbEntity == null){
                throw new DatabaseException(String.format("Entity %s with id %s does not exist.",
                                c.getClass().getSimpleName(), newEntity.getId().toString()));
            }

            if(!dbEntity.isUpdateDifferent(newEntity)){
                if(dbEntity.equals(newEntity)) dbEntity.preUpdate();
                return dbEntity;
            }

            dbEntity.update(newEntity, entityManager);

            newEntity.preUpdate();

            validateEntity(dbEntity);

            if(dbmCore != null) dbmCore.validate(this, newEntity);

            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Could not update entity generically", e);
        }
    }



    public <T extends BaseEntity<T, K>> T patch(T newEntity) throws DatabaseException {
        return patch(newEntity, null);
    }

    public <T extends BaseEntity<T, K>> T patch(T newEntity, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            Class<T> c = (Class<T>) newEntity.getClass();

            T dbEntity = get(c, newEntity.getId(), dbmCore);

            if(dbEntity == null){
                throw new DatabaseException(String.format("Entity %s with id %s does not exist.",
                        c.getClass().getSimpleName(), newEntity.getId().toString()));
            }

            if(!dbEntity.isPatchDifferent(newEntity)){
                if(dbEntity.equals(newEntity)) dbEntity.preUpdate();
                return dbEntity;
            }

            dbEntity.patch(newEntity, entityManager);

            newEntity.preUpdate();

            validateEntity(dbEntity);
            if(dbmCore != null) dbmCore.validate(this, newEntity);

            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception patching entity.", e);
        }
    }



    public <T extends BaseEntity<T, K>> T delete(Class<T> c, K id) throws DatabaseException {
        return delete(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T delete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            T dbEntity = get(c, id, dbmCore);

            if(dbEntity == null){
                throw new DatabaseException(String.format("Entity %s with id %s does not exist.",
                        c.getClass().getSimpleName(), id.toString()));
            }

            dbEntity.setIsDeleted(true);
            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception deleting entity", e);
        }
    }



    public <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id) throws DatabaseException {
        return toggleIsDeleted(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            T dbEntity = get(c, id, dbmCore);

            if(dbEntity == null){
                throw new DatabaseException(String.format("Entity %s with id %s does not exist.",
                        c.getClass().getSimpleName(), id.toString()));
            }

            dbEntity.setIsDeleted(!dbEntity.getIsDeleted());
            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception activating/deactivating entity.", e);
        }
    }



    public <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id) throws DatabaseException {
        return permDelete(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException {
        try {
            T dbEntity = get(c, id, dbmCore);

            if(dbEntity == null){
                throw new DatabaseException(String.format("Entity %s with id %s does not exist.",
                        c.getClass().getSimpleName(), id.toString()));
            }

            entityManager.remove(dbEntity);

            return dbEntity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException("Exception deleting entity permanently.", e);
        }
    }

}
