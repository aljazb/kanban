package si.fri.smrpo.kis.core.businessLogic.managers;

import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;
import si.fri.smrpo.kis.core.businessLogic.database.Database;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.businessLogic.managers.base.BaseManager;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;

import javax.ws.rs.core.Response;
import java.util.List;

public class CustomerManager extends BaseManager<UserAccount> {

    public CustomerManager(Database database) {
        super(database, null);
    }

    public UserAccount get(AuthEntity authEntity) throws BusinessLogicTransactionException {
        if(authEntity.isInRole(AuthEntity.ROLE_CUSTOMER)){
            final String authId = authEntity.getId();
            List<UserAccount> userAccountList = database.getStream(UserAccount.class)
                    .where(e -> e.getAuthenticationId().equals(authId) && e.getIsLatest() && !e.getIsDeleted())
                    .toList();

            UserAccount dbUserAccount;
            if(userAccountList.isEmpty()){
                dbUserAccount = generate(authEntity);
                database.createVersion(dbUserAccount, authorizationManager);
            } else {
                dbUserAccount = userAccountList.get(0);
            }

            return dbUserAccount;
        } else {
            throw new BusinessLogicTransactionException(Response.Status.BAD_REQUEST, "Account is not of customer type.");
        }
    }

    private UserAccount generate(AuthEntity authEntity) {
        UserAccount userAccount = new UserAccount();
        userAccount.setAuthenticationId(authEntity.getId());
        userAccount.setEmail(authEntity.getEmail());
        userAccount.setName(authEntity.getName());
        userAccount.setSurname(authEntity.getSurname());

        return userAccount;
    }

}
