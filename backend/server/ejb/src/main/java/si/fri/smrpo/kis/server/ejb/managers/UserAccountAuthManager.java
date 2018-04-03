package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.logic.database.instance.DatabaseCore;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;


public class UserAccountAuthManager extends AuthManager<UserAccount> {

    private String search = null;

    public UserAccountAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<UserAccount> authCriteria() {
        return (p, cb, r) -> {
            if(search == null) {
                return p;
            } else {
                return cb.and(p, cb.or(
                        cb.like(r.get("username"), search),
                        cb.or(cb.like(r.get("email"), search),
                            cb.or(cb.like(r.get("firstName"), search),
                                cb.like(r.get("lastName"), search)))));
            }
        };
    }

    @Override
    public boolean authCriteriaRequiresDistinct() {
        return true;
    }

    @Override
    public void authGet(DatabaseCore db, UserAccount entity) throws DatabaseException {
        if(!isUserInRole(ROLE_ADMINISTRATOR)){
            if(!entity.getId().equals(getUserId())) {
                throw new DatabaseException("User does not have permission.",
                        LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
            }
        }
    }


    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
