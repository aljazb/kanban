package si.fri.smrpo.kis.server.ejb.source;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.logic.database.manager.core.DatabaseManagerCore;
import si.fri.smrpo.kis.core.logic.database.manager.version.DatabaseManagerVersion;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.rest.exception.ApiException;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@PermitAll
@Stateless
@Local(CrudSourceServiceLocal.class)
public class CrudSourceService implements CrudSourceServiceLocal {

    @PersistenceContext(unitName = "kis-jpa")
    private EntityManager em;

    private CrudSource<UUID> crudSource;

    @PostConstruct
    private void init() {
        crudSource = new CrudSource<>(em);
    }


    @Override
    public EntityManager getEntityManager() {
        return crudSource.getEntityManager();
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, QueryParameters param) throws ApiException {
        return getList(c, param, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, QueryParameters param, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.getList(c, param, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws ApiException {
        return getList(c, customFilter, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.getList(c, customFilter, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T get(Class<T> c, UUID id) throws ApiException {
        return get(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T get(Class<T> c, UUID id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.get(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T create(T newEntity) throws ApiException {
        return create(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T create(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.create(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T update(T newEntity) throws ApiException {
        return update(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T update(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.update(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T patch(T newEntity) throws ApiException {
        return patch(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T patch(T newEntity, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.patch(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T delete(Class<T> c, UUID id) throws ApiException {
        return delete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T delete(Class<T> c, UUID id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.delete(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T toggleIsDeleted(Class<T> c, UUID id) throws ApiException {
        return toggleIsDeleted(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T toggleIsDeleted(Class<T> c, UUID id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.toggleIsDeleted(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T permDelete(Class<T> c, UUID id) throws ApiException {
        return permDelete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T permDelete(Class<T> c, UUID id, DatabaseManagerCore<T> dbmCore) throws ApiException {
        return crudSource.permDelete(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T createVersion(T newEntityVersion) throws ApiException {
        return createVersion(newEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T createVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException {
        return crudSource.createVersion(newEntityVersion, dbmCore);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T updateVersion(T newEntityVersion) throws ApiException {
        return updateVersion(newEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T updateVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException {
        return crudSource.updateVersion(newEntityVersion, dbmCore);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T patchVersion(T newEntityVersion) throws ApiException {
        return patchVersion(newEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T patchVersion(T newEntityVersion, DatabaseManagerVersion<T> dbmCore) throws ApiException {
        return crudSource.patchVersion(newEntityVersion, dbmCore);
    }
}
