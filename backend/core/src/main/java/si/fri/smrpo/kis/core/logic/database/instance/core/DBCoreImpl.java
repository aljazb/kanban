package si.fri.smrpo.kis.core.logic.database.instance.core;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.base.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.base.DBBaseImpl;
import si.fri.smrpo.kis.core.logic.database.manager.core.DBMCore;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;

import java.io.Serializable;


public interface DBCoreImpl<K extends Serializable> extends DBBaseImpl {

    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> T get(Class<T> c, K id) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> T get(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> T create(T newEntity) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> T create(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> T update(T newEntity) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> T update(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> T patch(T newEntity) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> T patch(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> T delete(Class<T> c, K id) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> T delete(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

    <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id) throws BusinessLogicTransactionException;
    <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException;

}
