package si.fri.smrpo.kis.app.server.ejb.managers;

import si.fri.smrpo.kis.app.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;
import si.fri.smrpo.kis.core.businessLogic.dto.Paging;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;

@PermitAll
@Stateless
@Local(UserAccountManagerLocal.class)
public class UserAccountManager implements UserAccountManagerLocal{

    @EJB
    DatabaseServiceLocal database;

    public UserAccount login(AuthEntity authEntity) throws BusinessLogicTransactionException {

        final String authId = authEntity.getId();
        List<UserAccount> uaList = database.getStream(UserAccount.class)
                .where(e -> e.getId().equals(authId))
                .toList();

        UserAccount ua;
        if(uaList.size() == 0){
            ua = buildUserAccount(authEntity);

            ua = database.create(ua, null, null);

        } else {
            ua = uaList.get(0);
        }

        return ua;

    }

    private UserAccount buildUserAccount(AuthEntity ae){
        UserAccount ua = new UserAccount();

        ua.setId(UUID.fromString(ae.getId()));
        ua.setEmail(ae.getEmail());
        ua.setName(ae.getName());
        ua.setSurname(ae.getSurname());

        return ua;
    }

}
