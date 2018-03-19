package si.fri.smrpo.kis.core.logic.database.instance.base;

import javax.persistence.EntityManager;

public interface DatabaseBaseImpl {

    EntityManager getEntityManager();

}
