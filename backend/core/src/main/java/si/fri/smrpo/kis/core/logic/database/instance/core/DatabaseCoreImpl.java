package si.fri.smrpo.kis.core.logic.database.instance.core;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.base.DatabaseBaseImpl;
import si.fri.smrpo.kis.core.logic.database.manager.core.DatabaseManagerCore;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import java.io.Serializable;


public interface DatabaseCoreImpl<K extends Serializable> extends DatabaseBaseImpl {

    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param) throws DatabaseException;
    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws DatabaseException;
    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> T get(Class<T> c, K id) throws DatabaseException;
    <T extends BaseEntity<T, K>> T get(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> T create(T newEntity) throws DatabaseException;
    <T extends BaseEntity<T, K>> T create(T newEntity, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> T update(T newEntity) throws DatabaseException;
    <T extends BaseEntity<T, K>> T update(T newEntity, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> T patch(T newEntity) throws DatabaseException;
    <T extends BaseEntity<T, K>> T patch(T newEntity, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> T delete(Class<T> c, K id) throws DatabaseException;
    <T extends BaseEntity<T, K>> T delete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id) throws DatabaseException;
    <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

    <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id) throws DatabaseException;
    <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws DatabaseException;

}
