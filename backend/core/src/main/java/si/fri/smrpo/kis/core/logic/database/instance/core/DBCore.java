package si.fri.smrpo.kis.core.logic.database.instance.core;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import com.github.tfaga.lynx.utils.JPAUtils;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.base.DBBase;
import si.fri.smrpo.kis.core.logic.database.manager.core.DBMCore;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

public abstract class DBCore<K extends Serializable> extends DBBase implements DBCoreImpl<K> {

    public DBCore() {
    }

    public DBCore(EntityManager entityManager) {
        super(entityManager);
    }


    protected void logSkip(){
        LOG.log(Level.INFO,"Skipped update, new entity had same values.");
    }

    protected void validateEntity(BaseEntity baseEntity) throws BusinessLogicTransactionException, IllegalAccessException {
        String error = baseEntity.isValidDatabaseItem();
        if(error != null){
            throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, error);
        }
    }


    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param) throws BusinessLogicTransactionException {
        return getList(c, param, null);
    }

    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            if(dbmCore != null) dbmCore.authQuery(this, c, param);

            List<T> items = JPAUtils.queryEntities(entityManager, c, param);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, param);

            return new Paging<T>(items, count);
        } catch (Exception e) {
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not process request.", e);
        }
    }



    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws BusinessLogicTransactionException {
        return getList(c, customFilter, null);
    }

    public <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try{
            if(dbmCore != null) dbmCore.authCriteria(this, c, customFilter);

            List<T> items = JPAUtils.queryEntities(entityManager, c, customFilter);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, customFilter);

            return new Paging<T>(items, count);
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not process custom filter.", e);
        }
    }



    public <T extends BaseEntity<T, K>> T get(Class<T> c, K id) throws BusinessLogicTransactionException {
        return get(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T get(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            T entity = entityManager.find(c, id);

            if(entity == null){
                throw new BusinessLogicTransactionException(Response.Status.NOT_FOUND,
                        "Could not find " + c.getClass().getSimpleName() + " with id: " + id.toString());
            }

            if(dbmCore != null) dbmCore.authGet(this, entity);

            return entity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR,
                    "Error finding entity with id: " + id.toString(), e);
        }
    }



    public  <T extends BaseEntity<T, K>> T create(T newEntity) throws BusinessLogicTransactionException {
        return create(newEntity, null);
    }

    public  <T extends BaseEntity<T, K>> T create(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            if(dbmCore != null) dbmCore.authSet(this, newEntity);

            newEntity.update(newEntity, entityManager);

            newEntity.prePersist();

            validateEntity(newEntity);
            if(dbmCore != null) dbmCore.validate(this, newEntity);

            entityManager.persist(newEntity);

            return newEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, "Couldn't create object type " + newEntity.getClass().getSimpleName(), e);
        }
    }



    public <T extends BaseEntity<T, K>> T update(T newEntity) throws BusinessLogicTransactionException {
        return update(newEntity, null);
    }

    public <T extends BaseEntity<T, K>> T update(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get((Class<T>) newEntity.getClass(), newEntity.getId(), dbmCore);

            if(!dbEntity.isUpdateDifferent(newEntity)){
                if(dbEntity.equals(newEntity)){
                    dbEntity.preUpdate();
                } else {
                    logSkip();
                }
                return dbEntity;
            }

            dbEntity.update(newEntity, entityManager);

            newEntity.preUpdate();

            validateEntity(dbEntity);

            if(dbmCore != null) dbmCore.validate(this, newEntity);

            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }



    public <T extends BaseEntity<T, K>> T patch(T newEntity) throws BusinessLogicTransactionException {
        return patch(newEntity, null);
    }

    public <T extends BaseEntity<T, K>> T patch(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get((Class<T>) newEntity.getClass(), newEntity.getId(), dbmCore);

            if(!dbEntity.isPatchDifferent(newEntity)){
                if(dbEntity.equals(newEntity)){
                    dbEntity.preUpdate();
                } else {
                    logSkip();
                }
                return dbEntity;
            }

            dbEntity.patch(newEntity, entityManager);

            newEntity.preUpdate();

            validateEntity(dbEntity);
            if(dbmCore != null) dbmCore.validate(this, newEntity);

            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }



    public <T extends BaseEntity<T, K>> T delete(Class<T> c, K id) throws BusinessLogicTransactionException {
        return delete(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T delete(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get(c, id, dbmCore);

            dbEntity.setIsDeleted(true);
            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }



    public <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id) throws BusinessLogicTransactionException {
        return toggleIsDeleted(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get(c, id, dbmCore);

            dbEntity.setIsDeleted(!dbEntity.getIsDeleted());
            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }



    public <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id) throws BusinessLogicTransactionException {
        return permDelete(c, id, null);
    }

    public <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get(c, id, dbmCore);

            entityManager.remove(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }

}
