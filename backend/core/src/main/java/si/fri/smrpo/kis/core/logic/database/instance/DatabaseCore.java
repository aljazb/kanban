package si.fri.smrpo.kis.core.logic.database.instance;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import com.github.tfaga.lynx.utils.JPAUtils;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.DatabaseBase;
import si.fri.smrpo.kis.core.logic.database.instance.interfaces.DatabaseCoreImpl;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public abstract class DatabaseCore<I extends Serializable> extends DatabaseBase implements DatabaseCoreImpl<I> {

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


    public <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param) throws DatabaseException {
        return getList(c, param,null);
    }

    public <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param, DatabaseManager<E, I> dbmCore) throws DatabaseException {
        try{
            CriteriaFilter<E> customFilter = null;
            if(dbmCore != null) {
                customFilter = dbmCore.authCriteria(this, c);
            }

            List<E> items = JPAUtils.queryEntities(entityManager, c, param, customFilter);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, param, customFilter);

            return new Paging<E>(items, count);
        } catch (Exception e){
            throw new DatabaseException("Exception during query.", e);
        }
    }


    public <E extends BaseEntity<E, I>> E get(Class<E> c, I id) throws DatabaseException {
        return get(c, id, null);
    }

    public <E extends BaseEntity<E, I>> E get(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException {
        try {
            E entity = entityManager.find(c, id);

            if(entity != null && dbmCore != null) dbmCore.authGet(this, entity);

            return entity;
        } catch (DatabaseException e){
            throw e;
        } catch (Exception e){
            throw new DatabaseException(String.format("Error finding entity with id: %s", id.toString()) , e,
                    LogicBaseException.Metadata.ENTITY_DOES_NOT_EXISTS);
        }
    }


    public  <E extends BaseEntity<E, I>> E create(E newEntity) throws DatabaseException {
        return create(newEntity, null);
    }

    public  <E extends BaseEntity<E, I>> E create(E newEntity, DatabaseManager<E, I> dbmCore) throws DatabaseException {
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



    public <E extends BaseEntity<E, I>> E update(E newEntity) throws DatabaseException {
        return update(newEntity, null);
    }

    public <E extends BaseEntity<E, I>> E update(E newEntity, DatabaseManager<E, I> dbmCore) throws DatabaseException {
        try {
            Class<E> c = (Class<E>) newEntity.getClass();

            E dbEntity = get(c, newEntity.getId(), dbmCore);

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



    public <E extends BaseEntity<E, I>> E patch(E newEntity) throws DatabaseException {
        return patch(newEntity, null);
    }

    public <E extends BaseEntity<E, I>> E patch(E newEntity, DatabaseManager<E, I> dbmCore) throws DatabaseException {
        try {
            Class<E> c = (Class<E>) newEntity.getClass();

            E dbEntity = get(c, newEntity.getId(), dbmCore);

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



    public <E extends BaseEntity<E, I>> E delete(Class<E> c, I id) throws DatabaseException {
        return delete(c, id, null);
    }

    public <E extends BaseEntity<E, I>> E delete(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException {
        try {
            E dbEntity = get(c, id, dbmCore);

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



    public <E extends BaseEntity<E, I>> E toggleIsDeleted(Class<E> c, I id) throws DatabaseException {
        return toggleIsDeleted(c, id, null);
    }

    public <E extends BaseEntity<E, I>> E toggleIsDeleted(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException {
        try {
            E dbEntity = get(c, id, dbmCore);

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



    public <E extends BaseEntity<E, I>> E permDelete(Class<E> c, I id) throws DatabaseException {
        return permDelete(c, id, null);
    }

    public <E extends BaseEntity<E, I>> E permDelete(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException {
        try {
            E dbEntity = get(c, id, dbmCore);

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
