package si.fri.smrpo.kis.core.logic.database.instance.base;

import org.jinq.jpa.JPAJinqStream;
import si.fri.smrpo.kis.core.jpa.BaseEntity;

import javax.persistence.EntityManager;

public interface DatabaseBaseImpl {

    EntityManager getEntityManager();
    <T extends BaseEntity> JPAJinqStream<T> stream(Class<T> c);

}
