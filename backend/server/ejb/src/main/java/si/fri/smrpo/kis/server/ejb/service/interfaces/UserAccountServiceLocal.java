package si.fri.smrpo.kis.server.ejb.service.interfaces;


import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface UserAccountServiceLocal {

    UserAccount login(UserAccount authEntity) throws LogicBaseException;
    UserAccount create(UserAccount authEntity) throws LogicBaseException;
    UserAccount update(UserAccount authEntity) throws LogicBaseException;
    UserAccount setEnabled(UUID id, Boolean enabled) throws LogicBaseException;
    void setPassword(UUID id, String password) throws LogicBaseException;

}
