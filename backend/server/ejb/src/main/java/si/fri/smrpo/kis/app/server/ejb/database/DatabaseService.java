package si.fri.smrpo.kis.app.server.ejb.database;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import org.jinq.jpa.JPAJinqStream;
import si.fri.smrpo.kis.core.jpa.base.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.Database;
import si.fri.smrpo.kis.core.logic.database.manager.core.DBMCore;
import si.fri.smrpo.kis.core.logic.database.manager.version.DBMVersion;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.base.BaseEntityVersion;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@PermitAll
@Stateless
@Local(DatabaseServiceLocal.class)
public class DatabaseService implements DatabaseServiceLocal {

    @PersistenceContext(unitName = "kis-jpa")
    private EntityManager em;

    private Database<UUID> database;

    @PostConstruct
    private void init() {
        database = new Database<UUID>(em);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T createVersion(T newEntityVersion) throws BusinessLogicTransactionException {
        return createVersion(newEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T createVersion(T newEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException {
        return database.createVersion(newEntityVersion, dbmCore);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T updateVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException {
        return updateVersion(newBaseEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T updateVersion(T newBaseEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException {
        return database.updateVersion(newBaseEntityVersion, dbmCore);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T patchVersion(T newBaseEntityVersion) throws BusinessLogicTransactionException {
        return patchVersion(newBaseEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T patchVersion(T newBaseEntityVersion, DBMVersion<T> dbmCore) throws BusinessLogicTransactionException {
        return database.patchVersion(newBaseEntityVersion, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, QueryParameters param) throws BusinessLogicTransactionException {
        return getList(c, param, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, QueryParameters param, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.getList(c, param, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter) throws BusinessLogicTransactionException {
        return getList(c, customFilter, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.getList(c, customFilter, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T get(Class<T> c, UUID id) throws BusinessLogicTransactionException {
        return get(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T get(Class<T> c, UUID id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.get(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T create(T newEntity) throws BusinessLogicTransactionException {
        return create(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T create(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.create(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T update(T newEntity) throws BusinessLogicTransactionException {
        return update(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T update(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return update(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T patch(T newEntity) throws BusinessLogicTransactionException {
        return patch(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T patch(T newEntity, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.patch(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T delete(Class<T> c, UUID id) throws BusinessLogicTransactionException {
        return delete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T delete(Class<T> c, UUID id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.delete(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T toggleIsDeleted(Class<T> c, UUID id) throws BusinessLogicTransactionException {
        return toggleIsDeleted(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T toggleIsDeleted(Class<T> c, UUID id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.toggleIsDeleted(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T permDelete(Class<T> c, UUID id) throws BusinessLogicTransactionException {
        return permDelete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T permDelete(Class<T> c, UUID id, DBMCore<T> dbmCore) throws BusinessLogicTransactionException {
        return database.permDelete(c, id, dbmCore);
    }


    @Override
    public EntityManager getEntityManager() {
        return database.getEntityManager();
    }

    @Override
    public <U extends BaseEntity> JPAJinqStream<U> getStream(Class<U> entity) {
        return database.getStream(entity);
    }
}
