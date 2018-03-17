package si.fri.smrpo.kis.core.businessLogic.database.instance;

import si.fri.smrpo.kis.core.businessLogic.database.instance.version.DBVersion;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class Database<K extends Serializable> extends DBVersion<K> implements DatabaseImpl<K> {

    public Database() {
    }

    public Database(EntityManager entityManager) {
        super(entityManager);
    }

}
