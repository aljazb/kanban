package si.fri.smrpo.kis.server.ejb.database;

import org.jinq.jpa.JPAJinqStream;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.instance.Database;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.jpa.BaseEntityVersion;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;

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
        database = new Database<>(em);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T createVersion(T newEntityVersion) throws DatabaseException {
        return createVersion(newEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T createVersion(T newEntityVersion, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.createVersion(newEntityVersion, dbmCore);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T updateVersion(T newBaseEntityVersion) throws DatabaseException {
        return updateVersion(newBaseEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T updateVersion(T newBaseEntityVersion, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.updateVersion(newBaseEntityVersion, dbmCore);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T patchVersion(T newBaseEntityVersion) throws DatabaseException {
        return patchVersion(newBaseEntityVersion, null);
    }

    @Override
    public <T extends BaseEntityVersion<T, UUID>> T patchVersion(T newBaseEntityVersion, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.patchVersion(newBaseEntityVersion, dbmCore);
    }


    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, QueryParameters param) throws DatabaseException {
        return getList(c, param, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, QueryParameters param, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.getList(c, param, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> Paging<T> getList(Class<T> c, QueryParameters param, CriteriaFilter<T> customFilter, boolean distinct) throws DatabaseException {
        return database.getList(c, param, customFilter, distinct);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T get(Class<T> c, UUID id) throws DatabaseException {
        return get(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T get(Class<T> c, UUID id, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.get(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T create(T newEntity) throws DatabaseException {
        return create(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T create(T newEntity, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.create(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T update(T newEntity) throws DatabaseException {
        return update(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T update(T newEntity, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.update(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T patch(T newEntity) throws DatabaseException {
        return patch(newEntity, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T patch(T newEntity, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.patch(newEntity, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T delete(Class<T> c, UUID id) throws DatabaseException {
        return delete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T delete(Class<T> c, UUID id, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.delete(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T toggleIsDeleted(Class<T> c, UUID id) throws DatabaseException {
        return toggleIsDeleted(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T toggleIsDeleted(Class<T> c, UUID id, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.toggleIsDeleted(c, id, dbmCore);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T permDelete(Class<T> c, UUID id) throws DatabaseException {
        return permDelete(c, id, null);
    }

    @Override
    public <T extends BaseEntity<T, UUID>> T permDelete(Class<T> c, UUID id, DatabaseManager<T, UUID> dbmCore) throws DatabaseException {
        return database.permDelete(c, id, dbmCore);
    }


    @Override
    public EntityManager getEntityManager() {
        return database.getEntityManager();
    }

    @Override
    public <T extends BaseEntity> JPAJinqStream<T> stream(Class<T> c) {
        return database.stream(c);
    }

}
