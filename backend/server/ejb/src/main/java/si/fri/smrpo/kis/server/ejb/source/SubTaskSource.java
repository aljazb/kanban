package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.SubTaskSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.SubTask;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.UUID;

@PermitAll
@Stateless
@Local(SubTaskSourceLocal.class)
public class SubTaskSource extends CrudSource<SubTask, UUID, UserAccount> implements SubTaskSourceLocal {

    @EJB
    private DatabaseServiceLocal databaseService;

    @PostConstruct
    private void init() {
        setDatabase(databaseService);
    }

}
