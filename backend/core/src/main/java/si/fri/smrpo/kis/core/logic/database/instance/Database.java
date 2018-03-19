package si.fri.smrpo.kis.core.logic.database.instance;

import si.fri.smrpo.kis.core.logic.database.instance.version.DatabaseVersion;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class Database<K extends Serializable> extends DatabaseVersion<K> implements DatabaseImpl<K> {

    public Database() {
    }

    public Database(EntityManager entityManager) {
        super(entityManager);
    }

}
