package si.fri.smrpo.kis.core.logic.database.instance.base;

import javax.persistence.EntityManager;
import java.util.logging.Logger;

public abstract class DatabaseBase implements DatabaseBaseImpl {

    protected EntityManager entityManager;

    public DatabaseBase() {
    }

    public DatabaseBase(EntityManager entityManager) {
        init(entityManager);
    }

    protected void init(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
