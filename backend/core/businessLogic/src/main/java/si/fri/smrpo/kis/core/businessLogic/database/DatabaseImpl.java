package si.fri.smrpo.kis.core.businessLogic.database;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import org.jinq.jpa.JPAJinqStream;
import si.fri.smrpo.kis.core.businessLogic.dto.Paging;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;

import javax.persistence.EntityManager;

public interface DatabaseImpl {

    <T extends BaseEntity> Paging<T> getList(Class<T> c, QueryParameters param, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T get(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException;

    <T extends BaseEntity> T create(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T update(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T patch(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T delete(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T toggleIsDeleted(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntity> T permDelete(Class<T> c, Integer id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion> T createVersion(T newEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion> T updateVersion(int oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;
    <T extends BaseEntityVersion> T patchVersion(int oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException;

    EntityManager getEntityManager();
    <U extends BaseEntity> JPAJinqStream<U> getStream(Class<U> entity);

}
