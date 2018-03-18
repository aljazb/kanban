package si.fri.smrpo.kis.app.server.ejb.managers;

import si.fri.smrpo.kis.app.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.server.entities.UserAccount;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;

@PermitAll
@Stateless
@Local(UserAccountManagerLocal.class)
public class UserAccountManager implements UserAccountManagerLocal {

    @EJB
    private DatabaseServiceLocal database;


    public UserAccount login(UserAccount authEntity) throws BusinessLogicTransactionException {

        final UUID authId = authEntity.getId();
        List<UserAccount> uaList = database.getStream(UserAccount.class)
                .where(e -> e.getId().equals(authId))
                .toList();

        UserAccount ua;
        if(uaList.isEmpty()) {
            ua = database.create(authEntity);
        } else {
            ua = uaList.get(0);
        }

        return ua;

    }

}
