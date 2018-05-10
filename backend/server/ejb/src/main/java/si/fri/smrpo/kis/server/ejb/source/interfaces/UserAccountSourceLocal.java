package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface UserAccountSourceLocal extends GetSourceImpl<UserAccount, UUID, UserAccount> {

    Paging<UserAccount> getList(Class<UserAccount> c, String search, QueryParameters param, UserAccount authUser) throws Exception;

    UserAccount login(UserAccount authUser) throws LogicBaseException;
    UserAccount create(UserAccount entity, UserAccount authUser) throws LogicBaseException;
    UserAccount update(UserAccount entity, UserAccount authUser) throws LogicBaseException;

    UserAccount setEnabled(UUID id, Boolean enabled, UserAccount authUser) throws LogicBaseException;
    void setPassword(UUID id, String password, UserAccount authUser) throws LogicBaseException;

    void checkAvailability(UserAccount userAccount, UserAccount authUser) throws LogicBaseException;

}
