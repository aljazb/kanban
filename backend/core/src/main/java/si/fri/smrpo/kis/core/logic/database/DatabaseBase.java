package si.fri.smrpo.kis.core.logic.database;

import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseBaseImpl;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;

@PermitAll
public abstract class DatabaseBase implements DatabaseBaseImpl {

    protected EntityManager entityManager;
    protected JinqJPAStreamProvider source;


    public DatabaseBase(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected void setEntityManager(EntityManager entityManager) {
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
