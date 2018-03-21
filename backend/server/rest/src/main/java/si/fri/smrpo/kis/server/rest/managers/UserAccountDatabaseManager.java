package si.fri.smrpo.kis.server.rest.managers;

import si.fri.smrpo.kis.core.logic.database.instance.core.DatabaseCore;
import si.fri.smrpo.kis.core.logic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public class UserAccountDatabaseManager extends DatabaseManager<UserAccount> {

    private UserAccount authEntity;

    public UserAccountDatabaseManager(UserAccount authEntity) {
        this.authEntity = authEntity;
    }

    /*@Override
    public void authQuery(DatabaseCore dbCore, Class<UserAccount> c, QueryParameters param) throws DatabaseException {
        QueryFilter filter = new QueryFilter("id", FilterOperation.EQ, authEntity.getId().toString());
        QueryParametersUtility.addParam(param.getFilters(), filter);
    }*/


    @Override
    public void authGet(DatabaseCore db, UserAccount entity) throws DatabaseException {
        if(!entity.getId().equals(authEntity.getId())) {
            throw new DatabaseException("UserAccount does not have permission.",
                    LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
        }
    }
}
