package si.fri.smrpo.kis.core.businessLogic.database;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import org.jinq.jpa.JPAJinqStream;
import si.fri.smrpo.kis.core.businessLogic.dto.Paging;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;

import javax.persistence.EntityManager;
import java.util.UUID;

public interface DatabaseImpl {

    //<T extends BaseEntity> Paging<T> getList(Class<T> c, QueryParameters param) throws BusinessLogicTransactionException;
    <T extends BaseEntity> Paging<T> getList(Class<T> c, QueryParameters param, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws BusinessLogicTransactionException;
    <T extends BaseEntity> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> T get(Class<T> c, UUID id) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T get(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> T create(T newEntity) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T create(T newEntity, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T create(T newEntity, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T create(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> T update(T newEntity) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T update(T newEntity, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T update(T newEntity, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T update(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> T patch(T newEntity) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T patch(T newEntity, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T patch(T newEntity, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T patch(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> T delete(Class<T> c, UUID id) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T delete(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T delete(Class<T> c, UUID id, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T delete(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> T toggleIsDeleted(Class<T> c, UUID id) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T toggleIsDeleted(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T toggleIsDeleted(Class<T> c, UUID id, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T toggleIsDeleted(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntity> T permDelete(Class<T> c, UUID id) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T permDelete(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntity> T permDelete(Class<T> c, UUID id, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T permDelete(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntityVersion> T createVersion(T newEntityVersion) throws BusinessLogicTransactionException;
    //<T extends BaseEntityVersion> T createVersion(T newEntityVersion, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntityVersion> T createVersion(T newEntityVersion, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion> T createVersion(T newEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;


    //<T extends BaseEntityVersion> T updateVersion(UUID oldId, T newBaseEntityVersion) throws BusinessLogicTransactionException;
    //<T extends BaseEntityVersion> T updateVersion(UUID oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntityVersion> T updateVersion(UUID oldId, T newBaseEntityVersion, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion> T updateVersion(UUID oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    //<T extends BaseEntityVersion> T patchVersion(UUID oldId, T newBaseEntityVersion) throws BusinessLogicTransactionException;
    //<T extends BaseEntityVersion> T patchVersion(UUID oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    //<T extends BaseEntityVersion> T patchVersion(UUID oldId, T newBaseEntityVersion, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion> T patchVersion(UUID oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    EntityManager getEntityManager();
    <U extends BaseEntity> JPAJinqStream<U> getStream(Class<U> entity);

}
