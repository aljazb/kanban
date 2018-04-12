package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.Request;

public class RequestAuthManager extends AuthManager<Request> {

    public RequestAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<Request> authCriteria() {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)){
                return cb.and(p, cb.or(
                        cb.equal(r.join("sender").get("id"), getUserId()),
                        cb.equal(r.join("receiver").get("id"), getUserId())));
            } else {
                return p;
            }
        };
    }

    @Override
    public boolean authCriteriaRequiresDistinct() {
        return true;
    }

    @Override
    public void authGet(DatabaseCore db, Request entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)) {
            if (!(entity.getSender().getId().equals(getUserId()) ||
                    entity.getReceiver().getId().equals(getUserId()))) {
                throw new DatabaseException("User is neither sender or receiver.", ExceptionType.INSUFFICIENT_RIGHTS);
            }
        }
    }
}
