package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

@PermitAll
@Stateless
@Local(UserAccountManagerLocal.class)
public class UserAccountManager implements UserAccountManagerLocal {

    @EJB
    private DatabaseServiceLocal database;

    public UserAccount login(UserAccount authEntity) throws DatabaseException {

        UserAccount ua = database.get(UserAccount.class, authEntity.getId());

        if(ua == null) {
            ua = database.create(authEntity);
        }

        return ua;
    }
}
