package si.fri.smrpo.kis.core.logic.database.interfaces;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;

import java.io.Serializable;


public interface DatabaseGetImpl<I extends Serializable> extends DatabaseBaseImpl {

    <E extends BaseEntity<E, I>> E find(Class<E> c, I id) throws DatabaseException;
    <E extends BaseEntity<E, I>> E get(Class<E> c, I id) throws DatabaseException;

    <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param) throws DatabaseException;
    <E extends BaseEntity<E, I>> Paging<E> getList(Class<E> c, QueryParameters param, CriteriaFilter<E> customFilter, boolean distinct) throws DatabaseException;

}
