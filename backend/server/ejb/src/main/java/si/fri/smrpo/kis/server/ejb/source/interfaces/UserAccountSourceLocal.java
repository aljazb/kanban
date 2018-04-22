package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface UserAccountSourceLocal extends GetSourceImpl<UserAccount, UUID>, AuthImpl {

    String getSearch();
    void setSearch(String search);

    UserAccount login() throws LogicBaseException;
    UserAccount create(UserAccount entity) throws LogicBaseException;
    UserAccount update(UserAccount entity) throws LogicBaseException;

    UserAccount setEnabled(UUID id, Boolean enabled) throws LogicBaseException;
    void setPassword(UUID id, String password) throws LogicBaseException;

    void checkAvailability(UserAccount userAccount) throws LogicBaseException;

}
