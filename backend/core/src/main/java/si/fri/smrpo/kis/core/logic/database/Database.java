package si.fri.smrpo.kis.core.logic.database;

import si.fri.smrpo.kis.core.logic.database.interfaces.DatabaseImpl;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import java.io.Serializable;

@PermitAll
public class Database<I extends Serializable> extends DatabaseVersion<I> implements DatabaseImpl<I> {

    public Database(EntityManager entityManager) {
        super(entityManager);
    }

    public Database() {
        super(null);
    }


}
