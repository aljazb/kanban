package si.fri.smrpo.kis.app.server.rest.resources.managers;

import com.github.tfaga.lynx.beans.QueryFilter;
import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.enums.FilterOperation;
import com.github.tfaga.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.businessLogic.database.instance.core.DBCore;
import si.fri.smrpo.kis.core.businessLogic.database.manager.DatabaseManager;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.restComponents.utility.QueryParametersUtility;

import javax.ws.rs.core.Response;

public class UserAccountDBM extends DatabaseManager<UserAccount> {

    private UserAccount authEntity;

    public UserAccountDBM(UserAccount authEntity) {
        this.authEntity = authEntity;
    }

    @Override
    public void authQuery(DBCore dbCore, Class<UserAccount> c, QueryParameters param) throws BusinessLogicTransactionException {
        QueryFilter filter = new QueryFilter("id", FilterOperation.EQ, authEntity.getId().toString());
        QueryParametersUtility.addParam(param.getFilters(), filter);
    }


    @Override
    public void authGet(DBCore db, UserAccount entity) throws BusinessLogicTransactionException {
        if(!entity.getId().equals(authEntity.getId())){
            throw new BusinessLogicTransactionException(Response.Status.FORBIDDEN, "UserAccount does not have permission.");
        }
    }
}
