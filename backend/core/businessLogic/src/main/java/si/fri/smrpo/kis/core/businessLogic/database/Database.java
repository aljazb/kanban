package si.fri.smrpo.kis.core.businessLogic.database;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import com.github.tfaga.lynx.utils.JPAUtils;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import si.fri.smrpo.kis.core.businessLogic.dto.Paging;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database implements DatabaseImpl {
    private static final Logger LOG = Logger.getLogger(Database.class.getSimpleName());

    protected EntityManager entityManager;
    protected JinqJPAStreamProvider source;

    public Database() { }

    public Database(EntityManager entityManager) {
        init(entityManager);
    }

    protected void init(EntityManager entityManager){
        this.entityManager = entityManager;
        this.source = new JinqJPAStreamProvider(entityManager.getMetamodel());
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public  <U extends BaseEntity> JPAJinqStream<U> getStream(Class<U> entity) {
        return source.streamAll(entityManager, entity);
    }

    private void validateEntity(BaseEntity baseEntity) throws BusinessLogicTransactionException, IllegalAccessException {
        String error = baseEntity.isValidDatabaseItem();
        if(error != null){
            throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, error);
        }
    }

    private void logSkip(){
        LOG.log(Level.INFO,"Skipped update, new entity had same values.");
    }

    /**
     * Example:
     * QueryParameters param = QueryParameters.query(uriInfo.getRequestUri().getQuery())
     * .defaultLimit(50).defaultOffset(0).build();
     *
     * @param c
     * @param param
     * @param <T>
     * @return
     * @throws BusinessLogicTransactionException
     */
    public <T extends BaseEntity> Paging<T> getList(Class<T> c, QueryParameters param) throws BusinessLogicTransactionException{
        return getList(c, param, null);
    }
    public <T extends BaseEntity> Paging<T> getList(Class<T> c, QueryParameters param, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException {
        try{
            if(authorizationManager != null){
                authorizationManager.setAuthorityFilter(param, this);
            }

            List<T> items = JPAUtils.queryEntities(entityManager, c, param);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, param);

            return new Paging<T>(items, count);
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e) {
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not process request.", e);
        }
    }


    public <T extends BaseEntity> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws BusinessLogicTransactionException{
        return getList(c, customFilter, null);
    }
    public <T extends BaseEntity> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException {
        try{
            if(authorizationManager != null){
                authorizationManager.setAuthorityCriteria(customFilter, this);
            }

            List<T> items = JPAUtils.queryEntities(entityManager, c, customFilter);
            Long count = JPAUtils.queryEntitiesCount(entityManager, c, customFilter);

            return new Paging<T>(items, count);
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not process custom filter.", e);
        }
    }


    public <T extends BaseEntity> T get(Class<T> c, Integer id) throws BusinessLogicTransactionException {
        return get(c, id, null);
    }
    public <T extends BaseEntity> T get(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException {
        try {
            T  o = entityManager.find(c, id);

            if(o == null){
                throw new BusinessLogicTransactionException(Response.Status.NOT_FOUND,
                        "Could not find " + c.getClass().getSimpleName() + " with id: " + id);
            }

            if(authorizationManager != null) {
                authorizationManager.checkAuthority(o, this);
            }

            return o;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR,
                    "Error finding entity with id: " + id, e);
        }
    }


    public  <T extends BaseEntity> T create(T newEntity) throws BusinessLogicTransactionException{
        return create(newEntity, null, null);
    }
    public  <T extends BaseEntity> T create(T newEntity, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return create(newEntity, authorizationManager, null);
    }
    public  <T extends BaseEntity> T create(T newEntity, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return create(newEntity, null, validationManager);
    }
    public  <T extends BaseEntity> T create(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {
            if(authorizationManager != null){
                authorizationManager.setEntityAuthority(newEntity, this);
            }

            newEntity.update(newEntity, entityManager);

            newEntity.prePersist();

            validateEntity(newEntity);
            if(validationManager != null) {
                validationManager.createValidate(newEntity, this);
            }

            entityManager.persist(newEntity);

            return newEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, "Couldn't create object type " + newEntity.getClass().getSimpleName(), e);
        }
    }


    public <T extends BaseEntity> T update(T newEntity) throws BusinessLogicTransactionException{
        return update(newEntity, null, null);
    }
    public <T extends BaseEntity> T update(T newEntity, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return update(newEntity, authorizationManager, null);
    }
    public <T extends BaseEntity> T update(T newEntity, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return update(newEntity, null, validationManager);
    }
    public <T extends BaseEntity> T update(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {

            T dbEntity = get((Class<T>) newEntity.getClass(), newEntity.getId(), authorizationManager);

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
            if(validationManager != null){
                validationManager.updateValidate(newEntity, this);
            }

            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }


    public <T extends BaseEntity> T patch(T newEntity) throws BusinessLogicTransactionException{
        return patch(newEntity, null, null);
    }
    public <T extends BaseEntity> T patch(T newEntity, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return patch(newEntity, authorizationManager, null);
    }
    public <T extends BaseEntity> T patch(T newEntity, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return patch(newEntity, null, validationManager);
    }
    public <T extends BaseEntity> T patch(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get((Class<T>) newEntity.getClass(), newEntity.getId(), authorizationManager);

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
            if(validationManager != null){
                validationManager.patchValidate(newEntity, this);
            }

            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }


    public <T extends BaseEntity> T delete(Class<T> c, Integer id) throws BusinessLogicTransactionException{
        return delete(c, id, null, null);
    }
    public <T extends BaseEntity> T delete(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return delete(c, id, authorizationManager, null);
    }
    public <T extends BaseEntity> T delete(Class<T> c, Integer id, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return delete(c, id, null, validationManager);
    }
    public <T extends BaseEntity> T delete(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get(c, id, authorizationManager);

            if(validationManager != null){
                validationManager.deleteValidate(dbEntity, this);
            }

            dbEntity.setIsDeleted(true);
            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }


    public <T extends BaseEntity> T toggleIsDeleted(Class<T> c, Integer id) throws BusinessLogicTransactionException{
        return toggleIsDeleted(c, id, null, null);
    }
    public <T extends BaseEntity> T toggleIsDeleted(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return toggleIsDeleted(c, id, authorizationManager, null);
    }
    public <T extends BaseEntity> T toggleIsDeleted(Class<T> c, Integer id, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return toggleIsDeleted(c, id, null, validationManager);
    }
    public <T extends BaseEntity> T toggleIsDeleted(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get(c, id, authorizationManager);

            if(validationManager != null){
                validationManager.toggleDeleteValidate(dbEntity, this);
            }

            dbEntity.setIsDeleted(!dbEntity.getIsDeleted());
            entityManager.merge(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }


    public <T extends BaseEntity> T permDelete(Class<T> c, Integer id) throws BusinessLogicTransactionException{
        return permDelete(c, id, null, null);
    }
    public <T extends BaseEntity> T permDelete(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return permDelete(c, id, authorizationManager, null);
    }
    public <T extends BaseEntity> T permDelete(Class<T> c, Integer id, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return permDelete(c, id, null, validationManager);
    }
    public <T extends BaseEntity> T permDelete(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {
            T dbEntity = get(c, id, authorizationManager);

            if(validationManager != null){
                validationManager.permDeleteValidate(dbEntity, this);
            }

            entityManager.remove(dbEntity);

            return dbEntity;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }


    public <T extends BaseEntityVersion> T createVersion(T newEntityVersion) throws BusinessLogicTransactionException{
        return createVersion(newEntityVersion, null, null);
    }
    public <T extends BaseEntityVersion> T createVersion(T newEntityVersion, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return createVersion(newEntityVersion, authorizationManager, null);
    }
    public <T extends BaseEntityVersion> T createVersion(T newEntityVersion, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return createVersion(newEntityVersion, null, validationManager);
    }
    public <T extends BaseEntityVersion> T createVersion(T newEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {

        try {
            if(authorizationManager != null){
                authorizationManager.setEntityAuthority(newEntityVersion, this);
            }

            newEntityVersion.update(newEntityVersion, entityManager);

            newEntityVersion.prePersist();

            validateEntity(newEntityVersion);
            if(validationManager != null){
                validationManager.createValidate(newEntityVersion, this);
            }

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


    public <T extends BaseEntityVersion> T updateVersion(int oldId, T newBaseEntityVersion) throws BusinessLogicTransactionException{
        return updateVersion(oldId, newBaseEntityVersion, null, null);
    }
    public <T extends BaseEntityVersion> T updateVersion(int oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return updateVersion(oldId, newBaseEntityVersion, authorizationManager, null);
    }
    public <T extends BaseEntityVersion> T updateVersion(int oldId, T newBaseEntityVersion, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return updateVersion(oldId, newBaseEntityVersion, null, validationManager);
    }
    public <T extends BaseEntityVersion> T updateVersion(int oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {
            T dbEntityVersion = get((Class<T>) newBaseEntityVersion.getClass(), oldId, authorizationManager);

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
            if(validationManager != null){
                validationManager.updateValidate(newEntityVersion, this);
            }

            entityManager.persist(newEntityVersion);

            return newEntityVersion;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }


    public <T extends BaseEntityVersion> T patchVersion(int oldId, T newBaseEntityVersion) throws BusinessLogicTransactionException{
        return patchVersion(oldId, newBaseEntityVersion, null, null);
    }
    public <T extends BaseEntityVersion> T patchVersion(int oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException{
        return patchVersion(oldId, newBaseEntityVersion, authorizationManager, null);
    }
    public <T extends BaseEntityVersion> T patchVersion(int oldId, T newBaseEntityVersion, ValidationManager<T> validationManager) throws BusinessLogicTransactionException{
        return patchVersion(oldId, newBaseEntityVersion, null, validationManager);
    }
    public <T extends BaseEntityVersion> T patchVersion(int oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        try {
            T dbEntityVersion = get((Class<T>) newBaseEntityVersion.getClass(), oldId, authorizationManager);

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
            if(validationManager != null){
                validationManager.patchValidate(newEntityVersion, this);
            }

            entityManager.persist(newEntityVersion);

            return newEntityVersion;
        } catch (BusinessLogicTransactionException e){
            throw e;
        } catch (Exception e){
            throw new BusinessLogicTransactionException(Response.Status.INTERNAL_SERVER_ERROR, "Could not update entity generically", e);
        }
    }

}
