package si.fri.smrpo.kis.core.rest.source;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.manager.core.DatabaseManagerCore;
import si.fri.smrpo.kis.core.logic.database.manager.version.DatabaseManagerVersion;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.rest.exception.ApiException;

import javax.persistence.EntityManager;
import java.io.Serializable;

public interface CrudSourceImpl<K extends Serializable> {

    EntityManager getEntityManager();

    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param) throws ApiException;
    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, QueryParameters param, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws ApiException;
    <T extends BaseEntity<T, K>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> T get(Class<T> c, K id) throws ApiException;
    <T extends BaseEntity<T, K>> T get(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> T create(T newEntity) throws ApiException;
    <T extends BaseEntity<T, K>> T create(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> T update(T newEntity) throws ApiException;
    <T extends BaseEntity<T, K>> T update(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> T patch(T newEntity) throws ApiException;
    <T extends BaseEntity<T, K>> T patch(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> T delete(Class<T> c, K id) throws ApiException;
    <T extends BaseEntity<T, K>> T delete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id) throws ApiException;
    <T extends BaseEntity<T, K>> T toggleIsDeleted(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id) throws ApiException;
    <T extends BaseEntity<T, K>> T permDelete(Class<T> c, K id, DatabaseManagerCore<T> dbmCore) throws ApiException;

    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion) throws ApiException;
    <T extends BaseEntityVersion<T, K>> T createVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException;

    <T extends BaseEntityVersion<T, K>> T updateVersion(T newEntityVersion) throws ApiException;
    <T extends BaseEntityVersion<T, K>> T updateVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException;

    <T extends BaseEntityVersion<T, K>> T patchVersion(T newEntityVersion) throws ApiException;
    <T extends BaseEntityVersion<T, K>> T patchVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException;

}
