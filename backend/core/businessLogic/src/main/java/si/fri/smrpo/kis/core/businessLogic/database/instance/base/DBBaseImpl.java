package si.fri.smrpo.kis.core.businessLogic.database.instance.base;

import org.jinq.jpa.JPAJinqStream;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.EntityManager;

public interface DBBaseImpl {

    EntityManager getEntityManager();
    <U extends BaseEntity> JPAJinqStream<U> getStream(Class<U> entity);

}
