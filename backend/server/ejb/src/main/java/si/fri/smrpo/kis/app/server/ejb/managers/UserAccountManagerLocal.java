package si.fri.smrpo.kis.app.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.server.entities.UserAccount;

public interface UserAccountManagerLocal {

    UserAccount login(UserAccount authEntity) throws BusinessLogicTransactionException;

}
