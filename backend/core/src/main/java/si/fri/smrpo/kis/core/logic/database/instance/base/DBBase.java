package si.fri.smrpo.kis.core.logic.database.instance.base;

import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import si.fri.smrpo.kis.core.jpa.BaseEntity;

import javax.persistence.EntityManager;
import java.util.logging.Logger;

public abstract class DBBase implements DBBaseImpl {

    protected static final Logger LOG = Logger.getLogger(DBBase.class.getSimpleName());

    protected EntityManager entityManager;
    protected JinqJPAStreamProvider source;

    public DBBase() {
    }

    public DBBase(EntityManager entityManager) {
        init(entityManager);
    }

    protected void init(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public  <U extends BaseEntity> JPAJinqStream<U> getStream(Class<U> entity) {
        if(source == null){
            this.source = new JinqJPAStreamProvider(entityManager.getMetamodel());
        }

        return source.streamAll(entityManager, entity);
    }

}
