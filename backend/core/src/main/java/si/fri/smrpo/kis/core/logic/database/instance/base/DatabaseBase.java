package si.fri.smrpo.kis.core.logic.database.instance.base;

import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import si.fri.smrpo.kis.core.jpa.BaseEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.logging.Logger;

public abstract class DatabaseBase implements DatabaseBaseImpl {

    protected EntityManager entityManager;

    private JinqJPAStreamProvider source;

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

    public <T extends BaseEntity> JPAJinqStream<T> stream(Class<T> c){
        if(source == null){
            source = new JinqJPAStreamProvider(entityManager.getMetamodel());
        }
        return source.streamAll(entityManager, c);
    }

}
