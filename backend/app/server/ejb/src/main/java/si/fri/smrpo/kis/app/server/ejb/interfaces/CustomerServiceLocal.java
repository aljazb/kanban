package si.fri.smrpo.kis.app.server.ejb.interfaces;

import si.fri.smrpo.kis.core.businessLogic.authentication.AuthEntity;
import si.fri.smrpo.kis.core.businessLogic.exceptions.BusinessLogicTransactionException;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;

public interface CustomerServiceLocal {

    UserAccount get(AuthEntity authEntity) throws BusinessLogicTransactionException;

}
