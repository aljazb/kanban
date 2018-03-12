package si.fri.smrpo.kis.app.server.ejb.managers;

import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;

public interface UserAccountManagerLocal {

    UserAccount login(AuthEntity authEntity) throws BusinessLogicTransactionException;

}
