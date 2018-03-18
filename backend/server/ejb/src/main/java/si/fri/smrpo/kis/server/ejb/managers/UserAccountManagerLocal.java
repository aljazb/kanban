package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public interface UserAccountManagerLocal {

    UserAccount login(UserAccount authEntity) throws BusinessLogicTransactionException;

}
