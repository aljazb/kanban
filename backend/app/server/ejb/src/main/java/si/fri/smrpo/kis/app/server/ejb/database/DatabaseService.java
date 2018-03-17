package si.fri.smrpo.kis.app.server.ejb.database;

import si.fri.smrpo.kis.core.businessLogic.database.instance.Database;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@PermitAll
@Stateless
@Local(DatabaseServiceLocal.class)
public class DatabaseService extends Database<UUID> implements DatabaseServiceLocal {

    @PersistenceContext(unitName = "kis-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        init(em);
    }

}
