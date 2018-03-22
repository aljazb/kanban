package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public interface UserAccountServiceLocal {

    UserAccount login(UserAccount authEntity) throws DatabaseException;

}
