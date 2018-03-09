package si.fri.smrpo.kis.app.server.ejb.database;

import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import org.jinq.jpa.JPAJinqStream;
import si.fri.smrpo.kis.core.businessLogic.database.AuthorizationManager;
import si.fri.smrpo.kis.core.businessLogic.database.Database;
import si.fri.smrpo.kis.core.businessLogic.database.ValidationManager;
import si.fri.smrpo.kis.core.businessLogic.dto.Paging;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;
import java.util.logging.Logger;

@PermitAll
@Stateless
@Local(DatabaseServiceLocal.class)
public class DatabaseService implements DatabaseServiceLocal {

    private static final Logger LOG = Logger.getLogger(DatabaseService.class.getSimpleName());

    @PersistenceContext(unitName = "kis-jpa")
    private EntityManager em;

    private Database database;

    @PostConstruct
    private void init(){
        this.database = new Database(em);
    }

    @Override
    public <T extends BaseEntity> Paging<T> getList(Class<T> c, QueryParameters param, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException {
        return database.getList(c, param, authorizationManager);
    }

    @Override
    public <T extends BaseEntity> Paging<T> getList(Class<T> c, CriteriaFilter<T> customFilter, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException {
        return database.getList(c, customFilter, authorizationManager);
    }

    @Override
    public <T extends BaseEntity> T get(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager) throws BusinessLogicTransactionException {
        return database.get(c, id, authorizationManager);
    }

    @Override
    public <T extends BaseEntity> T create(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.create(newEntity, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntity> T update(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.update(newEntity, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntity> T patch(T newEntity, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.patch(newEntity, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntity> T delete(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.delete(c, id, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntity> T toggleIsDeleted(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.toggleIsDeleted(c, id, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntity> T permDelete(Class<T> c, UUID id, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.permDelete(c, id, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntityVersion> T createVersion(T newEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.createVersion(newEntityVersion, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntityVersion> T updateVersion(UUID oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.updateVersion(oldId, newBaseEntityVersion, authorizationManager, validationManager);
    }

    @Override
    public <T extends BaseEntityVersion> T patchVersion(UUID oldId, T newBaseEntityVersion, AuthorizationManager<T> authorizationManager, ValidationManager<T> validationManager) throws BusinessLogicTransactionException {
        return database.patchVersion(oldId, newBaseEntityVersion, authorizationManager, validationManager);
    }

    @Override
    public EntityManager getEntityManager() {
        return database.getEntityManager();
    }

    @Override
    public <U extends BaseEntity> JPAJinqStream<U> getStream(Class<U> entity) {
        return null;
    }

    @Override
    public Database getDatabase() {
        return database;
    }
}
