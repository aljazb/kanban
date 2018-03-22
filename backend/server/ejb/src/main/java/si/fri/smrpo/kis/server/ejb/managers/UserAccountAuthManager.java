package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;


public class UserAccountAuthManager extends AuthManager<UserAccount> {

    public UserAccountAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public void authGet(DatabaseCore db, UserAccount entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)){
            if(!entity.getId().equals(getUserId())) {
                throw new DatabaseException("User does not have permission.",
                        LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
            }
        }
    }

}
