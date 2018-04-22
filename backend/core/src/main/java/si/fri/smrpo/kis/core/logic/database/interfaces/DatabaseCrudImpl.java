package si.fri.smrpo.kis.core.logic.database.interfaces;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;

import java.io.Serializable;


public interface DatabaseCrudImpl<I extends Serializable> extends DatabaseGetImpl<I> {


    <E extends BaseEntity<E, I>> E create(E entity) throws DatabaseException;
    <E extends BaseEntity<E, I>> E update(E entity) throws DatabaseException;
    <E extends BaseEntity<E, I>> E patch(E entity) throws DatabaseException;
    <E extends BaseEntity<E, I>> E delete(Class<E> c, I id) throws DatabaseException;
    <E extends BaseEntity<E, I>> E toggleIsDeleted(Class<E> c, I id) throws DatabaseException;
    <E extends BaseEntity<E, I>> E permDelete(Class<E> c, I id) throws DatabaseException;

}
