package si.fri.smrpo.kis.core.logic.database.instance;

import si.fri.smrpo.kis.core.logic.database.instance.version.DatabaseVersion;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class Database<I extends Serializable> extends DatabaseVersion<I> implements DatabaseImpl<I> {

    public Database() {
    }

    public Database(EntityManager entityManager) {
        super(entityManager);
    }

}
