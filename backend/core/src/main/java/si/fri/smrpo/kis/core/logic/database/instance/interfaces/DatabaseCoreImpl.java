package si.fri.smrpo.kis.core.logic.database.instance.interfaces;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;

import java.io.Serializable;


public interface DatabaseCoreImpl<I extends Serializable> extends DatabaseBaseImpl {

    <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param) throws DatabaseException;
    <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param, DatabaseManager<E, I> dbmCore) throws DatabaseException;
    <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param, CriteriaFilter<E> customFilter, boolean requiresDistinct) throws DatabaseException;

    <E extends BaseEntity<E, I>> E get(Class<E> c, I id) throws DatabaseException;
    <E extends BaseEntity<E, I>> E get(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntity<E, I>> E create(E newEntity) throws DatabaseException;
    <E extends BaseEntity<E, I>> E create(E newEntity, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntity<E, I>> E update(E newEntity) throws DatabaseException;
    <E extends BaseEntity<E, I>> E update(E newEntity, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntity<E, I>> E patch(E newEntity) throws DatabaseException;
    <E extends BaseEntity<E, I>> E patch(E newEntity, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntity<E, I>> E delete(Class<E> c, I id) throws DatabaseException;
    <E extends BaseEntity<E, I>> E delete(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntity<E, I>> E toggleIsDeleted(Class<E> c, I id) throws DatabaseException;
    <E extends BaseEntity<E, I>> E toggleIsDeleted(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException;

    <E extends BaseEntity<E, I>> E permDelete(Class<E> c, I id) throws DatabaseException;
    <E extends BaseEntity<E, I>> E permDelete(Class<E> c, I id, DatabaseManager<E, I> dbmCore) throws DatabaseException;

}